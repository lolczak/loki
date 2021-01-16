/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann;

import org.joone.engine.*;
import org.joone.net.NeuralNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.ann.joone.SoftmaxLayerExt;
import org.speech.asr.recognition.ann.joone.SigmoidLayerExt;

import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 4, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class JooneNeuralNetBuilder implements NeuralNetworkBuilder {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(JooneNeuralNetBuilder.class.getName());

  private Map<LayerType, LayerFactory> layerFactories;

  private Map<SynapseType, SynapseFactory> synapseFactories;

  private Map<LayerPosition, Integer> positionMap;

  private NeuralNet neuralNet;

  public JooneNeuralNetBuilder() {
    neuralNet = new NeuralNet();
    initMap();
  }

  private void initMap() {
    layerFactories = new HashMap();
    synapseFactories = new HashMap();
    positionMap = new HashMap();
    layerFactories.put(LayerType.LINEAR, new LinearLayerFactory());
    layerFactories.put(LayerType.SIGMOID, new SigmoidLayerFactory());
    layerFactories.put(LayerType.SOFT_MAX, new SoftMaxLayerFactory());
    layerFactories.put(LayerType.TANH, new TanhLayerFactory());

    synapseFactories.put(SynapseType.DIRECT, new DirectSynapseFactory());
    synapseFactories.put(SynapseType.FULL, new FullSynapseFactory());

    positionMap.put(LayerPosition.INPUT, NeuralNet.INPUT_LAYER);
    positionMap.put(LayerPosition.HIDDEN, NeuralNet.HIDDEN_LAYER);
    positionMap.put(LayerPosition.OUTPUT, NeuralNet.OUTPUT_LAYER);
  }

  public void addLayer(String name, LayerType type, int noNeurons, LayerPosition position) {
    Layer layer = createLayer(type);
    layer.setLayerName(name);
    layer.setRows(noNeurons);
    neuralNet.addLayer(layer, getPositionNumber(position));
    log.debug("Added layer with name {} and type {}", name, type);
  }

  public void connect(String outputLayer, String inputLayer, SynapseType type) {
    Synapse synapse = createSynapse(type);
    Layer outLayer = neuralNet.getLayer(outputLayer);
    Layer inLayer = neuralNet.getLayer(inputLayer);
    if (outLayer == null || inLayer == null) {
      throw new IllegalArgumentException("Can't find some layers");
    }
    synapse.setName("synapse_from_" + outputLayer + "_to_" + inputLayer);
    outLayer.addOutputSynapse(synapse);
    inLayer.addInputSynapse(synapse);
    log.debug("Added synapse between {} and {} with type {}", new Object[]{outputLayer, inputLayer, type});
  }

  public NeuralNetwork getNeuralNetwork() {
    return new JooneNeuralNetAdapter(neuralNet, false);
  }

  public NeuralNet getNeuralNet() {
    return neuralNet;
  }

  private int getPositionNumber(LayerPosition position) {
    Integer number = positionMap.get(position);
    if (number == null) {
      throw new IllegalArgumentException("There is no mapping for " + position);
    }
    return number;
  }

  private Layer createLayer(LayerType type) {
    LayerFactory factory = layerFactories.get(type);
    if (factory == null) {
      throw new IllegalArgumentException("There is no factory for type " + type);
    }
    return factory.createLayer();
  }

  private Synapse createSynapse(SynapseType type) {
    SynapseFactory factory = synapseFactories.get(type);
    if (factory == null) {
      throw new IllegalArgumentException("There is no factory for type " + type);
    }
    return factory.createSynapse();
  }

  private interface LayerFactory {
    Layer createLayer();
  }

  private interface SynapseFactory {
    Synapse createSynapse();
  }

  private class DirectSynapseFactory implements SynapseFactory {
    public Synapse createSynapse() {
      return new DirectSynapse();
    }
  }

  private class FullSynapseFactory implements SynapseFactory {
    public Synapse createSynapse() {
      return new FullSynapse();
    }
  }

  private class LinearLayerFactory implements LayerFactory {
    public Layer createLayer() {
      return new LinearLayer();
    }
  }

  private class SigmoidLayerFactory implements LayerFactory {
    public Layer createLayer() {
      return new SigmoidLayerExt();
    }
  }

  private class SoftMaxLayerFactory implements LayerFactory {
    public Layer createLayer() {
      return new SoftmaxLayerExt();
    }
  }

  private class TanhLayerFactory implements LayerFactory {
    public Layer createLayer() {
      return new TanhLayer();
    }
  }
}
