/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.math.LogScale;

import java.io.File;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 28, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class AsrContext {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(AsrContext.class.getName());

  private LogScale logScale;

  private boolean isTransitionScored;

  private boolean findFrameAlignment;

  private Double deviationFloor;

  private File workDir;

  private static AsrContext instance;

  public static AsrContext getContext() {
    return instance;
  }

  public static void setContext(AsrContext context) {
    instance = context;
  }

  /**
   * Getter for property 'logScale'.
   *
   * @return Value for property 'logScale'.
   */
  public LogScale getLogScale() {
    return logScale;
  }

  /**
   * Setter for property 'logScale'.
   *
   * @param logScale Value to set for property 'logScale'.
   */
  public void setLogScale(LogScale logScale) {
    this.logScale = logScale;
  }

  /**
   * Getter for property 'useTransitionProbability'.
   *
   * @return Value for property 'useTransitionProbability'.
   */
  public boolean isTransitionScored() {
    return isTransitionScored;
  }

  /**
   * Setter for property 'useTransitionProbability'.
   *
   * @param transitionScored Value to set for property 'useTransitionProbability'.
   */
  public void setTransitionScored(boolean transitionScored) {
    this.isTransitionScored = transitionScored;
  }

  /**
   * Getter for property 'findFrameAlignment'.
   *
   * @return Value for property 'findFrameAlignment'.
   */
  public boolean isFindFrameAlignment() {
    return findFrameAlignment;
  }

  /**
   * Setter for property 'findFrameAlignment'.
   *
   * @param findFrameAlignment Value to set for property 'findFrameAlignment'.
   */
  public void setFindFrameAlignment(boolean findFrameAlignment) {
    this.findFrameAlignment = findFrameAlignment;
  }

  /**
   * Getter for property 'deviationFloor'.
   *
   * @return Value for property 'deviationFloor'.
   */
  public Double getDeviationFloor() {
    return deviationFloor;
  }

  /**
   * Setter for property 'deviationFloor'.
   *
   * @param deviationFloor Value to set for property 'deviationFloor'.
   */
  public void setDeviationFloor(Double deviationFloor) {
    this.deviationFloor = deviationFloor;
  }

  /**
   * Getter for property 'workDir'.
   *
   * @return Value for property 'workDir'.
   */
  public File getWorkDir() {
    return workDir;
  }

  /**
   * Setter for property 'workDir'.
   *
   * @param workDir Value to set for property 'workDir'.
   */
  public void setWorkDir(File workDir) {
    this.workDir = workDir;
  }
}
