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
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.acoustic.StateDescriptor;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 26, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class HmmSearchNode extends AbstractSearchNode {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(HmmSearchNode.class.getName());

  private StateDescriptor state;

  public HmmSearchNode(StateDescriptor state) {
    this.state = state;
    addSelfLoop(state.getLogSelfLoopProbability());
  }

  public String getNodeId() {
    return state.getId();
  }

  public String getWord() {
    return null;
  }

  public boolean isHypothesis() {
    return false;
  }

  public boolean isEmitting() {
    return true;
  }

  public double score(Feature feature) {
    return state.getScorer().getValue(feature);
  }

  public double getSelfLoopProbability() {
    return state.getLogSelfLoopProbability();
  }


  public String toString() {
    return "HmmSearchNode{" +
        "state=" + state +
        '}';
  }
}
