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
import org.speech.asr.recognition.acoustic.FeatureImpl;
import org.speech.asr.recognition.acoustic.NeuralHybridAcousticModel;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.decoder.Decoder;
import org.speech.asr.recognition.decoder.DecoderImpl;
import org.speech.asr.recognition.decoder.Result;
import org.speech.asr.recognition.decoder.ViterbiSearchManager;
import org.speech.asr.recognition.frontend.ContextPreprocessor;
import org.speech.asr.recognition.launcher.an4.util.An4Importer;
import org.speech.asr.recognition.launcher.an4.util.TranscribedUtterance;
import org.speech.asr.recognition.linguist.Dictionary;
import org.speech.asr.recognition.linguist.SimpleDictionary;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.SimpleLogScale;
import org.speech.asr.recognition.util.NeuralHybridAmSerializer;
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
 * Creation date: Aug 28, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class An4CvtDecoderTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(An4CvtDecoderTest.class.getName());

  private Dictionary dictionary;

  private File transcriptionFile;

  private File dictionaryFile;

  private File featureDir;

  private File modelFile;

  private ViterbiSearchManager searchManager;

  private NeuralHybridAcousticModel model;

  private Decoder decoder;

  public static void main(String[] args) {
    new An4CvtDecoderTest().start();
  }

  public void start() {
    AsrContext ctx = new AsrContext();
    ctx.setTransitionScored(false);
    LogScale logScale = new SimpleLogScale(1.0001);
    ctx.setLogScale(logScale);
    AsrContext.setContext(ctx);

    dictionaryFile = new File("/mnt/work/an4/an4.dic");
    //transcriptionFile = new File("/mnt/work/an4/test.txt");
    transcriptionFile = new File("/mnt/work/an4/micro-test.txt");
    featureDir = new File("/mnt/work/an4/an4-8/feat");
    modelFile = new File("/mnt/work/an4/nn_epoch26.xml");

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
      model = NeuralHybridAmSerializer.load(new FileInputStream(modelFile));
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
      item.setFeatures(createContextFeatures(features));
      item.setTranscription(transcription);
      testSet.add(item);
    }

    return testSet;
  }

  protected List<Feature> createContextFeatures(List<Feature> features) {
    int contextSize = 3;
    ContextPreprocessor preprocessor = new ContextPreprocessor(contextSize);
    List<Feature> contextFeatures = new LinkedList();
    int seq = 0;
    for (int i = contextSize; i < features.size() - contextSize; i++) {
      List<Feature> window = features.subList(i - contextSize, i + contextSize + 1);
      FeatureImpl feat = (FeatureImpl) preprocessor.process(window);
      feat.setSequenceNumber(seq++);
      contextFeatures.add(feat);
    }
    return contextFeatures;
  }

  protected void initDecoder(List<Item> testSet) {

    List<String> sentenceList = new LinkedList();
    for (Item sentence : testSet) {
      String txt = sentence.getTranscription().trim();
      sentenceList.add(txt.trim());
    }
    searchManager = new ViterbiSearchManager();
    searchManager.setBeamWidth(30);
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
