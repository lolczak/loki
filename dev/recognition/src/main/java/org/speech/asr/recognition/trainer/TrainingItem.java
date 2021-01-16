/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.acoustic.Hmm;
import org.speech.asr.recognition.acoustic.PhoneticUnit;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 19, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class TrainingItem {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(TrainingItem.class.getName());

  private Integer index;

  private List<PhoneticUnit> transcription;

  private Feature[] observationSequence;

  private Feature[] logObservationSequence;

  private Hmm sentenceHmm;

  /**
   * Getter for property 'sequenceHmm'.
   *
   * @return Value for property 'sequenceHmm'.
   */
  public Hmm getSentenceHmm() {
    return sentenceHmm;
  }

  /**
   * Setter for property 'sequenceHmm'.
   *
   * @param sentenceHmm Value to set for property 'sequenceHmm'.
   */
  public void setSentenceHmm(Hmm sentenceHmm) {
    this.sentenceHmm = sentenceHmm;
  }

  /**
   * Getter for property 'logObservationSequence'.
   *
   * @return Value for property 'logObservationSequence'.
   */
  public Feature[] getLogObservationSequence() {
    return logObservationSequence;
  }

  /**
   * Setter for property 'logObservationSequence'.
   *
   * @param logObservationSequence Value to set for property 'logObservationSequence'.
   */
  public void setLogObservationSequence(Feature[] logObservationSequence) {
    this.logObservationSequence = logObservationSequence;
  }

  /**
   * Getter for property 'observationSequence'.
   *
   * @return Value for property 'observationSequence'.
   */
  public Feature[] getObservationSequence() {
    return observationSequence;
  }

  /**
   * Setter for property 'observationSequence'.
   *
   * @param observationSequence Value to set for property 'observationSequence'.
   */
  public void setObservationSequence(Feature[] observationSequence) {
    this.observationSequence = observationSequence;
  }

  /**
   * Getter for property 'index'.
   *
   * @return Value for property 'index'.
   */
  public Integer getIndex() {
    return index;
  }

  /**
   * Setter for property 'index'.
   *
   * @param index Value to set for property 'index'.
   */
  public void setIndex(Integer index) {
    this.index = index;
  }

  /**
   * Getter for property 'transcription'.
   *
   * @return Value for property 'transcription'.
   */
  public List<PhoneticUnit> getTranscription() {
    return transcription;
  }

  /**
   * Setter for property 'transcription'.
   *
   * @param transcription Value to set for property 'transcription'.
   */
  public void setTranscription(List<PhoneticUnit> transcription) {
    this.transcription = transcription;
  }
}
