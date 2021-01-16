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
import org.speech.asr.recognition.decoder.ConnectorSearchNode;
import org.speech.asr.recognition.decoder.HmmSearchNode;
import org.speech.asr.recognition.decoder.SearchGraphSegment;
import org.speech.asr.recognition.decoder.SearchNode;
import org.speech.asr.recognition.math.GaussianMixture;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.trainer.baumwelch.StateEstimates;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 15, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class GmmAcousticModel implements AcousticModel<StateDescriptor> {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(GmmAcousticModel.class.getName());

  private Map<String, PhoneticUnit<StateDescriptor>> phonemesMap;

  private Map<String, StateDescriptor> statesMap;

  private Map<String, EstimatesAccumulator> estimatesHolderMap;

  private int noMixtures;

  private int noDimensions;

  public GmmAcousticModel(int noDimensions, int noMixtures) {
    this.noDimensions = noDimensions;
    this.noMixtures = noMixtures;
    phonemesMap = new HashMap();
    statesMap = new HashMap();
    estimatesHolderMap = new ConcurrentHashMap();//HashMap();
  }

  public void addPhoneticUnit(PhoneticUnit<StateDescriptor> unit) {
    assert !phonemesMap.containsKey(unit.getName()) : "Value " + unit.getName();

    phonemesMap.put(unit.getName(), unit);
    for (StateDescriptor state : unit.getStatesSequence()) {
      assert !statesMap.containsKey(state.getId()) : "Values " + state.getId();
      statesMap.put(state.getId(), state);
      estimatesHolderMap.put(state.getId(), new EstimatesAccumulator(state.getId()));
    }
  }

  public void collectEstimates(Map<String, StateEstimates> estimatesMap) {
    for (Map.Entry<String, StateEstimates> entry : estimatesMap.entrySet()) {
      String stateId = entry.getKey();
      StateEstimates estimates = entry.getValue();
      EstimatesAccumulator holder = estimatesHolderMap.get(stateId);
      holder.collectStateEstimates(estimates);
      //todo test it below
      //state.setLogSelfLoopProbability(estimates.getLogSelfProbability());
    }
  }

  public void normalizeEstimates() {
    log.debug("Normalizing collected estimates of model parameters...");
    for (Map.Entry<String, StateDescriptor> entry : statesMap.entrySet()) {
      StateDescriptor state = entry.getValue();
      //todo refactor it
      EstimatesAccumulator holder = estimatesHolderMap.get(state.getId());
      GaussianMixture pdf = holder.getNormalizedGmm();
      assert pdf != null;
      state.setLogSelfLoopProbability(holder.getSelfLogProb());
      state.setScorer(pdf);
    }
  }

  public SearchGraphSegment getGraphSegment(String phonemeName) {
    LogScale logScale = AsrContext.getContext().getLogScale();
    PhoneticUnit<StateDescriptor> phoneme = getPhoneticUnit(phonemeName);
    SearchNode initialNode = new ConnectorSearchNode("start_phoneme_" + phonemeName);
    SearchNode finalNode = new ConnectorSearchNode("end_phoneme_" + phonemeName);
    double transitionScore = 0.0;
    SearchNode lastNode = initialNode;
    for (StateDescriptor state : phoneme.getStatesSequence()) {
      HmmSearchNode node = new HmmSearchNode(state);
      lastNode.addSuccessor(node, transitionScore);
      lastNode = node;
      transitionScore = logScale.subtractAsLinear(logScale.getLogOne(), state.getLogSelfLoopProbability());
    }
    lastNode.addSuccessor(finalNode, transitionScore);
    return new SearchGraphSegment(initialNode, finalNode);
  }

  public List<StateDescriptor> getAllStates() {
    return new LinkedList(statesMap.values());
  }

  public List<PhoneticUnit<StateDescriptor>> getPhoneSet() {
    return new LinkedList(phonemesMap.values());
  }

  public PhoneticUnit getPhoneticUnit(String name) {
    return phonemesMap.get(name);
  }

  public StateDescriptor getState(String id) {
    return statesMap.get(id);
  }


  /**
   * Getter for property 'noMixtures'.
   *
   * @return Value for property 'noMixtures'.
   */
  public int getNoMixtures() {
    return noMixtures;
  }

  /**
   * Getter for property 'noDimensions'.
   *
   * @return Value for property 'noDimensions'.
   */
  public int getNoDimensions() {
    return noDimensions;
  }
}
