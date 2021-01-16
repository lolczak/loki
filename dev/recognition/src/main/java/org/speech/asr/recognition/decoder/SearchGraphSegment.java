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
public class SearchGraphSegment {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SearchGraphSegment.class.getName());

  private SearchNode initialNode;

  private SearchNode finalNode;

  public SearchGraphSegment(SearchNode initialNode, SearchNode finalNode) {
    this.initialNode = initialNode;
    this.finalNode = finalNode;
  }

  /**
   * Getter for property 'finalNode'.
   *
   * @return Value for property 'finalNode'.
   */
  public SearchNode getFinalNode() {
    return finalNode;
  }

  /**
   * Getter for property 'initialNode'.
   *
   * @return Value for property 'initialNode'.
   */
  public SearchNode getInitialNode() {
    return initialNode;
  }

}
