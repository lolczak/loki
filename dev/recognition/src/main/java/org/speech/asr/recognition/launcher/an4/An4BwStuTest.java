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
import org.speech.asr.recognition.acoustic.Phoneme;
import org.speech.asr.recognition.acoustic.PhoneticUnit;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.launcher.an4.util.An4Importer;
import org.speech.asr.recognition.launcher.an4.util.TranscribedUtterance;
import org.speech.asr.recognition.linguist.Dictionary;
import org.speech.asr.recognition.linguist.Pronunciation;
import org.speech.asr.recognition.linguist.SimpleDictionary;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.SimpleLogScale;
import org.speech.asr.recognition.trainer.TrainSentence;
import org.speech.asr.recognition.trainer.baumwelch.BaumWelchTrainManager;
import org.speech.asr.recognition.util.GmmAmSerializer;
import org.speech.asr.recognition.util.SerializerUtils;
import org.speech.asr.recognition.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 16, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class An4BwStuTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(An4BwStuTest.class.getName());

  private GmmAcousticModel model;

  private Dictionary dictionary;

  private int noDimensions;

  private int noMixtures;

  private LogScale logScale;

  private int noCycles;

  private double deviationFloor;

  private int noStatesPerStationary;

  private int noStatesPerTransition;

  private double minRelativeErrorChange;

  private double logBase;

  private boolean dump;

  private File workDir;

  private int samplingRate;

  private File transcriptionFile;

  private File dictionaryFile;

  private File phoneSetFile;

  private File amFile;

  private File featureDir;

  public void start() {
    noDimensions = 39;
    logParameters();
    AsrContext ctx = new AsrContext();
    ctx.setTransitionScored(false);
    logScale = new SimpleLogScale(logBase);
    ctx.setLogScale(logScale);
    ctx.setDeviationFloor(deviationFloor);
    ctx.setWorkDir(workDir);
    AsrContext.setContext(ctx);
    createDictionary();
    createModel();

    try {
      BaumWelchTrainManager manager =
          new BaumWelchTrainManager(model, createTrainSet(), noMixtures, noCycles, minRelativeErrorChange, dump);
      manager.train();
    } catch (Exception e) {
      log.error("Exception occurred during training", e);
      System.exit(1);
    }
    try {
      GmmAmSerializer.save(model, new FileOutputStream(amFile));
    } catch (IOException e) {
      log.error("", e);
    }

  }

  private void logParameters() {
    log.info("No mixtures: {}", noMixtures);
    log.info("No dimensions: {}", noDimensions);
    log.info("No cycles: {}", noCycles);
    log.info("Deviation floor: {}", deviationFloor);
    log.info("No states per stationary part: {}", noStatesPerStationary);
    log.info("No states per transition part: {}", noStatesPerTransition);
    log.info("minRelativeErrorChange: {}", minRelativeErrorChange);
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
  }

  private void createDictionary() {
    Map<String, String> dictMap = An4Importer.importDictionary(dictionaryFile);
    dictionary = new SimpleDictionary(dictMap);
  }

  private void createModel() {
    List<String> phoneSet = An4Importer.importPhoneSet(phoneSetFile);
    model = new GmmAcousticModel(noDimensions, noMixtures);
    for (String symbol : phoneSet) {
      Phoneme phoneme = new Phoneme(symbol, noStatesPerStationary);
      model.addPhoneticUnit(phoneme);
    }
    //filler
    Phoneme phoneme = new Phoneme("<sil>", noStatesPerStationary);
    model.addPhoneticUnit(phoneme);
    phoneme = new Phoneme("</sil>", noStatesPerStationary);
    model.addPhoneticUnit(phoneme);
  }

  private List<TrainSentence> createTrainSet() {
//    AudioFormat format =
//        new AudioFormat(AudioFormat.LINEAR, samplingRate, 16, 1, AudioFormat.LITTLE_ENDIAN, AudioFormat.SIGNED);
    List<TrainSentence> trainSentences = new LinkedList();

    List<TranscribedUtterance> trainSet =
        An4Importer.parseTranscriptionFile(transcriptionFile);
    for (TranscribedUtterance tu : trainSet) {
      log.debug("Loading {}", tu.getRelativePath());
      File file = new File(featureDir, tu.getRelativePath() + ".feat");
      List<Feature> features = (List<Feature>) SerializerUtils.load(file);
      List<PhoneticUnit> transcription = createTranscription(tu.getTranscription());
      TrainSentence trainSentence = new TrainSentence();
      trainSentence.setObservations(features);
      trainSentence.setTranscription(transcription);
      trainSentences.add(trainSentence);
    }

    return trainSentences;
  }

  private List<PhoneticUnit> createTranscription(String txt) {
    List<String> words = StringUtils.parse(txt);
    PhoneticUnit startSil = model.getPhoneticUnit("<sil>");
    List<PhoneticUnit> result = new LinkedList();
    result.add(startSil);
    PhoneticUnit lastPhoneme = startSil;
    for (String word : words) {
      Pronunciation pronunciation = dictionary.getPronunciation(word);
      for (String phoneme : pronunciation.getPhonemes()) {
        PhoneticUnit actualPhoneme = model.getPhoneticUnit(phoneme);
        String transitionSymbol = lastPhoneme.getName() + "-" + actualPhoneme.getName();
        PhoneticUnit transitionPart = model.getPhoneticUnit(transitionSymbol);
        if (transitionPart == null) {
          transitionPart = new Phoneme(transitionSymbol, noStatesPerTransition);
          model.addPhoneticUnit(transitionPart);
        }
        result.add(transitionPart);
        result.add(actualPhoneme);
        lastPhoneme = actualPhoneme;
      }
    }
    PhoneticUnit stopSil = model.getPhoneticUnit("</sil>");


    String transitionSymbol = lastPhoneme.getName() + "-" + stopSil.getName();
    PhoneticUnit transitionPart = model.getPhoneticUnit(transitionSymbol);
    if (transitionPart == null) {
      transitionPart = new Phoneme(transitionSymbol, noStatesPerTransition);
      model.addPhoneticUnit(transitionPart);
    }
    result.add(transitionPart);
    result.add(stopSil);
    return result;
  }

  public void setNoStatesPerStationary(int noStatesPerStationary) {
    this.noStatesPerStationary = noStatesPerStationary;
  }

  public void setNoStatesPerTransition(int noStatesPerTransition) {
    this.noStatesPerTransition = noStatesPerTransition;
  }

  public void setWorkDir(File workDir) {
    this.workDir = workDir;
  }

  public void setTranscriptionFile(File transcriptionFile) {
    this.transcriptionFile = transcriptionFile;
  }

  public void setSamplingRate(int samplingRate) {
    this.samplingRate = samplingRate;
  }

  public void setPhoneSetFile(File phoneSetFile) {
    this.phoneSetFile = phoneSetFile;
  }

  public void setNoMixtures(int noMixtures) {
    this.noMixtures = noMixtures;
  }

  public void setNoCycles(int noCycles) {
    this.noCycles = noCycles;
  }

  public void setMinRelativeErrorChange(double minRelativeErrorChange) {
    this.minRelativeErrorChange = minRelativeErrorChange;
  }

  public void setLogBase(double logBase) {
    this.logBase = logBase;
  }

  public void setDump(boolean dump) {
    this.dump = dump;
  }

  public void setDictionaryFile(File dictionaryFile) {
    this.dictionaryFile = dictionaryFile;
  }

  public void setDeviationFloor(double deviationFloor) {
    this.deviationFloor = deviationFloor;
  }

  public void setAmFile(File amFile) {
    this.amFile = amFile;
  }

  public void setFeatureDir(File featureDir) {
    this.featureDir = featureDir;
  }
}
