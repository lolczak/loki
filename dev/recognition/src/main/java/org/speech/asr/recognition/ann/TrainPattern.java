/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann;

import java.util.LinkedList;
import java.util.List;
import java.io.Serializable;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 30, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class TrainPattern implements Serializable {

  private double[] desiredOutput;

  private List<InputBlock> inputs;

  public TrainPattern(List<InputBlock> inputs, double[] desiredOutput) {
    this.inputs = inputs;
    this.desiredOutput = desiredOutput;
  }

  public TrainPattern(double[] input, double[] desiredOutput) {
    this.desiredOutput = desiredOutput;
    InputBlock defBlock = new InputBlock();
    defBlock.setId("_default_");
    defBlock.setInput(input);
    inputs = new LinkedList();
    inputs.add(defBlock);
  }

  /**
   * Getter for property 'desiredOutput'.
   *
   * @return Value for property 'desiredOutput'.
   */
  public double[] getDesiredOutput() {
    return desiredOutput;
  }

  /**
   * Setter for property 'desiredOutput'.
   *
   * @param desiredOutput Value to set for property 'desiredOutput'.
   */
  public void setDesiredOutput(double[] desiredOutput) {
    this.desiredOutput = desiredOutput;
  }

  /**
   * Getter for property 'inputs'.
   *
   * @return Value for property 'inputs'.
   */
  public List<InputBlock> getInputs() {
    return inputs;
  }

  /**
   * Setter for property 'inputs'.
   *
   * @param inputs Value to set for property 'inputs'.
   */
  public void setInputs(List<InputBlock> inputs) {
    this.inputs = inputs;
  }
}
