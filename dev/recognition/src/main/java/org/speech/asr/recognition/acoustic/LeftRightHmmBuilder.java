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
import org.speech.asr.recognition.math.Pdf;

import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 21, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class LeftRightHmmBuilder {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(LeftRightHmmBuilder.class.getName());

  private LeftRightHmmState initialState;

  private LeftRightHmmState finalState;

  private List<HmmState> states;

  public LeftRightHmmBuilder() {
    states = new LinkedList();
  }

  public void addState(StateDescriptor state) {
    addState(state.getId(), state.getScorer(), state.getLogSelfLoopProbability());
  }

  public void addState(String id, Pdf<Feature> pdf, double selfLoopProb) {
    SimpleHmmState newState = new SimpleHmmState(id, selfLoopProb);
    newState.setScorer(pdf);
    addState(newState);
  }

  public void addState(LeftRightHmmState state) {
    if (initialState == null) {
      initialState = finalState = state;
    } else {
      finalState.setNext(state);
      finalState = state;
    }
    states.add(state);
  }

  public void addHmm(Hmm hmm) {
    if (initialState == null) {
      initialState = (LeftRightHmmState) hmm.getInitialState();
      finalState = (LeftRightHmmState) hmm.getFinalState();
    } else {
      finalState.setNext(hmm.getInitialState());
      finalState = (LeftRightHmmState) hmm.getFinalState();
    }
    states.addAll(hmm.getAllStates());
  }

  public Hmm getHmm() {
    HmmImpl hmm = new HmmImpl(initialState, finalState, states);
    return hmm;
  }
}
