/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 30, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class TrainParameters {

  private double learningRate;

  private double momentum;

  private double randomizeAmplitude;

  private boolean randomize;

  private double maxRmse;

  private int noCycles;

  private LearningMode learningMode;

  private int batchSize;

  /**
   * Getter for property 'batchSize'.
   *
   * @return Value for property 'batchSize'.
   */
  public int getBatchSize() {
    return batchSize;
  }

  /**
   * Setter for property 'batchSize'.
   *
   * @param batchSize Value to set for property 'batchSize'.
   */
  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  /**
   * Getter for property 'learningMode'.
   *
   * @return Value for property 'learningMode'.
   */
  public LearningMode getLearningMode() {
    return learningMode;
  }

  /**
   * Setter for property 'learningMode'.
   *
   * @param learningMode Value to set for property 'learningMode'.
   */
  public void setLearningMode(LearningMode learningMode) {
    this.learningMode = learningMode;
  }

  /**
   * Getter for property 'maxRmse'.
   *
   * @return Value for property 'maxRmse'.
   */
  public double getMaxRmse() {
    return maxRmse;
  }

  /**
   * Setter for property 'maxRmse'.
   *
   * @param maxRmse Value to set for property 'maxRmse'.
   */
  public void setMaxRmse(double maxRmse) {
    this.maxRmse = maxRmse;
  }

  /**
   * Getter for property 'randomize'.
   *
   * @return Value for property 'randomize'.
   */
  public boolean isRandomize() {
    return randomize;
  }

  /**
   * Setter for property 'randomize'.
   *
   * @param randomize Value to set for property 'randomize'.
   */
  public void setRandomize(boolean randomize) {
    this.randomize = randomize;
  }

  /**
   * Getter for property 'learningRate'.
   *
   * @return Value for property 'learningRate'.
   */
  public double getLearningRate() {
    return learningRate;
  }

  /**
   * Setter for property 'learningRate'.
   *
   * @param learningRate Value to set for property 'learningRate'.
   */
  public void setLearningRate(double learningRate) {
    this.learningRate = learningRate;
  }

  /**
   * Getter for property 'momentum'.
   *
   * @return Value for property 'momentum'.
   */
  public double getMomentum() {
    return momentum;
  }

  /**
   * Setter for property 'momentum'.
   *
   * @param momentum Value to set for property 'momentum'.
   */
  public void setMomentum(double momentum) {
    this.momentum = momentum;
  }

  /**
   * Getter for property 'noCycles'.
   *
   * @return Value for property 'noCycles'.
   */
  public int getNoCycles() {
    return noCycles;
  }

  /**
   * Setter for property 'noCycles'.
   *
   * @param noCycles Value to set for property 'noCycles'.
   */
  public void setNoCycles(int noCycles) {
    this.noCycles = noCycles;
  }

  /**
   * Getter for property 'randomizeAmplitude'.
   *
   * @return Value for property 'randomizeAmplitude'.
   */
  public double getRandomizeAmplitude() {
    return randomizeAmplitude;
  }

  /**
   * Setter for property 'randomizeAmplitude'.
   *
   * @param randomizeAmplitude Value to set for property 'randomizeAmplitude'.
   */
  public void setRandomizeAmplitude(double randomizeAmplitude) {
    this.randomizeAmplitude = randomizeAmplitude;
  }
}
