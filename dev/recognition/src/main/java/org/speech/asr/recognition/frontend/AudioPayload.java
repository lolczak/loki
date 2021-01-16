/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.frontend;


import java.io.Serializable;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 5, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class AudioPayload implements Serializable {
  /**
   * Is last.
   */
  private boolean last = false;

  /**
   * In Hz.
   */
  private int samplingRate;

  /**
   * Offset of first chunk sample in real stream.
   */
  private long offset;

  /**
   * Timestamp of first chunk sample in real stream.
   */
  private long timestamp;

  /**
   * Samples.
   */
  private int[] samples;

  /**
   * Getter for property 'offset'.
   *
   * @return Value for property 'offset'.
   */
  public long getOffset() {
    return offset;
  }

  /**
   * Setter for property 'offset'.
   *
   * @param offset Value to set for property 'offset'.
   */
  public void setOffset(long offset) {
    this.offset = offset;
  }

  /**
   * Getter for property 'samples'.
   *
   * @return Value for property 'samples'.
   */
  public int[] getSamples() {
    return samples;
  }

  /**
   * Setter for property 'samples'.
   *
   * @param samples Value to set for property 'samples'.
   */
  public void setSamples(int[] samples) {
    this.samples = samples;
  }

  /**
   * Getter for property 'samplingRate'.
   *
   * @return Value for property 'samplingRate'.
   */
  public int getSamplingRate() {
    return samplingRate;
  }

  /**
   * Setter for property 'samplingRate'.
   *
   * @param samplingRate Value to set for property 'samplingRate'.
   */
  public void setSamplingRate(int samplingRate) {
    this.samplingRate = samplingRate;
  }

  /**
   * Getter for property 'timestamp'.
   *
   * @return Value for property 'timestamp'.
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Setter for property 'timestamp'.
   *
   * @param timestamp Value to set for property 'timestamp'.
   */
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Checks if is last.
   *
   * @return true, if is last
   */
  public boolean isLast() {
    return last;
  }

  /**
   * Setter for property \'last\'.
   *
   * @param last Value to set for property \'last\'.
   */
  public void setLast(boolean last) {
    this.last = last;
  }
}
