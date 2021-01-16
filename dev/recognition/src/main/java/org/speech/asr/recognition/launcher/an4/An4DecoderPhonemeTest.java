/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.launcher.an4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.acoustic.GmmAcousticModel;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.decoder.Decoder;
import org.speech.asr.recognition.decoder.DecoderImpl;
import org.speech.asr.recognition.decoder.Result;
import org.speech.asr.recognition.decoder.ViterbiSearchManager;
import org.speech.asr.recognition.launcher.an4.util.An4Importer;
import org.speech.asr.recognition.launcher.an4.util.TranscribedUtterance;
import org.speech.asr.recognition.linguist.Dictionary;
import org.speech.asr.recognition.linguist.SimpleDictionary;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.SimpleLogScale;
import org.speech.asr.recognition.util.GmmAmSerializer;
import org.speech.asr.recognition.util.SerializerUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 15, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class An4DecoderPhonemeTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(An4DecoderPhonemeTest.class.getName());

  private GmmAcousticModel model;

  private Dictionary dictionary;

  private File transcriptionFile;

  private File dictionaryFile;

  private File featureDir;

  private File modelFile;

  private ViterbiSearchManager searchManager;

  private Decoder decoder;

  public static void main(String[] args) {
    new An4DecoderPhonemeTest().start();
  }

  public void start() {
    AsrContext ctx = new AsrContext();
    ctx.setTransitionScored(false);
    LogScale logScale = new SimpleLogScale(1.0001);
    ctx.setLogScale(logScale);
    AsrContext.setContext(ctx);

    dictionaryFile = new File("/mnt/work/an4/an4.dic");
    transcriptionFile = new File("/mnt/work/an4/test.txt");
    featureDir = new File("/mnt/work/an4/an4-8/feat");
    modelFile = new File("/mnt/work/an4/iteration12.xml");

    createDictionary();
    createModel();
    performTests(createTestSet());
  }

  private void createDictionary() {
    Map<String, String> dictMap = An4Importer.importDictionary(dictionaryFile);
    dictionary = new SimpleDictionary(dictMap);
  }

  private void createModel() {
    try {
      model = GmmAmSerializer.load(new FileInputStream(modelFile));
    } catch (IOException e) {
      log.error("", e);
      System.exit(1);
    }
  }

  private List<Item> createTestSet() {
    List<Item> testSet = new LinkedList();

    List<TranscribedUtterance> trainSet =
        An4Importer.parseTranscriptionFile(transcriptionFile);
    for (TranscribedUtterance tu : trainSet) {
      log.debug("Loading {}", tu.getRelativePath());
      File file = new File(featureDir, tu.getRelativePath() + ".feat");
      List<Feature> features = (List<Feature>) SerializerUtils.load(file);
      String transcription = tu.getTranscription();
      Item item = new Item();
      item.setFeatures(features);
      item.setTranscription(transcription);
      testSet.add(item);
    }

    return testSet;
  }

  protected void initDecoder(List<Item> testSet) {

    List<String> sentenceList = new LinkedList();
    for (Item sentence : testSet) {
      String txt = sentence.getTranscription().trim();
      sentenceList.add(txt.trim());
    }
    searchManager = new ViterbiSearchManager();
    searchManager.setBeamWidth(400);
    An4SearchGraphBuilder builder = new An4SearchGraphBuilder();
    builder.setDictionary(dictionary);
    builder.setModel(model);
    builder.setNullProbability(0.0);
    builder.setSentences(sentenceList);
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

  protected void performTests(List<Item> testSet) {
    initDecoder(testSet);
    int ok = 0;
    int er = 0;
    log.info("Test set size {}", testSet.size());
    for (Item sentence : testSet) {
      log.info("Item {}", ok + er + 1);
      String real = sentence.getTranscription().trim();
      log.debug("Decoding {}", real);
      List<Result> results = getSentenceForFeatures(sentence.getFeatures());
      if (results.size() != 0 && real.equals(results.get(0).getUtterance().trim())) {
        log.info("Best matching word for {} is {} ok=" + ++ok, real, results.get(0));
      } else {
        log.error("Best matching word for {} is " + results.size() + " {} ", real, results);
        er++;
      }
    }
    log.info("Recognized {} words, errors {}", ok, er);
  }

  private class Item {
    private List<Feature> features;

    private String transcription;

    public List<Feature> getFeatures() {
      return features;
    }

    public void setFeatures(List<Feature> features) {
      this.features = features;
    }

    public String getTranscription() {
      return transcription;
    }

    public void setTranscription(String transcription) {
      this.transcription = transcription;
    }
  }

}
