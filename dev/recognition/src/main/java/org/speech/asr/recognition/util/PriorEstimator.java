/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.recognition.launcher.an4.util.TranscribedUtterance;
import org.speech.asr.recognition.linguist.Dictionary;
import org.speech.asr.recognition.linguist.Pronunciation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 31, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class PriorEstimator {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(PriorEstimator.class.getName());

  private int noSamples;

  private Map<String, Integer> priorMap;

  public PriorEstimator() {
    noSamples = 0;
    priorMap = new HashMap();
  }

  public double getPrior(String token) {
    Integer count = priorMap.get(token);
    if (count == null) {
      throw new AsrRuntimeException("There is no estimates for " + token);
    }
    return count / (double) noSamples;
  }

  public int getCount(String token) {
    Integer count = priorMap.get(token);
    if (count == null) {
      throw new AsrRuntimeException("There is no estimates for " + token);
    }
    return count;
  }

  public List<String> getTokens() {
    return new LinkedList(priorMap.keySet());
  }

  public void estimate(List<TranscribedUtterance> set, Dictionary dictionary) {
    for (TranscribedUtterance item : set) {
      List<String> phonemes = getPhonemes(item.getTranscription(), dictionary);
      for (String phoneme : phonemes) {
        noSamples++;
        Integer count = priorMap.get(phoneme);
        if (count == null) {
          priorMap.put(phoneme, 1);
        } else {
          priorMap.put(phoneme, count + 1);
        }
      }
    }
  }

  private List<String> getPhonemes(String txt, Dictionary dictionary) {
    List<String> words = StringUtils.parse(txt);
    List<String> phonemes = new LinkedList();
    phonemes.add("<sil>");
    phonemes.add("</sil>");
    for (String word : words) {
      Pronunciation pronunciation = dictionary.getPronunciation(word);
      for (String phoneme : pronunciation.getPhonemes()) {
        phonemes.add(phoneme);
      }
    }
    return phonemes;
  }

  public int getNoSamples() {
    return noSamples;
  }
}
