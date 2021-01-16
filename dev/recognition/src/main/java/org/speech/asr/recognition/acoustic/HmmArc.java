/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.acoustic;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 19, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class HmmArc {

  private HmmState nextState;

  private double transitionProbability;

  /**
   * Getter for property 'nextState'.
   *
   * @return Value for property 'nextState'.
   */
  public HmmState getNextState() {
    return nextState;
  }

  /**
   * Setter for property 'nextState'.
   *
   * @param nextState Value to set for property 'nextState'.
   */
  public void setNextState(HmmState nextState) {
    this.nextState = nextState;
  }

  /**
   * Getter for property 'transitionProbability'.
   *
   * @return Value for property 'transitionProbability'.
   */
  public double getTransitionProbability() {
    return transitionProbability;
  }

  /**
   * Setter for property 'transitionProbability'.
   *
   * @param transitionProbability Value to set for property 'transitionProbability'.
   */
  public void setTransitionProbability(double transitionProbability) {
    this.transitionProbability = transitionProbability;
  }
}


