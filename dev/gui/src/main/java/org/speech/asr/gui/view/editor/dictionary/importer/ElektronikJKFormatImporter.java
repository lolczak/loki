/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.view.editor.dictionary.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.common.entity.Word;
import org.speech.asr.gui.view.editor.dictionary.importer.DictionaryImporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 10, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ElektronikJKFormatImporter implements DictionaryImporter {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ElektronikJKFormatImporter.class.getName());

  private String separator = "=";

  private String leftWrapper = "[";

  private String rightWrapper = "]";

  public List<Word> importDictionary(InputStream in) {
    InputStreamReader wrapper = new InputStreamReader(in);
    BufferedReader reader = new BufferedReader(wrapper);
    String line = null;
    List<Word> words = new LinkedList();

    try {
      while ((line = reader.readLine()) != null) {
        int separatorIndex = line.indexOf(separator);
        int leftIndex = line.indexOf(leftWrapper);
        int rightIndex = line.indexOf(rightWrapper);
        if (matches(separatorIndex, leftIndex, rightIndex)) {
          String graphemes = line.substring(0, separatorIndex);
          String phonemes = line.substring(leftIndex + 1, rightIndex);
          Word word = new Word();
          word.setGraphemes(graphemes);
          word.setPhonemes(phonemes);
          words.add(word);
        }
      }
    } catch (IOException e) {
      throw new AsrRuntimeException(e);
    }

    return words;
  }

  private boolean matches(int separatorIndex, int leftIndex, int rightIndex) {
    return separatorIndex >= 0 && leftIndex >= 0 && rightIndex >= 0 && separatorIndex < leftIndex &&
        leftIndex < rightIndex;
  }
}
