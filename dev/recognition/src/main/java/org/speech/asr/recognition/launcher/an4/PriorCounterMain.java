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
import org.speech.asr.recognition.launcher.an4.util.An4Importer;
import org.speech.asr.recognition.launcher.an4.util.TranscribedUtterance;
import org.speech.asr.recognition.linguist.Dictionary;
import org.speech.asr.recognition.linguist.SimpleDictionary;
import org.speech.asr.recognition.util.PriorEstimator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 30, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class PriorCounterMain {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(PriorCounterMain.class.getName());

  private Dictionary dictionary;

  private File transcriptionFile;

  private File dictionaryFile;

  public static void main(String[] args) {
    new PriorCounterMain().start();
  }

  public void start() {
    transcriptionFile = new File("/mnt/work/an4/train.txt");
    dictionaryFile = new File("/mnt/work/an4/an4.dic");
    List<TranscribedUtterance> trainSet =
        An4Importer.parseTranscriptionFile(transcriptionFile);
    createDictionary();
    PriorEstimator priorEstimator = new PriorEstimator();
    priorEstimator.estimate(trainSet, dictionary);
    try {
      FileWriter statsWriter = new FileWriter("/mnt/work/an4/stats.csv");
      log.info("Total no: {}", priorEstimator.getNoSamples());
      for (String token : priorEstimator.getTokens()) {
        log.info("Prior for {} is {}", token, priorEstimator.getPrior(token));
        statsWriter.write(token + "," + priorEstimator.getCount(token) + "\n");
      }
      statsWriter.close();
    } catch (IOException e) {
      log.error("", e);
    }
  }


  private void createDictionary() {
    Map<String, String> dictMap = An4Importer.importDictionary(dictionaryFile);
    dictionary = new SimpleDictionary(dictMap);
  }

}
