/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.acoustic;

import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.math.Pdf;

import java.io.Serializable;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 21, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class StateDescriptor implements State, Serializable {

  protected String id;

  protected Pdf<Feature> scorer;

  protected double logSelfLoopProbability;

  public StateDescriptor(String id, Pdf<Feature> acousticScorer, double logSelfLoopProbability) {
    this.id = id;
    this.scorer = acousticScorer;
    this.logSelfLoopProbability = logSelfLoopProbability;
  }

  /**
   * Getter for property 'id'.
   *
   * @return Value for property 'id'.
   */
  public String getId() {
    return id;
  }

  /**
   * Setter for property 'id'.
   *
   * @param id Value to set for property 'id'.
   */
  public void setId(String id) {
    this.id = id;
  }

  public double getSelfLoopProbability() {
    return AsrContext.getContext().getLogScale().logToLinear(logSelfLoopProbability);
  }

  /**
   * Getter for property 'selfLoopProbability'.
   *
   * @return Value for property 'selfLoopProbability'.
   */
  public double getLogSelfLoopProbability() {
    return logSelfLoopProbability;
  }

  /**
   * Setter for property 'selfLoopProbability'.
   *
   * @param logSelfLoopProbability Value to set for property 'selfLoopProbability'.
   */
  public void setLogSelfLoopProbability(double logSelfLoopProbability) {
    this.logSelfLoopProbability = logSelfLoopProbability;
  }

  /**
   * Getter for property 'pdf'.
   *
   * @return Value for property 'pdf'.
   */
  public Pdf<Feature> getScorer() {
    return scorer;
  }

  /**
   * Setter for property 'pdf'.
   *
   * @param scorer Value to set for property 'pdf'.
   */
  public void setScorer(Pdf<Feature> scorer) {
    this.scorer = scorer;
  }


  public String toString() {
    return "StateDescriptor{" +
        "id='" + id + '\'' +
        '}';
  }
}
