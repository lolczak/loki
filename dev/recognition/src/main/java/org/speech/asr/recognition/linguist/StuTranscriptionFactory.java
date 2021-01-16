/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.linguist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.recognition.acoustic.AcousticModel;
import org.speech.asr.recognition.acoustic.PhoneticUnit;
import org.speech.asr.recognition.util.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Sep 1, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class StuTranscriptionFactory implements TranscriptionFactory {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(StuTranscriptionFactory.class.getName());

  private static final String DEFAULT_START_SIL = "<sil>";

  private static final String DEFAULT_STOP_SIL = "</sil>";

  private Dictionary dictionary;

  private AcousticModel acousticModel;

  private String startSilSymbol;

  private String stopSilSymbol;

  public StuTranscriptionFactory(AcousticModel model, Dictionary dictionary) {
    this.dictionary = dictionary;
    this.acousticModel = model;
    this.startSilSymbol = DEFAULT_START_SIL;
    this.stopSilSymbol = DEFAULT_STOP_SIL;
  }

  public List<PhoneticUnit> createTranscription(String txt) {
    List<String> words = StringUtils.parse(txt);
    PhoneticUnit startSil = acousticModel.getPhoneticUnit(startSilSymbol);
    List<PhoneticUnit> result = new LinkedList();
    result.add(startSil);
    PhoneticUnit lastPhoneme = startSil;
    for (String word : words) {
      Pronunciation pronunciation = dictionary.getPronunciation(word);
      for (String phoneme : pronunciation.getPhonemes()) {
        PhoneticUnit actualPhoneme = acousticModel.getPhoneticUnit(phoneme);
        if (actualPhoneme == null) {
          throw new AsrRuntimeException("There is no unit " + phoneme + " in model in txt " + txt);
        }
        String transitionSymbol = lastPhoneme.getName() + "-" + actualPhoneme.getName();
        PhoneticUnit transitionPart = acousticModel.getPhoneticUnit(transitionSymbol);
        if (transitionPart == null) {
          throw new AsrRuntimeException("There is no unit " + transitionSymbol + " in model in txt " + txt);
        }
        result.add(transitionPart);
        result.add(actualPhoneme);
        lastPhoneme = actualPhoneme;
      }
    }
    PhoneticUnit stopSil = acousticModel.getPhoneticUnit(stopSilSymbol);
    String transitionSymbol = lastPhoneme.getName() + "-" + stopSil.getName();
    PhoneticUnit transitionPart = acousticModel.getPhoneticUnit(transitionSymbol);
    result.add(transitionPart);
    result.add(stopSil);
    return result;
  }

  public void setStartSilenceSymbol(String symbol) {
    this.startSilSymbol = symbol;
  }

  public void setStopSilenceSymbol(String symbol) {
    this.stopSilSymbol = symbol;
  }
}
