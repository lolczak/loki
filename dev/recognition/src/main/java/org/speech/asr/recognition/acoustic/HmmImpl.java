/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.acoustic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 21, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class HmmImpl implements Hmm {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(HmmImpl.class.getName());

  private HmmState initialState;

  private HmmState finalState;

  private Collection<HmmState> states;

  public HmmImpl(HmmState initialState, HmmState finalState, Collection<HmmState> states) {
    this.initialState = initialState;
    this.finalState = finalState;
    this.states = states;
  }

  public Collection<HmmState> getAllStates() {
    return states;
  }

  public HmmState getFinalState() {
    return finalState;
  }

  public HmmState getInitialState() {
    return initialState;
  }

}
