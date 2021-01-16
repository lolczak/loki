package org.speech.asr.recognition.ann;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jul 30, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface NeuralNetworkBuilder {

  void addLayer(String name, LayerType type, int noNeurons, LayerPosition position);

  void connect(String outputLayer, String inputLayer, SynapseType type);

  NeuralNetwork getNeuralNetwork();
}
