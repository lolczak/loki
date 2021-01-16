/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.decoder;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 26, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class FrameAlignment {

  private long sequenceNumber;

  private String stateId;

  public FrameAlignment(String stateId, long sequenceNumber) {
    this.stateId = stateId;
    this.sequenceNumber = sequenceNumber;
  }

  /**
   * Getter for property 'sequenceNumber'.
   *
   * @return Value for property 'sequenceNumber'.
   */
  public long getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * Setter for property 'sequenceNumber'.
   *
   * @param sequenceNumber Value to set for property 'sequenceNumber'.
   */
  public void setSequenceNumber(long sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  /**
   * Getter for property 'stateId'.
   *
   * @return Value for property 'stateId'.
   */
  public String getStateId() {
    return stateId;
  }

  /**
   * Setter for property 'stateId'.
   *
   * @param stateId Value to set for property 'stateId'.
   */
  public void setStateId(String stateId) {
    this.stateId = stateId;
  }


  public String toString() {
    return "FrameAlignment{" +
        "sequenceNumber=" + sequenceNumber +
        ", stateId='" + stateId + '\'' +
        '}';
  }
}
