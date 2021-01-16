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
 * Creation date: Jun 20, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class FeatureImpl implements Feature, Serializable {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(FeatureImpl.class.getName());

  private double[] data;

  private long sequenceNumber;

  public FeatureImpl(double[] vals) {
    data = vals;
  }

  public FeatureImpl(double val) {
    data = new double[1];
    data[0] = val;
  }

  public long getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(long sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public double[] getData() {
    return data;
  }


  public String toString() {
    return "FeatureImpl{" +
        "data=" + data +
        ", sequenceNumber=" + sequenceNumber +
        '}';
  }
}
