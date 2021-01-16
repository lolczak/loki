/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann;

import org.joone.engine.*;
import org.joone.engine.learning.TeachingSynapse;
import org.joone.io.MemoryInputSynapse;
import org.joone.io.MemoryOutputSynapse;
import org.joone.net.NeuralNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.util.NeuralNetUtils;

import java.io.Serializable;
import java.util.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 30, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class JooneNeuralNetAdapter implements NeuralNetwork, NeuralNetListener {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(JooneNeuralNetAdapter.class.getName());

  private NeuralNet neuralNet;

  private boolean isTrained;

  private Object synchronizer = new Object();

  private TrainParameters trainParameters;

  public JooneNeuralNetAdapter(NeuralNet nnet, boolean isTrained) {
    this.neuralNet = nnet;
    this.isTrained = isTrained;
  }

  public Serializable getMemento() {
    return neuralNet;
  }

  public double[] forward(Collection<InputBlock> inputs) {
    if (!isTrained) {
      throw new IllegalStateException("Neural net is not trained");
    }
    Monitor monitor = neuralNet.getMonitor();
    for (InputBlock inputBlock : inputs) {
      bindLayer(inputBlock);
    }

    MemoryOutputSynapse outSynapse = new MemoryOutputSynapse();
    Layer output = neuralNet.getOutputLayer();
    output.removeAllOutputs();
    output.addOutputSynapse(outSynapse);
    monitor.removeAllListeners();
    monitor.addNeuralNetListener(this);
    monitor.setTotCicles(1);
    monitor.setTrainingPatterns(1);
    monitor.setLearning(false);

    synchronized (synchronizer) {
      neuralNet.go();
      try {
        synchronizer.wait();
      } catch (InterruptedException e) {
        //todo zaimplementowac z petla czekajaca na koniec
        log.error("", e);
      }
    }
    monitor.removeNeuralNetListener(this);
    log.trace("Net forward alg. finished");
    Vector<Pattern> outVector = outSynapse.getAllPatterns();
    assert outVector.size() == 1 : "Value " + outVector.size();
    double[] outActivation = outVector.get(0).getArray();
    return outActivation;
  }

  public double[] forward(double[] inputArray) {
    if (!isTrained) {
      throw new IllegalStateException("Neural net is not trained");
    }
    InputBlock defBlock = new InputBlock();
    defBlock.setId("input");
    defBlock.setInput(inputArray);
    List<InputBlock> inputs = new LinkedList();
    inputs.add(defBlock);

    return forward(inputs);
  }

  public boolean isTrained() {
    return isTrained;
  }

  public TrainResult train(List<TrainPattern> trainingPatterns) {
    TrainParameters defaultParams = new TrainParameters();
    defaultParams.setLearningRate(0.8);
    defaultParams.setMomentum(0.3);
    defaultParams.setRandomizeAmplitude(0.5);
    defaultParams.setNoCycles(4000);
    defaultParams.setRandomize(true);
    return train(trainingPatterns, defaultParams);
  }

  public TrainResult train(List<TrainPattern> trainingPatterns, TrainParameters parameters) {
    trainParameters = parameters;
    Map<String, double[][]> inputMap = NeuralNetUtils.createInputArrayMap(trainingPatterns);
    double[][] outputArray = NeuralNetUtils.createOutputArray(trainingPatterns);
    if (inputMap.size() == 1) {
      double[][] inputArray = inputMap.values().iterator().next();
      bindLayer("input", inputArray);
    } else {
      for (Map.Entry<String, double[][]> entry : inputMap.entrySet()) {
        bindLayer(entry.getKey(), entry.getValue());
      }
    }
    TeachingSynapse trainer = new TeachingSynapse();
    MemoryInputSynapse memSamples = new MemoryInputSynapse();
    memSamples.setInputArray(outputArray);
    memSamples.setAdvancedColumnSelector(generateSequence(outputArray[0].length));

    trainer.setDesired(memSamples);

    MemoryOutputSynapse error = new MemoryOutputSynapse();
    trainer.addResultSynapse(error);
    Layer output = neuralNet.getOutputLayer();
    output.removeAllOutputs();
    output.addOutputSynapse(trainer);
    neuralNet.setTeacher(trainer);

    Monitor monitor = neuralNet.getMonitor();
    monitor.removeAllListeners();
    monitor.setLearningRate(parameters.getLearningRate());
    monitor.setMomentum(parameters.getMomentum());
    monitor.addNeuralNetListener(this);
    monitor.setTrainingPatterns(trainingPatterns.size());
    monitor.setTotCicles(parameters.getNoCycles());
    monitor.addLearner(0, "org.joone.engine.BasicLearner");
    monitor.addLearner(1, "org.joone.engine.BatchLearner");
//    monitor.addLearner(2, "org.joone.engine.RpropLearner");
//    monitor.setBatchSize(1);
    if (parameters.getLearningMode() == null || parameters.getLearningMode() == LearningMode.ONLINE) {
      monitor.setLearningMode(0);
    } else {
      monitor.setLearningMode(1);
      monitor.setBatchSize(parameters.getBatchSize());
    }
    monitor.setLearning(true);
    if (parameters.isRandomize()) {
      neuralNet.randomize(parameters.getRandomizeAmplitude());
    }
    synchronized (synchronizer) {
      neuralNet.go();
      try {
        synchronizer.wait();
      } catch (InterruptedException e) {
        //todo zaimplementowac z petla czekajaca na koniec
        log.error("", e);
      }
    }
    monitor.removeNeuralNetListener(this);
    log.debug("Training finished");
    Vector<org.joone.engine.Pattern> errorVector = error.getAllPatterns();
    List<Double> errorList = new LinkedList();
    for (org.joone.engine.Pattern errPattern : errorVector) {
      double[] errorArray = errPattern.getArray();
      assert errorArray.length == 1 : "Value " + errorArray.length;
      errorList.add(errorArray[0]);
    }
    isTrained = true;
    TrainResult trainResult = new TrainResult();
    trainResult.setErrors(errorList);
    trainResult.setPerformedCycles(errorVector.size());
    return trainResult;
  }

  protected void bindLayer(InputBlock inputBlock) {
    bindLayer(inputBlock.getId(), new double[][]{inputBlock.getInput()});
  }

  protected void bindLayer(String layerName, double[][] inputArray) {
    MemoryInputSynapse memInput = new MemoryInputSynapse();
    memInput.setInputArray(inputArray);
    memInput.setAdvancedColumnSelector(generateSequence(inputArray[0].length));
    Layer inputLayer = neuralNet.getLayer(layerName);
    if (inputLayer == null) {
      throw new IllegalArgumentException("There is no input layer with name " + layerName);
    }
    inputLayer.removeAllInputs();
    inputLayer.addInputSynapse(memInput);
  }

  public void cicleTerminated(NeuralNetEvent e) {
    Monitor mon = (Monitor) e.getSource();
    long c = mon.getCurrentCicle();
//    if (c % 100 == 0) {
//    log.debug("{} epochs remaining - RMSE = {}", c, mon.getGlobalError());
//    }
  }

  public void errorChanged(NeuralNetEvent e) {
    Monitor mon = (Monitor) e.getSource();
    double globalError = mon.getGlobalError();
    log.debug("Error changed {}", globalError);
    if (globalError < trainParameters.getMaxRmse()) {
      log.debug("Stopping learning");
      neuralNet.stop();
    }
  }

  public void netStarted(NeuralNetEvent e) {
  }

  public void netStopped(NeuralNetEvent e) {
    log.trace("Net stopped {}", e.getNeuralNet());
    synchronized (synchronizer) {
      synchronizer.notify();
    }
  }

  public void netStoppedError(NeuralNetEvent e, String error) {
    log.error("Error occurred during processing neural net {}", error);
    synchronized (synchronizer) {
      synchronizer.notify();
    }
  }

  protected String generateSequence(int length) {
    StringBuilder sb = new StringBuilder();
    sb.append(1);
    for (int i = 2; i <= length; i++) {
      sb.append(",").append(i);
    }
    return sb.toString();
  }
}
