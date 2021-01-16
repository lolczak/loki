/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann.joone;

import org.joone.engine.Layer;
import org.joone.net.NeuralNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.recognition.ann.*;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 24, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ParallelNeuralNet implements NeuralNetwork {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ParallelNeuralNet.class.getName());

  private static ParallelNeuralNet manager;

  private List<NeuralNetWorker> workers;

  private NeuralNet prototype;

  private int availableProcessors;

  private volatile CountDownLatch cycleSignal;

  private volatile CountDownLatch startSignal;

  private Map<String, DeltaAccumulator> synapseAccumulatorMap;

  private Map<String, DeltaAccumulator> layerAccumulatorMap;

  private List<TrainPattern> trainingPatterns;

  private TrainParameters parameters;

  private double error;

  private TrainResult trainResult;

  private boolean isTrained;

  private BlockingQueue<NeuralNetWorker> workerQueue;

  public static ParallelNeuralNet getManager() {
    return manager;
  }

  public ParallelNeuralNet(NeuralNet prototype) {
    this.prototype = prototype;
    this.workerQueue = new LinkedBlockingQueue();
    isTrained = false;
    layerAccumulatorMap = new HashMap();
    synapseAccumulatorMap = new HashMap();
    initPool();
    manager = this;
  }

  public Serializable getMemento() {
    NeuralNet nnet = workers.get(0).getNeuralNet();
    for (Layer layer : (Vector<Layer>) nnet.getLayers()) {
      if (layer.isInputLayer()) {
        log.debug("Removing inputs for {}", layer.getLayerName());
        layer.removeAllInputs();
      }
      if (layer.isOutputLayer()) {
        log.debug("Removing outputs for {}", layer.getLayerName());
        layer.removeAllOutputs();
      }
    }

    return nnet;
  }

  private void initPool() {
    availableProcessors = Runtime.getRuntime().availableProcessors();
    log.info("Number of processors {}", availableProcessors);
    workers = new ArrayList(availableProcessors);
  }

  public double[] forward(double[] input) {
    NeuralNetWorker worker = null;
    try {
      worker = workerQueue.take();
      JooneNeuralNetAdapter adapter = new JooneNeuralNetAdapter(worker.getNeuralNet(), true);
      return adapter.forward(input);
    } catch (Exception e) {
      throw new AsrRuntimeException(e);
    } finally {
      if (worker != null) {
        try {
          workerQueue.put(worker);
        } catch (InterruptedException e) {
          throw new AsrRuntimeException(e);
        }
      }
    }
  }

  public double[] forward(Collection<InputBlock> inputs) {
    NeuralNetWorker worker = null;
    try {
      worker = workerQueue.take();
      JooneNeuralNetAdapter adapter = new JooneNeuralNetAdapter(worker.getNeuralNet(), true);
      return adapter.forward(inputs);
    } catch (Exception e) {
      throw new AsrRuntimeException(e);
    } finally {
      if (worker != null) {
        try {
          workerQueue.put(worker);
        } catch (InterruptedException e) {
          throw new AsrRuntimeException(e);
        }
      }
    }
  }

  public boolean isTrained() {
    return isTrained;
  }

  public TrainResult train(List<TrainPattern> trainingPatterns) {
    return null;  //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public TrainResult train(List<TrainPattern> trainingPatterns, TrainParameters parameters) {
    workerQueue.clear();
    this.trainingPatterns = trainingPatterns;
    this.parameters = parameters;
    if (parameters.isRandomize()) {
      startTraining();
    } else {
      retrain();
    }
    isTrained = true;
    workerQueue.addAll(workers);
    return trainResult;
  }

  protected void retrain() {
    synapseAccumulatorMap.clear();
    layerAccumulatorMap.clear();
    List<List<TrainPattern>> buckets = splitPatterns();
    for (int i = 0; i < availableProcessors; i++) {
      NeuralNetWorker worker = workers.get(i);
      worker.setPartialTrainingSet(buckets.get(i));
      worker.setTrainParameters(parameters);
    }
    train();
  }

  protected void startTraining() {
    workers.clear();
    synapseAccumulatorMap.clear();
    layerAccumulatorMap.clear();
    initWorkers();
    train();
  }

  protected void startWorkers() {
    log.debug("Starting workers...");
    cycleSignal = new CountDownLatch(workers.size());
    startSignal = new CountDownLatch(1);
    for (NeuralNetWorker worker : workers) {
      worker.run();
    }
  }

  private void train() {
    List<Double> errorList = new LinkedList();
    for (int epoch = 0; epoch < parameters.getNoCycles(); epoch++) {
      log.info("Performing {} epoch of parallel learning", epoch);
      performFullCycle(epoch);
      waitForWorkers();
      updateError();
      log.info("Error after epoch {} is {}", epoch, error);
      errorList.add(error);
    }
    trainResult = new TrainResult();
    trainResult.setErrors(errorList);
    trainResult.setPerformedCycles(parameters.getNoCycles());
  }

  private void performFullCycle(int epoch) {
    if (epoch == 0) {
      startWorkers();
    } else {
      startSignal.countDown();
      startSignal = new CountDownLatch(1);
    }
  }

  private void updateError() {
    error = 0;
    for (NeuralNetWorker worker : workers) {
      error += worker.getGlobalError();
    }
    error /= trainingPatterns.size();
    error = Math.sqrt(error);
    for (NeuralNetWorker worker : workers) {
      worker.setGlobalError(error);
    }
  }

  protected void waitForWorkers() {
    log.debug("Waiting for workers...");
    try {
      cycleSignal.await();
      cycleSignal = new CountDownLatch(workers.size());
    } catch (InterruptedException e) {
      log.error("", e);
    }
  }

  protected void initWorkers() {
    prototype.randomize(parameters.getRandomizeAmplitude());
    List<List<TrainPattern>> buckets = splitPatterns();
    for (int i = 0; i < availableProcessors; i++) {
      NeuralNet netClone = prototype.cloneNet();
      NeuralNetWorker worker = new NeuralNetWorker(i, netClone, buckets.get(i));
      worker.setTrainParameters(parameters);
      workers.add(worker);
    }
  }

  protected List<List<TrainPattern>> splitPatterns() {
    List<List<TrainPattern>> buckets = new ArrayList(availableProcessors);
    double splitSize = (double) trainingPatterns.size() / (double) availableProcessors;
    for (int i = 0; i < availableProcessors; i++) {
      int startIndex = (int) Math.floor(((double) i) * splitSize);
      int stopIndex = i == availableProcessors - 1 ? trainingPatterns.size() : (int) Math.floor((i + 1) * splitSize);
      buckets.add(trainingPatterns.subList(startIndex, stopIndex));
    }
    return buckets;
  }

  public synchronized DeltaAccumulator getSynapseAccumulator(String synapse, ParallelWeightUpdater updater) {
    log.trace("Getting accumulator for synapse {}", synapse);
    DeltaAccumulator accumulator = synapseAccumulatorMap.get(synapse);
    if (accumulator == null) {
      accumulator = new DeltaAccumulator(DeltaAccumulator.DeltaType.WEIGHTS, updater.getRows(), updater.getColumns(),
          workers.size());
      synapseAccumulatorMap.put(synapse, accumulator);
    }
    return accumulator;
  }

  public synchronized DeltaAccumulator getLayerAccumulator(String layer, ParallelWeightUpdater updater) {
    log.trace("Getting accumulator for layer {}", layer);
    DeltaAccumulator accumulator = layerAccumulatorMap.get(layer);
    if (accumulator == null) {
      accumulator = new DeltaAccumulator(DeltaAccumulator.DeltaType.BIASES, updater.getRows(), updater.getColumns(),
          workers.size());
      layerAccumulatorMap.put(layer, accumulator);
    }
    return accumulator;
  }

  public CountDownLatch getCycleSignal() {
    return cycleSignal;
  }

  public CountDownLatch getStartSignal() {
    return startSignal;
  }
}
