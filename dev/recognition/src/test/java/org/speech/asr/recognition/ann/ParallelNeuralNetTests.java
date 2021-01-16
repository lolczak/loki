/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann;

import org.joone.net.NeuralNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.ann.joone.ParallelNeuralNet;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.SimpleLogScale;
import org.speech.asr.recognition.trainer.baumwelch.vo.Symbol;
import org.speech.asr.recognition.trainer.util.SyntheticDataGenerator;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 24, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ParallelNeuralNetTests {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ParallelNeuralNetTests.class.getName());

  private int noSymbols;

  private double avgObservationDistance;

  private double avgObservationDeviation;

  private int noDimensions;

  private int noMixtures;

  private int noPatterns;

  private SyntheticDataGenerator dataGenerator;

  private NeuralNet neuralNet;

  private List<TrainPattern> patterns;

  @Test
  public void testParallelLearning() {
    prepare();
    TrainParameters defaultParams = new TrainParameters();
    defaultParams.setLearningRate(0.01);
    defaultParams.setMomentum(0.0);
    defaultParams.setRandomizeAmplitude(0.5);
    defaultParams.setNoCycles(100);
    defaultParams.setBatchSize(35);
    defaultParams.setRandomize(true);
    neuralNet.randomize(defaultParams.getRandomizeAmplitude());
    boolean isMultiThread = true;
    Set<TrainPattern> set = new HashSet(patterns);
    patterns = new LinkedList(set);
    if (isMultiThread) {
      ParallelNeuralNet net = new ParallelNeuralNet(neuralNet);
      net.train(patterns, defaultParams);
      defaultParams.setRandomize(false);
//      net.train(patterns, defaultParams);
      log.debug("Testing batch");
      defaultParams.setLearningMode(LearningMode.BATCH);
      defaultParams.setRandomize(false);
      defaultParams.setBatchSize(70);
      JooneNeuralNetAdapter adapter = new JooneNeuralNetAdapter(neuralNet, false);
      adapter.train(patterns, defaultParams);
    } else {
      NeuralNet clone = neuralNet.cloneNet();
      log.debug("Testing batch");
      defaultParams.setLearningMode(LearningMode.BATCH);
      JooneNeuralNetAdapter adapter = new JooneNeuralNetAdapter(neuralNet, false);
      adapter.train(patterns, defaultParams);
      log.debug("Testing online");
      defaultParams.setLearningMode(LearningMode.ONLINE);
      adapter = new JooneNeuralNetAdapter(clone, false);
      adapter.train(patterns, defaultParams);
    }
  }

  protected void prepare() {
    LogScale logScale = new SimpleLogScale(1.0001);
    AsrContext ctx = new AsrContext();
    ctx.setTransitionScored(false);
    ctx.setLogScale(logScale);
    AsrContext.setContext(ctx);

    noSymbols = 30;
    avgObservationDistance = 10;
    avgObservationDeviation = 10;
    noDimensions = 30;
    noPatterns = 1001;
    noMixtures = 3;
    dataGenerator = new SyntheticDataGenerator();
    dataGenerator.setNoSymbols(noSymbols);
    dataGenerator.setAvgObservationDeviation(avgObservationDeviation);
    dataGenerator.setAvgObservationDistance(avgObservationDistance);
    dataGenerator.setLogScale(logScale);
    dataGenerator.setNoDimensions(noDimensions);
    dataGenerator.setNoMixtures(noMixtures);
    dataGenerator.generateSymbols();

    neuralNet = buildNet();
    patterns = new LinkedList();
    for (int i = 0; i < noPatterns; i++) {
      Symbol symbol = dataGenerator.randomSymbol();
      int outputNumber = Integer.valueOf(symbol.getName()) - 1;
      double[] desired = new double[noSymbols];
      double[] input = symbol.getGenerator().nextRandom();
      desired[outputNumber] = 1.0;
      TrainPattern pattern = new TrainPattern(input, desired);
      patterns.add(pattern);
    }
  }

  protected NeuralNet buildNet() {
    //noDimensions, noDimensions * noSymbols, noSymbols

    JooneNeuralNetBuilder builder = new JooneNeuralNetBuilder();
    builder.addLayer("input", LayerType.LINEAR, noDimensions, LayerPosition.INPUT);
//    builder.addLayer("input2", LayerType.LINEAR, 2, LayerPosition.INPUT);
    builder.addLayer("hidden", LayerType.TANH, noDimensions * noSymbols, LayerPosition.HIDDEN);
    builder.addLayer("output", LayerType.SOFT_MAX, noSymbols, LayerPosition.OUTPUT);

    builder.connect("input", "hidden", SynapseType.FULL);
//    builder.connect("input2", "hidden", SynapseType.FULL);
    builder.connect("hidden", "output", SynapseType.FULL);

    return builder.getNeuralNet();
  }
}
