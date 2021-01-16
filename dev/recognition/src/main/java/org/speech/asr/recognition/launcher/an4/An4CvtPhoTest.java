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
import org.speech.asr.recognition.acoustic.*;
import org.speech.asr.recognition.ann.*;
import org.speech.asr.recognition.ann.joone.ParallelNeuralNet;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.frontend.MfccMapper;
import org.speech.asr.recognition.launcher.an4.util.An4Importer;
import org.speech.asr.recognition.launcher.an4.util.TranscribedUtterance;
import org.speech.asr.recognition.linguist.Dictionary;
import org.speech.asr.recognition.linguist.PhonemeTranscriptionFactory;
import org.speech.asr.recognition.linguist.SimpleDictionary;
import org.speech.asr.recognition.linguist.TranscriptionFactory;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.SimpleLogScale;
import org.speech.asr.recognition.trainer.TrainSentence;
import org.speech.asr.recognition.trainer.cvt.CvtManager;
import org.speech.asr.recognition.util.GmmAmSerializer;
import org.speech.asr.recognition.util.PriorEstimator;
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
 * Creation date: Aug 23, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class An4CvtPhoTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(An4CvtPhoTest.class.getName());

  private NeuralHybridAcousticModel model;

  private Dictionary dictionary;

  private LogScale logScale;

  private int noDimensions;

  private int noStatesPerPhoneme;

  private double logBase;

  private boolean dump;

  private File workDir;

  private int samplingRate;

  private File transcriptionFile;

  private File dictionaryFile;

  private File phoneSetFile;

  private File amFile;

  private File featureDir;

  private File bootstrapModelFile;

  private NeuralNetwork neuralNet;

  private int noOutputs;

  private int batchSize;

  private double alphaInit;

  private double alphaDelta;

  private double learningRate;

  private double randomAmplitude;

  private double momentum;

  private int firstEpochNoCycles;

  private int noCycles;

  private int hiddenUnits;

  private TranscriptionFactory transcriptionFactory;

  private PriorEstimator priorEstimator;

  public void start() {
    noDimensions = 39;
    noOutputs = 35;
    logParameters();
    AsrContext ctx = new AsrContext();
    ctx.setTransitionScored(false);
    logScale = new SimpleLogScale(logBase);
    ctx.setLogScale(logScale);
    ctx.setWorkDir(workDir);
    AsrContext.setContext(ctx);
    createDictionary();
    List<TranscribedUtterance> trainSet =
        An4Importer.parseTranscriptionFile(transcriptionFile);
    priorEstimator = new PriorEstimator();
    log.info("Estimating prior probabilities...");
    priorEstimator.estimate(trainSet, dictionary);
    createModel();
    transcriptionFactory = new PhonemeTranscriptionFactory(model, dictionary);
    try {
      GmmAcousticModel bootstrapModel = null;
      try {
        bootstrapModel = GmmAmSerializer.load(new FileInputStream(bootstrapModelFile));
      } catch (IOException e) {
        log.error("", e);
        System.exit(1);
      }
      CvtManager manager = new CvtManager(model, createTrainSet(trainSet));
      manager.setBootstrapModel(bootstrapModel);
      manager.setBatchSize(batchSize);
      manager.setAlpha(alphaInit);
      manager.setAlphaDelta(alphaDelta);
      manager.setLearningRate(learningRate);
      manager.setMomentum(momentum);
      manager.setRandomWeightAmpl(randomAmplitude);
      manager.setFirstEpochNoCycles(firstEpochNoCycles);
      manager.setNoCycles(noCycles);
      manager.setInputMapper(new MfccMapper());
      manager.train();
    } catch (Exception e) {
      log.error("Exception occurred during training", e);
      System.exit(1);
    }
  }

  private void logParameters() {
    log.info("No dimensions: {}", noDimensions);
    log.info("No states per phoneme: {}", noStatesPerPhoneme);
    log.info("logBase: {}", logBase);
    log.info("dump: {}", dump);
    log.info("samplingRate: {}", samplingRate);
    log.info("workDir: {}", workDir);
    log.info("featureDir: {}", featureDir);
    log.info("transcriptionFile: {}", transcriptionFile);
    log.info("dictionaryFile: {}", dictionaryFile);
    log.info("phoneSetFile: {}", phoneSetFile);
    log.info("amFile: {}", amFile);
    log.info("Available processors: {}", Runtime.getRuntime().availableProcessors());
    log.info("Current dir: {}", System.getProperty("user.dir"));
    log.info("Alpha init: {}", alphaInit);
    log.info("Hidden units: {}", hiddenUnits);
    log.info("Batch size: {}", batchSize);
  }

  private void createDictionary() {
    Map<String, String> dictMap = An4Importer.importDictionary(dictionaryFile);
    dictionary = new SimpleDictionary(dictMap);
  }

  protected NeuralNetwork buildNet() {
    //noDimensions, noDimensions * noSymbols, noSymbols

    JooneNeuralNetBuilder builder = new JooneNeuralNetBuilder();

    builder.addLayer("E", LayerType.LINEAR, 1, LayerPosition.INPUT);
    builder.addLayer("CEP", LayerType.LINEAR, 12, LayerPosition.INPUT);
    builder.addLayer("dE", LayerType.LINEAR, 1, LayerPosition.INPUT);
    builder.addLayer("dCEP", LayerType.LINEAR, 12, LayerPosition.INPUT);
    builder.addLayer("ddE", LayerType.LINEAR, 1, LayerPosition.INPUT);
    builder.addLayer("ddCEP", LayerType.LINEAR, 12, LayerPosition.INPUT);

    builder.addLayer("E_hidden", LayerType.SIGMOID, 5, LayerPosition.HIDDEN);
    builder.addLayer("CEP_hidden", LayerType.SIGMOID, 30, LayerPosition.HIDDEN);
    builder.addLayer("dE_hidden", LayerType.SIGMOID, 5, LayerPosition.HIDDEN);
    builder.addLayer("dCEP_hidden", LayerType.SIGMOID, 30, LayerPosition.HIDDEN);
    builder.addLayer("ddE_hidden", LayerType.SIGMOID, 5, LayerPosition.HIDDEN);
    builder.addLayer("ddCEP_hidden", LayerType.SIGMOID, 30, LayerPosition.HIDDEN);

    builder.addLayer("second_hidden", LayerType.SIGMOID, hiddenUnits, LayerPosition.HIDDEN);
    builder.addLayer("output", LayerType.SOFT_MAX, 3 * noOutputs, LayerPosition.OUTPUT);

    builder.connect("E", "E_hidden", SynapseType.FULL);
    builder.connect("CEP", "CEP_hidden", SynapseType.FULL);
    builder.connect("dE", "dE_hidden", SynapseType.FULL);
    builder.connect("dCEP", "dCEP_hidden", SynapseType.FULL);
    builder.connect("ddE", "ddE_hidden", SynapseType.FULL);
    builder.connect("ddCEP", "ddCEP_hidden", SynapseType.FULL);

    builder.connect("E_hidden", "second_hidden", SynapseType.FULL);
    builder.connect("CEP_hidden", "second_hidden", SynapseType.FULL);
    builder.connect("dE_hidden", "second_hidden", SynapseType.FULL);
    builder.connect("dCEP_hidden", "second_hidden", SynapseType.FULL);
    builder.connect("ddE_hidden", "second_hidden", SynapseType.FULL);
    builder.connect("ddCEP_hidden", "second_hidden", SynapseType.FULL);

    builder.connect("second_hidden", "output", SynapseType.FULL);

    return new ParallelNeuralNet(builder.getNeuralNet());
  }

  private void createModel() {
    List<String> phoneSet = An4Importer.importPhoneSet(phoneSetFile);
    phoneSet.add("<sil>");
    phoneSet.add("</sil>");
    assert noOutputs == phoneSet.size();
    neuralNet = buildNet();
    model = new NeuralHybridAcousticModel(neuralNet, new MfccMapper());
    int number = 0;
    for (String symbol : phoneSet) {
      List<NeuralStateDescriptor> states = new LinkedList();
      double logPrior = logScale.linearToLog(priorEstimator.getPrior(symbol));
      log.info("Setting log prior probability for class {} = {}", symbol, logPrior);
      for (int i = 0; i < noStatesPerPhoneme; i++) {
        NeuralStateDescriptor state =
            new NeuralStateDescriptor(symbol + "_" + (i + 1), logScale.getLogHalf(), number++);
        state.setLogPriorProbability(logPrior);
        states.add(state);
      }
      Phoneme phoneme = new Phoneme(symbol, states);
      model.addPhoneticUnit(phoneme);
    }
  }

  private List<TrainSentence> createTrainSet(List<TranscribedUtterance> trainSet) {
    List<TrainSentence> trainSentences = new LinkedList();

    for (TranscribedUtterance tu : trainSet) {
      log.debug("Loading {}", tu.getRelativePath());
      File file = new File(featureDir, tu.getRelativePath() + ".feat");
      List<Feature> features = (List<Feature>) SerializerUtils.load(file);
      List<PhoneticUnit> transcription = transcriptionFactory.createTranscription(tu.getTranscription());
      TrainSentence trainSentence = new TrainSentence();
      trainSentence.setObservations(features);
      trainSentence.setTranscription(transcription);
      trainSentences.add(trainSentence);
    }
    return trainSentences;
  }

  public void setDictionaryFile(File dictionaryFile) {
    this.dictionaryFile = dictionaryFile;
  }

  public void setFeatureDir(File featureDir) {
    this.featureDir = featureDir;
  }

  public void setPhoneSetFile(File phoneSetFile) {
    this.phoneSetFile = phoneSetFile;
  }

  public void setTranscriptionFile(File transcriptionFile) {
    this.transcriptionFile = transcriptionFile;
  }

  public void setWorkDir(File workDir) {
    this.workDir = workDir;
  }

  public void setSamplingRate(int samplingRate) {
    this.samplingRate = samplingRate;
  }

  public void setNoDimensions(int noDimensions) {
    this.noDimensions = noDimensions;
  }

  public void setNoStatesPerPhoneme(int noStatesPerPhoneme) {
    this.noStatesPerPhoneme = noStatesPerPhoneme;
  }

  public void setLogBase(double logBase) {
    this.logBase = logBase;
  }

  public void setDump(boolean dump) {
    this.dump = dump;
  }

  public void setAmFile(File amFile) {
    this.amFile = amFile;
  }

  public void setBootstrapModelFile(File bootstrapModelFile) {
    this.bootstrapModelFile = bootstrapModelFile;
  }

  public void setAlphaInit(double alphaInit) {
    this.alphaInit = alphaInit;
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  public void setLearningRate(double learningRate) {
    this.learningRate = learningRate;
  }

  public void setMomentum(double momentum) {
    this.momentum = momentum;
  }

  public void setRandomAmplitude(double randomAmplitude) {
    this.randomAmplitude = randomAmplitude;
  }

  public void setFirstEpochNoCycles(int firstEpochNoCycles) {
    this.firstEpochNoCycles = firstEpochNoCycles;
  }

  public void setNoCycles(int noCycles) {
    this.noCycles = noCycles;
  }

  public void setHiddenUnits(int hiddenUnits) {
    this.hiddenUnits = hiddenUnits;
  }

  public void setAlphaDelta(double alphaDelta) {
    this.alphaDelta = alphaDelta;
  }

  public void setContextSize(int contextSize) {

  }
}
