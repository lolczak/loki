package org.speech.asr.recognition.ann;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jul 30, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface NeuralNetwork {

  boolean isTrained();

  TrainResult train(List<TrainPattern> trainingPatterns);

  TrainResult train(List<TrainPattern> trainingPatterns, TrainParameters parameters);

  double[] forward(double[] input);

  double[] forward(Collection<InputBlock> inputs);

  Serializable getMemento();
}
