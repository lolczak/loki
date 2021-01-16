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
import org.speech.asr.recognition.util.MatrixUtils;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 12, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class MultivariateGaussian implements Pdf<Feature> {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MultivariateGaussian.class.getName());

  private double[] mean;

  private double[] deviation;

  private double[] factor1;

  private double[] factor2;

  private int length;

  private LogScale logScale;

  public MultivariateGaussian(LogScale logScale, double mean, double deviation) {
    this(logScale, new double[]{mean}, new double[]{deviation});
  }

  public MultivariateGaussian(LogScale logScale, double[] mean, double[] deviation) {
    if (mean.length != deviation.length) {
      throw new IllegalArgumentException("Length of mean and deviation vector must be the same");
    }
    this.length = mean.length;
    this.logScale = logScale;
    this.mean = mean;
    this.deviation = deviation;
    this.factor1 = new double[length];
    this.factor2 = new double[length];

    for (int d = 0; d < length; d++) {
      factor1[d] = -0.5 * (logScale.linearToLog(2 * Math.PI * MathUtils.sqr(deviation[d])));
      factor2[d] = logScale.getLogE() * (-0.5 / MathUtils.sqr(deviation[d]));
      if (!MathUtils.isReal(factor1[d])) {
        throw new IllegalArgumentException("You have too small deviation");
      }
      if (!MathUtils.isReal(factor2[d])) {
        throw new IllegalArgumentException("You have too small deviation");
      }
    }
  }

  public double getValue(Feature randomVariable) {
    if (randomVariable.getData().length != length) {
      throw new IllegalArgumentException("Length of random variable vector must be the same as pdf length");
    }
    double pdf = 0.0;
    for (int d = 0; d < length; d++) {
      pdf += factor1[d] + factor2[d] * (MathUtils.sqr(randomVariable.getData()[d] - mean[d]));
      assert !Double.isNaN(pdf) : "Value " + pdf;
      if (MathUtils.isNegativeInfinity(pdf)) {
        return MathUtils.NEGATIVE_INFINITY_VALUE;
      }
    }
    return pdf;
  }

  /**
   * Getter for property 'deviation'.
   *
   * @return Value for property 'deviation'.
   */
  public double[] getDeviation() {
    return deviation;
  }

  /**
   * Getter for property 'mean'.
   *
   * @return Value for property 'mean'.
   */
  public double[] getMean() {
    return mean;
  }


  public String toString() {
    return "MultivariateGaussian{" +
        "deviation=" + MatrixUtils.vectorToString(deviation) +
        ", mean=" + MatrixUtils.vectorToString(mean) +
        ", length=" + length +
        '}';
  }

}
