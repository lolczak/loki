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
import org.joone.io.FileOutputSynapse;
import org.joone.io.MemoryInputSynapse;
import org.joone.io.MemoryOutputSynapse;
import org.joone.net.NeuralNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.Random;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 29, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SmokeTests implements NeuralNetListener {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SmokeTests.class.getName());

  private static String inputData = "/home/luol/ann/xor.txt";
  private static String outputFile = "/home/luol/ann/xorout.txt";

  private double[][] inputArray;

  private double[][] outputArray;

  boolean isLearning = true;

  private NeuralNet nnet;

  private Layer inputLayer1;

  private Layer inputLayer2;

  @Test
  public void xor3Test() {
    generate3Patterns();
    /*
     * Firts, creates the three Layers
     */
    LinearLayer input1 = new LinearLayer();
    LinearLayer input2 = new LinearLayer();
    SigmoidLayer hidden = new SigmoidLayer();
    SigmoidLayer output = new SigmoidLayer();
    input1.setLayerName("input1");
    input2.setLayerName("input2");
    hidden.setLayerName("hidden");
    output.setLayerName("output");
    /* sets their dimensions */
    input1.setRows(2);
    input2.setRows(1);
    hidden.setRows(8);
    output.setRows(1);

    /*
     * Now create the two Synapses
     */
    FullSynapse synapse_IH = new FullSynapse(); /* input1 -> hidden conn. */
    FullSynapse synapse_IH2 = new FullSynapse(); /* input2 -> hidden conn. */
    FullSynapse synapse_HO = new FullSynapse(); /* hidden -> output conn. */

    synapse_IH.setName("IH");
    synapse_IH2.setName("IH2");
    synapse_HO.setName("HO");
    /*
     * Connect the input1 layer whit the hidden layer
     */
    input1.addOutputSynapse(synapse_IH);
    input2.addOutputSynapse(synapse_IH2);
    hidden.addInputSynapse(synapse_IH);
    hidden.addInputSynapse(synapse_IH2);
    /*
     * Connect the hidden layer whit the output layer
     */
    hidden.addOutputSynapse(synapse_HO);
    output.addInputSynapse(synapse_HO);

    MemoryInputSynapse memInput = new MemoryInputSynapse();
    memInput.setInputArray(inputArray);
    memInput.setAdvancedColumnSelector("1,2");

    input1.addInputSynapse(memInput);

    MemoryInputSynapse memInput2 = new MemoryInputSynapse();
    memInput2.setInputArray(inputArray);
    memInput2.setAdvancedColumnSelector("3");
    input2.addInputSynapse(memInput2);


    TeachingSynapse trainer = new TeachingSynapse();


    MemoryInputSynapse memSamples = new MemoryInputSynapse();
    memSamples.setInputArray(inputArray);
    memSamples.setAdvancedColumnSelector("4");

    trainer.setDesired(memSamples);

    /* Creates the error output file */
    FileOutputSynapse error = new FileOutputSynapse();
    error.setFileName(outputFile);
    //error.setBuffered(false);
    trainer.addResultSynapse(error);

    /* Connects the Teacher to the last layer of the net */
    output.addOutputSynapse(trainer);
    nnet = new NeuralNet();
    inputLayer1 = input1;
    inputLayer2 = input2;
    nnet.addLayer(input1, NeuralNet.INPUT_LAYER);
    nnet.addLayer(input2, NeuralNet.INPUT_LAYER);
    nnet.addLayer(hidden, NeuralNet.HIDDEN_LAYER);
    nnet.addLayer(output, NeuralNet.OUTPUT_LAYER);
    nnet.setTeacher(trainer);
    // Gets the Monitor object and set the learning parameters
    Monitor monitor = nnet.getMonitor();
    monitor.setLearningRate(0.8);
    monitor.setMomentum(0.3);

    /* The application registers itself as monitor's listener
     * so it can receive the notifications of termination from
     * the net.
     */
    monitor.addNeuralNetListener(this);

    monitor.setTrainingPatterns(8); /* # of rows (patterns) contained in the input1 file */
    monitor.setTotCicles(4000); /* How many times the net must be trained on the input1 patterns */
    monitor.setLearning(true); /* The net must be trained */
    nnet.go(); /* The net starts the training job */
    synchronized (monitor) {
      try {
        monitor.wait();
      } catch (InterruptedException e) {
        log.error("", e);
      }
    }
  }

  private void generate2Patterns() {
    inputArray = new double[][]{
        {0.0, 0.0, 0.0},
        {0.0, 1.0, 1.0},
        {1.0, 0.0, 1.0},
        {1.0, 1.0, 0.0}};
  }


  private void generate3Patterns() {
    inputArray = new double[][]{
        {0.0, 0.0, 1.0, 1.0},
        {0.0, 0.0, 0.0, 0.0},
        {0.0, 1.0, 0.0, 1.0},
        {1.0, 0.0, 0.0, 1.0},
        {0.0, 1.0, 1.0, 0.0},
        {1.0, 1.0, 0.0, 0.0},
        {0.0, 0.0, 0.0, 0.0},
        {1.0, 0.0, 1.0, 0.0}};

  }

  Object monitor = new Object();

  public void cicleTerminated(NeuralNetEvent e) {
    Monitor mon = (Monitor) e.getSource();
    long c = mon.getCurrentCicle();
/* We want print the results every 100 epochs */
    if (c % 100 == 0)
      log.debug(c + " epochs remaining - RMSE = " + mon.getGlobalError());
  }

  public void errorChanged(NeuralNetEvent e) {
    //log.debug("errorChanged {}", e);
  }

  public void netStarted(NeuralNetEvent e) {
    log.debug("netStarted {}", e);
  }

  MemoryOutputSynapse outSynapse;

  public void netStopped(NeuralNetEvent e) {
    log.debug("netStopped {}", e);
    if (isLearning) {
      Monitor mon = nnet.getMonitor();
      MemoryInputSynapse memInput = new MemoryInputSynapse();
      memInput.setInputArray(new double[][]{{0, 0, 1}});
//      inputArray = generateInputs(1000);
//      memInput.setInputArray(inputArray);
      memInput.setAdvancedColumnSelector("1,2");
      inputLayer1.removeAllInputs();
      inputLayer1.addInputSynapse(memInput);

      MemoryInputSynapse memInput2 = new MemoryInputSynapse();
      memInput2.setInputArray(new double[][]{{1, 1, 1}});
//      inputArray = generateInputs(1000);
//      memInput.setInputArray(inputArray);
      memInput2.setAdvancedColumnSelector("3");
      inputLayer2.removeAllInputs();
      inputLayer2.addInputSynapse(memInput2);
      Layer inp = nnet.getLayer("input2");
      outSynapse = new MemoryOutputSynapse();
      outSynapse.setBuffered(true);
      nnet.getOutputLayer().removeAllOutputs();
      nnet.getOutputLayer().addOutputSynapse(outSynapse);
      mon.setTotCicles(1);
      mon.setTrainingPatterns(1);
      mon.setLearning(false);
      isLearning = false;
      log.debug("Starting forward pass...");
      nnet.go();
    } else {
      log.debug("Output {}", outSynapse.getAllPatterns().size());
      System.exit(1);
    }
  }

  protected double[][] generateInputs(int length) {
    double[][] input = new double[length][3];
    Random random = new Random();
    for (int i = 0; i < length; i++) {
      input[i][0] = random.nextDouble();
      input[i][1] = random.nextDouble();
      input[i][2] = random.nextDouble();
    }
    return input;
  }

  public void netStoppedError(NeuralNetEvent e, String error) {
    log.debug("netStoppedError {}", e);
  }
}
