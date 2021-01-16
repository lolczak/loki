/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 27, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ResultImpl implements Result {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ResultImpl.class.getName());

  private List<FrameAlignment> frameAlignments;

  private double score;

  private String utterance;

  public ResultImpl(String utterance, double score, List<FrameAlignment> frameAlignments) {
    this.utterance = utterance;
    this.score = score;
    this.frameAlignments = frameAlignments;
  }

  public List<FrameAlignment> getFrameAlignments() {
    return frameAlignments;
  }

  public double getScore() {
    return score;
  }

  public String getUtterance() {
    return utterance;
  }

  public String toString() {
    return "ResultImpl{" +
        "score=" + score +
        ", utterance='" + utterance + '\'' +
        '}';
  }
}
