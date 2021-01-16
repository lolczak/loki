/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.cvt;

import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.acoustic.PhoneticUnit;
import org.speech.asr.recognition.acoustic.NeuralStateDescriptor;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 30, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SegmentedTrainingItem {

  private List<Feature> observationSequence;

  private List<NeuralStateDescriptor> transcription;

  private List<StateSegment> segmentation;

  /**
   * Getter for property 'frameAlignment'.
   *
   * @return Value for property 'frameAlignment'.
   */
  public List<StateSegment> getSegmentation() {
    return segmentation;
  }

  /**
   * Setter for property 'frameAlignment'.
   *
   * @param segmentation Value to set for property 'frameAlignment'.
   */
  public void setSegmentation(List<StateSegment> segmentation) {
    this.segmentation = segmentation;
  }

  /**
   * Getter for property 'observationSequence'.
   *
   * @return Value for property 'observationSequence'.
   */
  public List<Feature> getObservationSequence() {
    return observationSequence;
  }

  /**
   * Setter for property 'observationSequence'.
   *
   * @param observationSequence Value to set for property 'observationSequence'.
   */
  public void setObservationSequence(List<Feature> observationSequence) {
    this.observationSequence = observationSequence;
  }

  /**
   * Getter for property 'transcription'.
   *
   * @return Value for property 'transcription'.
   */
  public List<NeuralStateDescriptor> getTranscription() {
    return transcription;
  }

  /**
   * Setter for property 'transcription'.
   *
   * @param transcription Value to set for property 'transcription'.
   */
  public void setTranscription(List<NeuralStateDescriptor> transcription) {
    this.transcription = transcription;
  }
}
