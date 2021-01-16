/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.launcher.an4.util;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 6, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class TranscribedUtterance {

  private String transcription;

  private byte[] utterance;

  private String relativePath;

  /**
   * Getter for property 'relativePath'.
   *
   * @return Value for property 'relativePath'.
   */
  public String getRelativePath() {
    return relativePath;
  }

  /**
   * Setter for property 'relativePath'.
   *
   * @param relativePath Value to set for property 'relativePath'.
   */
  public void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

  /**
   * Getter for property 'transcription'.
   *
   * @return Value for property 'transcription'.
   */
  public String getTranscription() {
    return transcription;
  }

  /**
   * Setter for property 'transcription'.
   *
   * @param transcription Value to set for property 'transcription'.
   */
  public void setTranscription(String transcription) {
    this.transcription = transcription;
  }

  /**
   * Getter for property 'utterance'.
   *
   * @return Value for property 'utterance'.
   */
  public byte[] getUtterance() {
    return utterance;
  }

  /**
   * Setter for property 'utterance'.
   *
   * @param utterance Value to set for property 'utterance'.
   */
  public void setUtterance(byte[] utterance) {
    this.utterance = utterance;
  }
}
