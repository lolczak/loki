/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.cvt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.*;
import org.speech.asr.recognition.ann.*;
import org.speech.asr.recognition.ann.joone.ParallelNeuralNet;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.decoder.Decoder;
import org.speech.asr.recognition.decoder.DecoderImpl;
import org.speech.asr.recognition.decoder.Result;
import org.speech.asr.recognition.decoder.ViterbiSearchManager;
import org.speech.asr.recognition.frontend.SimpleMapper;
import org.speech.asr.recognition.linguist.WordListSearchGraphBuilder;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.SimpleLogScale;
import org.speech.asr.recognition.trainer.TrainSentence;
import org.speech.asr.recognition.trainer.baumwelch.mock.DictionaryMock;
import org.speech.asr.recognition.trainer.baumwelch.vo.Symbol;
import org.speech.asr.recognition.trainer.util.DataGenerator;
import org.speech.asr.recognition.trainer.util.SyntheticDataGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 1, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class CvtTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(CvtTest.class.getName());

  private NeuralHybridAcousticModel model;

  private int noSymbols;

  private double avgObservationDistance;

  private double avgObservationDeviation;

  private int trainSentenceLength;

  private int testSentenceLength;

  private int noTrainSentences;

  private int noTestSentences;

  private int noDimensions;

  private int noMixtures;

  private LogScale logScale;

  private DataGenerator dataGenerator;

  private List<TrainSentence> trainingSet;

  private NeuralNetwork neuralNet;

  private volatile CountDownLatch doneSignal;


  public void catchTest() {
    doneSignal = new CountDownLatch(2);
    doneSignal.countDown();
    doneSignal.countDown();
    try {
      log.info("Waiting");
      doneSignal.await();
    } catch (InterruptedException e) {
      log.error("", e);
    }
    log.info("Finished");

  }

  @Test
  public void simpleTest() {
    AsrContext ctx = new AsrContext();
    ctx.setTransitionScored(false);
    logScale = new SimpleLogScale(1.0001);
    ctx.setLogScale(logScale);
    AsrContext.setContext(ctx);

    readParameters();
    generateSymbols();
    generateCvSets();
    performTrain();
    performTests();
  }

  protected void readParameters() {
    noSymbols = 15;
    avgObservationDistance = 10;
    avgObservationDeviation = 10;
    trainSentenceLength = 8;
    testSentenceLength = 8;
    noTrainSentences = 100;
    noTestSentences = 10;
    noDimensions = 6;
    noMixtures = 3;
  }

  protected void generateSymbols() {
    dataGenerator = new SyntheticDataGenerator();
    dataGenerator.setNoSymbols(noSymbols);
    dataGenerator.setAvgObservationDeviation(avgObservationDeviation);
    dataGenerator.setAvgObservationDistance(avgObservationDistance);
    dataGenerator.setLogScale(logScale);
    dataGenerator.setNoDimensions(noDimensions);
    dataGenerator.setNoMixtures(noMixtures);
    dataGenerator.generateSymbols();

    neuralNet = buildNet();
    model = new NeuralHybridAcousticModel(neuralNet, new SimpleMapper());
    int number = 0;
    for (Symbol symbol : dataGenerator.getSymbols().values()) {
      NeuralStateDescriptor state = new NeuralStateDescriptor(symbol.getName() + "_1", logScale.getLogHalf(), number++);
      List<NeuralStateDescriptor> states = new LinkedList();
      states.add(state);
      Phoneme phoneme = new Phoneme(symbol.getName(), states);
      model.addPhoneticUnit(phoneme);
    }
  }

  protected NeuralNetwork buildNet() {
    //noDimensions, noDimensions * noSymbols, noSymbols

    JooneNeuralNetBuilder builder = new JooneNeuralNetBuilder();
    builder.addLayer("input", LayerType.LINEAR, noDimensions, LayerPosition.INPUT);
//    builder.addLayer("input2", LayerType.LINEAR, 2, LayerPosition.INPUT);
    builder.addLayer("hidden", LayerType.TANH, noDimensions * noSymbols, LayerPosition.HIDDEN);
    builder.addLayer("output", LayerType.SOFT_MAX, noSymbols, LayerPosition.OUTPUT);

    builder.connect("input", "hidden", SynapseType.FULL);
//    builder.connect("input2", "hidden", SynapseType.FULL);
    builder.connect("hidden", "output", SynapseType.FULL);

    //return builder.getNeuralNetwork();
    return new ParallelNeuralNet(builder.getNeuralNet());
  }

  protected void generateCvSets() {
    trainingSet = dataGenerator.generateSentenceSet(noTrainSentences, trainSentenceLength, 10, 8, model);
//    testSet = dataGenerator.generateSentenceSet(noTestSentences, testSentenceLength, 15, 3, model);
  }

  protected void performTrain() {
    CvtManager manager = new CvtManager(model, trainingSet);
    manager.train();
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

  protected String getTxtSentence(List<PhoneticUnit> transcription) {
    String str = "";
    for (PhoneticUnit unit : transcription) {
      str = str + unit.getName() + " ";
    }
    return str;
  }
}
