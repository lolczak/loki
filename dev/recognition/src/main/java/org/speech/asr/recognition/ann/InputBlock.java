/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann;

import java.io.Serializable;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 30, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class InputBlock implements Serializable {

  private String id;

  private double[] input;

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

  /**
   * Getter for property 'input'.
   *
   * @return Value for property 'input'.
   */
  public double[] getInput() {
    return input;
  }

  /**
   * Setter for property 'input'.
   *
   * @param input Value to set for property 'input'.
   */
  public void setInput(double[] input) {
    this.input = input;
  }
}
