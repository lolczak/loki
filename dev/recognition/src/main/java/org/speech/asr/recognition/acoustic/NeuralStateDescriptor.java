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

import java.io.Serializable;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 1, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class NeuralStateDescriptor extends StateDescriptor implements Serializable {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(NeuralStateDescriptor.class.getName());

  protected int outputNumber;

  protected volatile double logPriorProbability;

  public NeuralStateDescriptor(String id, double logSelfLoopProbability, int noOutoput) {
    super(id, null,
        logSelfLoopProbability);
    this.outputNumber = noOutoput;
  }

  /**
   * Getter for property 'noOutput'.
   *
   * @return Value for property 'noOutput'.
   */
  public int getOutputNumber() {
    return outputNumber;
  }

  /**
   * Setter for property 'noOutput'.
   *
   * @param outputNumber Value to set for property 'noOutput'.
   */
  public void setOutputNumber(int outputNumber) {
    this.outputNumber = outputNumber;
  }

  public double getLogPriorProbability() {
    return logPriorProbability;
  }

  public void setLogPriorProbability(double logPriorProbability) {
    this.logPriorProbability = logPriorProbability;
  }
}
