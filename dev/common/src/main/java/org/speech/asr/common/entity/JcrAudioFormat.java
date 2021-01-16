/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.common.entity;

import org.speech.asr.common.entity.BaseEntity;

import javax.media.Format;
import javax.media.format.AudioFormat;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 10, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class JcrAudioFormat extends BaseEntity {

  private String encoding;

  private Integer samplingRate;

  private Integer endian;

  private boolean signed;

  /**
   * In bits.
   */
  private Integer sampleSize;

  public AudioFormat toAudioFormat() {
    return new AudioFormat(encoding, samplingRate, sampleSize, 1,
        endian == 0 ? AudioFormat.LITTLE_ENDIAN : AudioFormat.BIG_ENDIAN,
        signed ? AudioFormat.SIGNED : AudioFormat.UNSIGNED, sampleSize, samplingRate, Format.byteArray);
  }

  /**
   * Getter for property 'encoding'.
   *
   * @return Value for property 'encoding'.
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * Setter for property 'encoding'.
   *
   * @param encoding Value to set for property 'encoding'.
   */
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  /**
   * Getter for property 'endian'.
   *
   * @return Value for property 'endian'.
   */
  public Integer getEndian() {
    return endian;
  }

  /**
   * Setter for property 'endian'.
   *
   * @param endian Value to set for property 'endian'.
   */
  public void setEndian(Integer endian) {
    this.endian = endian;
  }

  /**
   * Getter for property 'sampleSize'.
   *
   * @return Value for property 'sampleSize'.
   */
  public Integer getSampleSize() {
    return sampleSize;
  }

  /**
   * Setter for property 'sampleSize'.
   *
   * @param sampleSize Value to set for property 'sampleSize'.
   */
  public void setSampleSize(Integer sampleSize) {
    this.sampleSize = sampleSize;
  }

  /**
   * Getter for property 'samplingRate'.
   *
   * @return Value for property 'samplingRate'.
   */
  public Integer getSamplingRate() {
    return samplingRate;
  }

  /**
   * Setter for property 'samplingRate'.
   *
   * @param samplingRate Value to set for property 'samplingRate'.
   */
  public void setSamplingRate(Integer samplingRate) {
    this.samplingRate = samplingRate;
  }

  /**
   * Getter for property 'signed'.
   *
   * @return Value for property 'signed'.
   */
  public boolean isSigned() {
    return signed;
  }

  /**
   * Setter for property 'signed'.
   *
   * @param signed Value to set for property 'signed'.
   */
  public void setSigned(boolean signed) {
    this.signed = signed;
  }

  public String toString() {
    return "JcrAudioFormat{" +
        "encoding='" + encoding + '\'' +
        ", samplingRate=" + samplingRate +
        ", endian=" + endian +
        ", signed=" + signed +
        ", sampleSize=" + sampleSize +
        '}';
  }
}
