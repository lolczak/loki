/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.baumwelch.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.linguist.Dictionary;
import org.speech.asr.recognition.linguist.Pronunciation;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 28, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class DictionaryMock implements Dictionary {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DictionaryMock.class.getName());

  public List<Pronunciation> getAllPronunciation() {
    return null;  //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public Pronunciation getPronunciation(String spelling) {
    Pronunciation pronunciation = new Pronunciation();
    pronunciation.setSpelling(spelling);
    StringTokenizer st = new StringTokenizer(spelling, " ", false);
    List<String> phonemes = new LinkedList();
    while (st.hasMoreTokens()) {
      phonemes.add(st.nextToken());
    }
    pronunciation.setPhonemes(phonemes);
    return pronunciation;
  }
}
