/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 30, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class TrainResult {

  private List<Double> errors;

  private int performedCycles;

  /**
   * Getter for property 'errors'.
   *
   * @return Value for property 'errors'.
   */
  public List<Double> getErrors() {
    return errors;
  }

  /**
   * Setter for property 'errors'.
   *
   * @param errors Value to set for property 'errors'.
   */
  public void setErrors(List<Double> errors) {
    this.errors = errors;
  }

  /**
   * Getter for property 'performedCycles'.
   *
   * @return Value for property 'performedCycles'.
   */
  public int getPerformedCycles() {
    return performedCycles;
  }

  /**
   * Setter for property 'performedCycles'.
   *
   * @param performedCycles Value to set for property 'performedCycles'.
   */
  public void setPerformedCycles(int performedCycles) {
    this.performedCycles = performedCycles;
  }

  public double getError() {
    return errors.get(errors.size() - 1);
  }
}
