/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.baumwelch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.*;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.decoder.Decoder;
import org.speech.asr.recognition.decoder.DecoderImpl;
import org.speech.asr.recognition.decoder.Result;
import org.speech.asr.recognition.decoder.ViterbiSearchManager;
import org.speech.asr.recognition.linguist.WordListSearchGraphBuilder;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.SimpleLogScale;
import org.speech.asr.recognition.trainer.TrainSentence;
import org.speech.asr.recognition.trainer.baumwelch.mock.DictionaryMock;
import org.speech.asr.recognition.trainer.baumwelch.vo.Symbol;
import org.speech.asr.recognition.trainer.util.DataGenerator;
import org.speech.asr.recognition.trainer.util.ZeroVarianceDataGenerator;
import org.speech.asr.recognition.util.GmmAmSerializer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Test generujacy alfabet symboli, kazdy symbol generuje obserwacje o zadanym rozkladzie.
 * Na postawie wygenerowanych obserwacji tworzony jest zbior uczacy i zbior testujacy.
 * Za pomoca zbioru testujace sprawdzamy poprawnosc algorytmu bauma welcha.
 * Sprawdzanie poprawnosci to wyliczenie prawd. a posteriori i wybranie slowa z maksymalna
 * <p/>
 * Creation date: Jun 22, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class FullBaumWelchTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(FullBaumWelchTest.class.getName());

  private Map<String, Symbol> symbols;

  private GmmAcousticModel model;

  private int noSymbols;

  private double avgObservationDistance;

  private double avgObservationDeviation;

  private int trainSentenceLength;

  private int testSentenceLength;

  private int noTrainSentences;

  private int noTestSentences;

  private int noDimensions;

  private int noMixtures;

  private List<TrainSentence> trainingSet;

  private List<TrainSentence> testSet;

  private LogScale logScale;

  private DataGenerator dataGenerator;

  private int noCycles;

  @Test
  public void testBaumWelchAlg() {
//    for (int i = 0; i < 11; i++) {
    AsrContext ctx = new AsrContext();
    ctx.setTransitionScored(false);
    logScale = new SimpleLogScale(1.0000001);
    ctx.setLogScale(logScale);
    ctx.setDeviationFloor(1E-12);
    ctx.setWorkDir(new File("/mnt/work/an4/work"));
    AsrContext.setContext(ctx);

    readParameters();
    generateSymbols();
    generateCvSets();
    performTrain();
    try {
      GmmAmSerializer.save(model, new FileOutputStream("/home/luol/gmm.xml"));

      model = GmmAmSerializer.load(new FileInputStream("/home/luol/gmm.xml"));
    } catch (IOException e) {
      log.error("", e);
      Assert.assertTrue(false);
    }
    performTests();

//    }
  }

  protected void readParameters() {
    noSymbols = 10;
    avgObservationDistance = 1E-5;//10;
    avgObservationDeviation = 1E-5;//10;
    trainSentenceLength = 10;
    testSentenceLength = 8;
    noTrainSentences = 100;
    noTestSentences = 10;
    noDimensions = 10;
    noMixtures = 3;
    noCycles = 20;
  }

  protected void generateSymbols() {
    dataGenerator = new ZeroVarianceDataGenerator();//SyntheticDataGenerator();//ZeroVarianceDataGenerator();
    dataGenerator.setNoSymbols(noSymbols);
    dataGenerator.setAvgObservationDeviation(avgObservationDeviation);
    dataGenerator.setAvgObservationDistance(avgObservationDistance);
    dataGenerator.setLogScale(logScale);
    dataGenerator.setNoDimensions(noDimensions);
    dataGenerator.setNoMixtures(noMixtures);
    dataGenerator.generateSymbols();

    model = new GmmAcousticModel(noDimensions, noMixtures);
    for (Symbol symbol : dataGenerator.getSymbols().values()) {
      Phoneme phoneme = new Phoneme(symbol.getName(), 1);
      model.addPhoneticUnit(phoneme);
    }
  }

  protected void generateCvSets() {
    trainingSet = dataGenerator.generateSentenceSet(noTrainSentences, trainSentenceLength, 15, 3, model);
    testSet = dataGenerator.generateSentenceSet(noTestSentences, testSentenceLength, 15, 3, model);
  }

  protected void performTrain() {
    BaumWelchTrainManager manager = new BaumWelchTrainManager(model, trainingSet, noMixtures, noCycles, 1E-50, true);
    manager.train();
  }

  protected String getTxtSentence(List<PhoneticUnit> transcription) {
    String str = "";
    for (PhoneticUnit unit : transcription) {
      str = str + unit.getName() + " ";
    }
    return str;
  }

  protected Hmm buildSentenceHmm(TrainSentence sentence) {
    LeftRightHmmBuilder builder = new LeftRightHmmBuilder();

    for (PhoneticUnit<StateDescriptor> unit : sentence.getTranscription()) {
      for (StateDescriptor state : unit.getStatesSequence()) {
        builder.addState(state);
      }
    }

    return builder.getHmm();
  }

  private org.speech.asr.recognition.linguist.Dictionary dictionary;

  private ViterbiSearchManager searchManager;

  private Decoder decoder;

  protected void initDecoder() {
    dictionary = new DictionaryMock();

    List<String> wordList = new LinkedList();
    for (TrainSentence sentence : trainingSet) {
      String txt = getTxtSentence(sentence.getTranscription());
      wordList.add(txt.trim());
    }
    searchManager = new ViterbiSearchManager();
    searchManager.setBeamWidth(50);
    WordListSearchGraphBuilder builder = new WordListSearchGraphBuilder();
    builder.setDictionary(dictionary);
    builder.setModel(model);
    builder.setNullProbability(0.0);
    builder.setWords(wordList);
    searchManager.setSearchGraphBuilder(builder);
    searchManager.init();

    DecoderImpl decoder = new DecoderImpl();
    decoder.setSearchManager(searchManager);
    this.decoder = decoder;
  }

  protected List<Result> getSentenceForFeatures(List<Feature> features) {
    for (Feature feature : features) {
      decoder.decode(feature);
    }
    List<Result> results = decoder.getAllHypotheses();

    return results;
  }

  protected void performTests() {
    initDecoder();
    int ok = 0;
    int er = 0;
    log.info("Test set size {}", trainingSet.size());
    for (TrainSentence sentence : trainingSet) {
      String real = getTxtSentence(sentence.getTranscription()).trim();
      log.debug("Decoding {}", real);
      List<Result> results = getSentenceForFeatures(sentence.getObservations());

      if (real.equals(results.get(0).getUtterance().trim())) {
        log.info("Best matching word for {} is {} ok=" + ok, real, results.get(0));
        ok++;
      } else {
        log.error("Best matching word for {} is" + results.size() + " {} ", real, results);
        er++;
      }
    }
    log.info("Recognized {} words, errors {}", ok, er);
    Assert.assertEquals(0, er);
  }

}
