/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 27, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class WordSearchNode extends ConnectorSearchNode {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(WordSearchNode.class.getName());

  private String word;

  public WordSearchNode(String nodeId, String word) {
    super(nodeId);
    this.word = word;
  }

  public String getWord() {
    return word;
  }

  public boolean isHypothesis() {
    return true;
  }

  public String toString() {
    return "WordSearchNode{" +
        "word='" + word + '\'' +
        '}';
  }
}
