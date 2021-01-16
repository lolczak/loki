/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.context.AsrContext;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 17, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class GaussianMixture implements Pdf<Feature> {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(GaussianMixture.class.getName());

  private double[] logWeights;

  private MultivariateGaussian[] mixtureComponents;

  private LogScale logScale;

  public GaussianMixture(LogScale logScale, MultivariateGaussian mixtureComponent) {
    this(logScale, new MultivariateGaussian[]{mixtureComponent}, new double[]{logScale.getLogOne()});
  }

  public GaussianMixture(LogScale logScale, MultivariateGaussian[] mixtureComponents, double[] logWeights) {
    this.logScale = logScale;
    this.mixtureComponents = mixtureComponents;
    this.logWeights = logWeights;
  }

  public double getValue(Feature randomVariable) {
    double score = logWeights[0] + mixtureComponents[0].getValue(randomVariable);

    for (int i = 1; i < mixtureComponents.length; i++) {
      score = logScale.addAsLinear(score, logWeights[i] + mixtureComponents[i].getValue(randomVariable));
    }
    return score;
  }

  public int getNoMixtures() {
    return logWeights.length;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("GaussianMixture{");
    sb.append(" [");
    for (int m = 0; m < logWeights.length; m++) {
      sb.append("logWeight=" + logWeights[m]);
      sb.append(", mixtureComponent=" + mixtureComponents[m]);
      sb.append(";");
    }
    sb.append("] }");
    return sb.toString();
  }

  public double[] getWeights() {
    LogScale logScale = AsrContext.getContext().getLogScale();
    double[] weights = new double[logWeights.length];
    for (int i = 0; i < weights.length; i++) {
      weights[i] = logScale.logToLinear(logWeights[i]);
    }
    return weights;
  }

  /**
   * Getter for property 'logWeights'.
   *
   * @return Value for property 'logWeights'.
   */
  public double[] getLogWeights() {
    return logWeights;
  }

  /**
   * Getter for property 'mixtureComponents'.
   *
   * @return Value for property 'mixtureComponents'.
   */
  public MultivariateGaussian[] getMixtureComponents() {
    return mixtureComponents;
  }
}
