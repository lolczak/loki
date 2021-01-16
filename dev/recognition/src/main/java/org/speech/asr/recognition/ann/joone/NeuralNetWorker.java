/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann.joone;

import org.joone.engine.Layer;
import org.joone.engine.Monitor;
import org.joone.engine.NeuralNetEvent;
import org.joone.engine.NeuralNetListener;
import org.joone.engine.learning.TeachingSynapse;
import org.joone.io.MemoryInputSynapse;
import org.joone.io.MemoryOutputSynapse;
import org.joone.net.NeuralNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.ann.InputBlock;
import org.speech.asr.recognition.ann.TrainParameters;
import org.speech.asr.recognition.ann.TrainPattern;
import org.speech.asr.recognition.util.NeuralNetUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 24, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class NeuralNetWorker implements Runnable, NeuralNetListener {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(NeuralNetWorker.class.getName());

  private NeuralNet neuralNet;

  private List<TrainPattern> partialTrainingSet;

  private TrainParameters trainParameters;

  private int id;

  public NeuralNetWorker(int id, NeuralNet netClone, List<TrainPattern> partialTrainingSet) {
    this.id = id;
    this.neuralNet = netClone;
    this.partialTrainingSet = partialTrainingSet;
  }

  public void run() {
    log.debug("Performing training in worker {} with {} training patterns", id, partialTrainingSet.size());
    train(partialTrainingSet, trainParameters);
  }

  public void train(List<TrainPattern> trainingPatterns, TrainParameters parameters) {
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
    trainer.setTheTeacherSynapse(new CrossEntropySynapse());
    MemoryInputSynapse memSamples = new MemoryInputSynapse();
    memSamples.setInputArray(outputArray);
    memSamples.setAdvancedColumnSelector("1-" + outputArray[0].length);

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
    monitor.setUseRMSE(true);
    monitor.addLearner(0, "org.joone.engine.BasicLearner");
    monitor.addLearner(1, "org.speech.asr.recognition.ann.joone.ParallelBatchLearner");
    monitor.setLearningMode(1);
    monitor.setBatchSize(parameters.getBatchSize());
    monitor.setLearning(true);
    neuralNet.go();
  }

  protected void bindLayer(InputBlock inputBlock) {
    bindLayer(inputBlock.getId(), new double[][]{inputBlock.getInput()});
  }

  protected void bindLayer(String layerName, double[][] inputArray) {
    MemoryInputSynapse memInput = new MemoryInputSynapse();
    memInput.setInputArray(inputArray);
    memInput.setAdvancedColumnSelector("1-" + inputArray[0].length);
    Layer inputLayer = neuralNet.getLayer(layerName);
    if (inputLayer == null) {
      throw new IllegalArgumentException("There is no input layer with name " + layerName);
    }
    inputLayer.removeAllInputs();
    inputLayer.addInputSynapse(memInput);
  }

  public void setGlobalError(double err) {

  }

  public double getGlobalError() {
    double globalError = neuralNet.getMonitor().getGlobalError();
    globalError *= globalError;
    globalError *= neuralNet.getMonitor().getNumOfPatterns();
    return globalError;
  }

  public void cicleTerminated(NeuralNetEvent e) {

  }

  public NeuralNet getNeuralNet() {
    return neuralNet;
  }

  public void errorChanged(NeuralNetEvent e) {
    log.trace("Cycle terminated in worker {} on cycle {}", id, neuralNet.getMonitor().getCurrentCicle());
    CountDownLatch startSignal = ParallelNeuralNet.getManager().getStartSignal();
    ParallelNeuralNet.getManager().getCycleSignal().countDown();
    if (neuralNet.getMonitor().getCurrentCicle() > 1) {
      try {
        startSignal.await();
        log.trace("Starting next cycle in worker {}...", id);
      } catch (InterruptedException e1) {
        log.error("", e1);
      }
    }
  }

  public void netStarted(NeuralNetEvent e) {
//TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public void netStopped(NeuralNetEvent e) {
  }

  public void netStoppedError(NeuralNetEvent e, String error) {
//TODO To change body of implemented methods use File | Settings | File Templates.
  }

  /**
   * Setter for property 'partialTrainingSet'.
   *
   * @param partialTrainingSet Value to set for property 'partialTrainingSet'.
   */
  public void setPartialTrainingSet(List<TrainPattern> partialTrainingSet) {
    this.partialTrainingSet = partialTrainingSet;
  }


  /**
   * Setter for property 'trainParameters'.
   *
   * @param trainParameters Value to set for property 'trainParameters'.
   */
  public void setTrainParameters(TrainParameters trainParameters) {
    this.trainParameters = trainParameters;
  }
}
