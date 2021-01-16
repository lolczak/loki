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

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 26, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class Pronunciation {
  
  private String spelling;

  private List<String> phonemes;

  /**
   * Getter for property 'phonemes'.
   *
   * @return Value for property 'phonemes'.
   */
  public List<String> getPhonemes() {
    return phonemes;
  }

  /**
   * Setter for property 'phonemes'.
   *
   * @param phonemes Value to set for property 'phonemes'.
   */
  public void setPhonemes(List<String> phonemes) {
    this.phonemes = phonemes;
  }

  /**
   * Getter for property 'spelling'.
   *
   * @return Value for property 'spelling'.
   */
  public String getSpelling() {
    return spelling;
  }

  /**
   * Setter for property 'spelling'.
   *
   * @param spelling Value to set for property 'spelling'.
   */
  public void setSpelling(String spelling) {
    this.spelling = spelling;
  }
}
