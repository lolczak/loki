/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann;

import org.joone.engine.FullSynapse;
import org.joone.engine.Layer;
import org.joone.engine.LinearLayer;
import org.joone.engine.SigmoidLayer;
import org.joone.net.NeuralNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 31, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class AdapterTests {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(AdapterTests.class.getName());

  private double[][] inputArray;

  @Test
  public void testXor3() {
    generate3Patterns();

    NeuralNetwork adapter = buildNetwork();//new JooneNeuralNetAdapter(buildNet(), false);
    List<TrainPattern> patterns = createTrainSet();
    adapter.train(patterns);

//    adapter.train(patterns, parameters);
    double[] input1Array = new double[]{1};
    double[] input2Array = new double[]{1, 0};
    InputBlock input1Block = new InputBlock();
    input1Block.setId("input1");
    input1Block.setInput(input1Array);
    InputBlock input2Block = new InputBlock();
    input2Block.setId("input2");
    input2Block.setInput(input2Array);

    List<InputBlock> blocks = new LinkedList();
    blocks.add(input1Block);
    blocks.add(input2Block);
    double[] out = adapter.forward(blocks);
    log.debug("out = {}", out[0]);
    Assert.assertTrue(out[0] < 0.1);
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

  private List<TrainPattern> createTrainSet() {
    List<TrainPattern> patterns = new LinkedList();

    for (int i = 0; i < inputArray.length; i++) {
      double[] input1Array = new double[1];
      double[] input2Array = new double[2];
      double[] outputArray = new double[1];
      input1Array[0] = inputArray[i][0];
      input2Array[0] = inputArray[i][1];
      input2Array[1] = inputArray[i][2];
      outputArray[0] = inputArray[i][3];

      InputBlock input1Block = new InputBlock();
      input1Block.setId("input1");
      input1Block.setInput(input1Array);
      InputBlock input2Block = new InputBlock();
      input2Block.setId("input2");
      input2Block.setInput(input2Array);

      List<InputBlock> blocks = new LinkedList();
      blocks.add(input1Block);
      blocks.add(input2Block);

      TrainPattern pattern = new TrainPattern(blocks, outputArray);
      patterns.add(pattern);
    }

    return patterns;
  }

  private NeuralNetwork buildNetwork() {
    JooneNeuralNetBuilder builder = new JooneNeuralNetBuilder();
    builder.addLayer("input1", LayerType.LINEAR, 1, LayerPosition.INPUT);
    builder.addLayer("input2", LayerType.LINEAR, 2, LayerPosition.INPUT);
    builder.addLayer("hidden", LayerType.SIGMOID, 8, LayerPosition.HIDDEN);
    builder.addLayer("output", LayerType.SIGMOID, 1, LayerPosition.OUTPUT);

    builder.connect("input1", "hidden", SynapseType.FULL);
    builder.connect("input2", "hidden", SynapseType.FULL);
    builder.connect("hidden", "output", SynapseType.FULL);

    return builder.getNeuralNetwork();
  }


  private NeuralNet buildNet() {
    Layer input1 = new LinearLayer();
    Layer input2 = new LinearLayer();
    Layer hidden = new SigmoidLayer();
    Layer output = new SigmoidLayer();
    input1.setLayerName("input1");
    input2.setLayerName("input2");
    hidden.setLayerName("hidden");
    output.setLayerName("output");
    input1.setRows(1);
    input2.setRows(2);
    hidden.setRows(8);
    output.setRows(1);

    FullSynapse synapse_IH1 = new FullSynapse(); /* input -> hidden conn. */
    FullSynapse synapse_IH2 = new FullSynapse(); /* input -> hidden conn. */
    FullSynapse synapse_HO = new FullSynapse(); /* hidden -> output conn. */

    synapse_IH1.setName("IH1");
    synapse_IH2.setName("IH2");
    synapse_HO.setName("HO");
    input1.addOutputSynapse(synapse_IH1);
    input2.addOutputSynapse(synapse_IH2);
    hidden.addInputSynapse(synapse_IH1);
    hidden.addInputSynapse(synapse_IH2);
    hidden.addOutputSynapse(synapse_HO);
    output.addInputSynapse(synapse_HO);

    NeuralNet nnet = new NeuralNet();
    nnet.addLayer(input1, NeuralNet.INPUT_LAYER);
    nnet.addLayer(input2, NeuralNet.INPUT_LAYER);
    nnet.addLayer(hidden, NeuralNet.HIDDEN_LAYER);
    nnet.addLayer(output, NeuralNet.OUTPUT_LAYER);
    return nnet;
  }

}
