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

import java.util.Random;

/**
 * Generator wielowymiarowej zmiennej losowej o rozkladzie Gaussa z diagonalna macierza kowiarancji.
 * <p/>
 * Creation date: Jul 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class MultivariateGaussianGenerator implements RandomGenerator {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MultivariateGaussianGenerator.class.getName());

  private MultivariateGaussian gaussian;

  private Random baseGenerator;

  public MultivariateGaussianGenerator(MultivariateGaussian gaussian) {
    this.gaussian = gaussian;
    baseGenerator = new Random();
  }

  public MultivariateGaussian getGaussian() {
    return gaussian;
  }

  public double[] nextRandom() {
    double[] result = new double[gaussian.getMean().length];
    for (int d = 0; d < gaussian.getMean().length; d++) {
      double baseRandomVariable = baseGenerator.nextGaussian();
      result[d] = baseRandomVariable * gaussian.getDeviation()[d] + gaussian.getMean()[d];
    }
    return result;
  }
}
