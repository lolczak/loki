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
import org.speech.asr.recognition.ann.NeuralNetwork;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.decoder.ConnectorSearchNode;
import org.speech.asr.recognition.decoder.HmmSearchNode;
import org.speech.asr.recognition.decoder.SearchGraphSegment;
import org.speech.asr.recognition.decoder.SearchNode;
import org.speech.asr.recognition.frontend.InputMapper;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.MathUtils;
import org.speech.asr.recognition.math.Pdf;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 31, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class NeuralHybridAcousticModel implements AcousticModel<NeuralStateDescriptor>, Serializable {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(NeuralHybridAcousticModel.class.getName());

  private static ThreadLocal<Long> lastSequenceNumber = new ThreadLocal();

  private static ThreadLocal<double[]> lastOutput = new ThreadLocal();

  private NeuralNetwork neuralNetwork;

  private Map<String, PhoneticUnit> phonemesMap;

  private Map<String, NeuralStateDescriptor> statesMap;

  /**
   * Mappingi sa indeksowane od 0.
   */
  private Map<Integer, NeuralStateDescriptor> outputMapping;

//  private long lastSequenceNumber = -1;
//
//  private double[] lastOutput;

  private InputMapper inputMapper;

  public NeuralHybridAcousticModel(NeuralNetwork neuralNetwork, InputMapper inputMapper) {
    this.neuralNetwork = neuralNetwork;
    this.inputMapper = inputMapper;
    phonemesMap = new ConcurrentHashMap();
    statesMap = new ConcurrentHashMap();
    outputMapping = new ConcurrentHashMap();
  }

  public void addPhoneticUnit(PhoneticUnit<NeuralStateDescriptor> unit) {
    assert !phonemesMap.containsKey(unit.getName()) : "Value " + unit.getName();

    phonemesMap.put(unit.getName(), unit);
    for (NeuralStateDescriptor state : unit.getStatesSequence()) {
      assert !statesMap.containsKey(state.getId()) : "Value " + state.getId();
      state.setScorer(new NeuralScorer(state));
      statesMap.put(state.getId(), state);
      outputMapping.put(state.getOutputNumber(), state);
    }
  }

  public SearchGraphSegment getGraphSegment(String phonemeName) {
    LogScale logScale = AsrContext.getContext().getLogScale();
    PhoneticUnit<NeuralStateDescriptor> phoneme = getPhoneticUnit(phonemeName);
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

  public NeuralStateDescriptor getState(String id) {
    return statesMap.get(id);
  }

  public NeuralStateDescriptor getStateForOutput(int index) {
    return outputMapping.get(index);
  }

  public PhoneticUnit<NeuralStateDescriptor> getPhoneticUnit(String name) {
    return phonemesMap.get(name);
  }

  public List<PhoneticUnit<NeuralStateDescriptor>> getPhoneSet() {
    return new LinkedList(phonemesMap.values());
  }

  public List<NeuralStateDescriptor> getAllStates() {
    return new LinkedList(statesMap.values());
  }

  /**
   * Getter for property 'neuralNetwork'.
   *
   * @return Value for property 'neuralNetwork'.
   */
  public NeuralNetwork getNeuralNetwork() {
    return neuralNetwork;
  }

  private class NeuralScorer implements Pdf<Feature>, Serializable {

    private int outputNumber;

    private NeuralStateDescriptor state;

    public NeuralScorer(NeuralStateDescriptor state) {
      this.outputNumber = state.getOutputNumber();
      this.state = state;
    }

    public double getValue(Feature randomVariable) {
      Long lastSeqNum = lastSequenceNumber.get() == null ? -1 : lastSequenceNumber.get();
      if (randomVariable.getSequenceNumber() != lastSeqNum) {
        double[] output = neuralNetwork.forward(inputMapper.mapInput(randomVariable.getData()));
        lastSequenceNumber.set(randomVariable.getSequenceNumber());
        lastOutput.set(output);
      }
      double logPosteriori = AsrContext.getContext().getLogScale().linearToLog(lastOutput.get()[outputNumber]);
      double logPrior = state.getLogPriorProbability();
      assert MathUtils.isReal(logPosteriori) : "Value " + logPosteriori;
      assert MathUtils.isReal(logPrior) : "Value " + logPrior;
      assert logPosteriori > -Double.MAX_VALUE;
      return logPosteriori - logPrior;
    }
  }
}
