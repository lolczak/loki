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
import org.speech.asr.recognition.acoustic.FeatureImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 12, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class MultivariateGaussianTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MultivariateGaussianTest.class.getName());

  @Test
  public void simpleTest() {
    LogScale logScale = new SimpleLogScale(1.0001);
    double mean[] = new double[]{3, 4, 5};
    double deviation[] = new double[]{2, 1, 0.5};
    MultivariateGaussian pdf = new MultivariateGaussian(logScale, mean, deviation);

    double[] x = new double[]{2.5, 5, 5};

    double linearPdf = linearGaussian(mean, deviation, x);
    double logPdf = pdf.getValue(new FeatureImpl(x));

    Assert.assertTrue(MathUtils.compare(logPdf, logScale.linearToLog(linearPdf)));
  }

  private double linearGaussian(double[] mean, double[] deviation, double[] randomVariable) {
    assert mean.length == deviation.length && deviation.length == randomVariable.length;
    double value = 1.0;
    for (int d = 0; d < randomVariable.length; d++) {
      value *= 1.0 / Math.sqrt(2.0 * Math.PI * MathUtils.sqr(deviation[d])) *
          Math.exp(-0.5 * MathUtils.sqr(randomVariable[d] - mean[d]) / MathUtils.sqr(deviation[d]));
    }

    return value;
  }
}
