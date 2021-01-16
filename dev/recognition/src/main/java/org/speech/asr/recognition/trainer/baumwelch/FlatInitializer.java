/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.baumwelch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.AcousticModel;
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.acoustic.StateDescriptor;
import org.speech.asr.recognition.acoustic.GmmAcousticModel;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.math.GaussianMixture;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.MathUtils;
import org.speech.asr.recognition.math.MultivariateGaussian;
import org.speech.asr.recognition.trainer.TrainSentence;
import org.speech.asr.recognition.util.MatrixUtils;

import java.util.Collection;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 20, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class FlatInitializer implements ModelInitializer {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(FlatInitializer.class.getName());

  public void initializeStates(Collection<TrainSentence> trainingSet, GmmAcousticModel model) {
    int noDimensions = trainingSet.iterator().next().getObservations().get(0).getData().length;
    double[] mean = new double[noDimensions];
    double[] deviation = new double[noDimensions];
    int n = 0;
    for (TrainSentence sentence : trainingSet) {
      for (Feature feature : sentence.getObservations()) {
        n++;
        assert feature.getData().length == noDimensions : "Value " + feature.getData().length;
        for (int d = 0; d < noDimensions; d++) {
          mean[d] += feature.getData()[d];
        }
      }
    }
    for (int d = 0; d < noDimensions; d++) {
      mean[d] /= (double) n;
    }

    for (TrainSentence sentence : trainingSet) {
      for (Feature feature : sentence.getObservations()) {
        for (int d = 0; d < noDimensions; d++) {
          deviation[d] += MathUtils.sqr(feature.getData()[d] - mean[d]);
        }
      }
    }
    for (int d = 0; d < noDimensions; d++) {
      deviation[d] = Math.sqrt(deviation[d] / (double) n);
    }
    LogScale logScale = AsrContext.getContext().getLogScale();
    log.debug("Initializing all states on mean={} and deviation={}", MatrixUtils.vectorToString(mean),
        MatrixUtils.vectorToString(deviation));
    for (StateDescriptor state : model.getAllStates()) {
      state.setLogSelfLoopProbability(logScale.getLogHalf());
      MultivariateGaussian gaussian =
          new MultivariateGaussian(logScale, MatrixUtils.copyVector(mean), MatrixUtils.copyVector(deviation));
      GaussianMixture gmm = new GaussianMixture(logScale, gaussian);
      state.setScorer(gmm);
    }
  }

}
