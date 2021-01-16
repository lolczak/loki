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
import org.speech.asr.recognition.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 7, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SimpleDictionary implements Dictionary {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SimpleDictionary.class.getName());

  private Map<String, Pronunciation> pronunciationMap;

  public SimpleDictionary(Map<String, String> dictMap) {
    pronunciationMap = new HashMap();
    for (Map.Entry<String, String> entry : dictMap.entrySet()) {
      Pronunciation pronunciation = new Pronunciation();
      pronunciation.setSpelling(entry.getKey());
      pronunciation.setPhonemes(StringUtils.parse(entry.getValue()));
      pronunciationMap.put(pronunciation.getSpelling(), pronunciation);
    }
  }

  public List<Pronunciation> getAllPronunciation() {
    return new LinkedList(pronunciationMap.values());
  }

  public Pronunciation getPronunciation(String spelling) {
    return pronunciationMap.get(spelling);
  }

}
