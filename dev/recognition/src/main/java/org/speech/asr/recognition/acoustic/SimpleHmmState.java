/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.acoustic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.Pdf;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 21, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SimpleHmmState implements LeftRightHmmState {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SimpleHmmState.class.getName());

  private String id;

  private Pdf<Feature> pdf;

  private boolean isEmitting;

  private double selfLoopProbability;

  private List<HmmArc> successors;

  public SimpleHmmState(String id, boolean isEmitting) {
    this.id = id;
    this.isEmitting = isEmitting;
    successors = new LinkedList();
  }

  /**
   * Konstruuje stan typu left-rigth.
   *
   * @param id
   * @param selfLoopProbability
   */
  public SimpleHmmState(String id, double selfLoopProbability) {
    this(id, true);
    this.selfLoopProbability = selfLoopProbability;
    successors.add(createSelfLoop());
  }

  private HmmArc createSelfLoop() {
    HmmArc selfArc = new HmmArc();
    selfArc.setNextState(this);
    selfArc.setTransitionProbability(selfLoopProbability);
    return selfArc;
  }

  public String getId() {
    return id;
  }

  public double score(Feature observation) {
    return pdf.getValue(observation);
  }

  public Collection<HmmArc> getSuccessors() {
    return successors;
  }

  public void setNext(HmmState next) {
    successors.clear();
    if (isEmitting) {
      successors.add(createSelfLoop());
    }
    HmmArc nextArc = new HmmArc();
    nextArc.setNextState(next);
    LogScale logScale = AsrContext.getContext().getLogScale();
    nextArc.setTransitionProbability(logScale.subtractAsLinear(logScale.getLogOne(), selfLoopProbability));
    successors.add(nextArc);
  }

  public HmmArc getSelfArc() {
    for (HmmArc arc : successors) {
      if (arc.getNextState() == this) {
        return arc;
      }
    }
    return null;
  }

  public void updateSelfProbability() {
    selfLoopProbability = getSelfArc().getTransitionProbability();
  }

  public void setScorer(Pdf<Feature> pdf) {
    this.pdf = pdf;
  }

  public Pdf<Feature> getScorer() {
    return pdf;
  }

  public boolean isEmitting() {
    return isEmitting;
  }

  public double getSelfLoopProbability() {
    return selfLoopProbability;
  }

  public void setSelfLoopProbability(double prob) {
    this.selfLoopProbability = prob;
  }
}
