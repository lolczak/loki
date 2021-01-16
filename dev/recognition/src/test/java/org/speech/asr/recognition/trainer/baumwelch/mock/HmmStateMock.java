/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.baumwelch.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.acoustic.HmmArc;
import org.speech.asr.recognition.acoustic.HmmState;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.math.MultivariateGaussian;
import org.speech.asr.recognition.math.Pdf;

import java.util.Collection;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 20, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class HmmStateMock implements HmmState {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(HmmStateMock.class.getName());

  private Pdf<Feature> pdf;

  public HmmStateMock(double mean, double deviation) {
    pdf = new MultivariateGaussian(AsrContext.getContext().getLogScale(), mean, deviation);
  }

  public String getId() {
    return null;  //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public double score(Feature observation) {
    return pdf.getValue(observation);
  }

  public Collection<HmmArc> getSuccessors() {
    return null;  //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isEmitting() {
    return false;  //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public void setScorer(Pdf<Feature> pdf) {
    this.pdf = pdf;
  }

  public Pdf<Feature> getScorer() {
    return pdf;
  }

  public void addSuccessor(HmmArc arc) {
    //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public void setSuccessors(Collection<HmmArc> arcs) {
    //TODO To change body of implemented methods use File | Settings | File Templates.
  }
}
