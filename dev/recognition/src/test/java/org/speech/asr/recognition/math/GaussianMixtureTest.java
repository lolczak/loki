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
import org.speech.asr.recognition.acoustic.FeatureImpl;
import org.speech.asr.recognition.context.AsrContext;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 17, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class GaussianMixtureTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(GaussianMixtureTest.class.getName());

  @Test
  public void testGaussianMixture() {
    LogScale logScale = new SimpleLogScale(1.0001);
    MultivariateGaussian mixComp1 = new MultivariateGaussian(logScale, 5, 1);
    MultivariateGaussian mixComp2 = new MultivariateGaussian(logScale, 3, 1);
    double weight1 = 0.3;
    double weight2 = 0.7;
    Feature testFeature = new FeatureImpl(4.0);

    double prob1 = logScale.logToLinear(mixComp1.getValue(testFeature));
    double prob2 = logScale.logToLinear(mixComp2.getValue(testFeature));
    double expected = logScale.linearToLog(weight1 * prob1 + weight2 * prob2);

    GaussianMixture gmm = new GaussianMixture(logScale, new MultivariateGaussian[]{mixComp1, mixComp2},
        new double[]{logScale.linearToLog(weight1), logScale.linearToLog(weight2)});

    Assert.assertTrue(MathUtils.compare(expected, gmm.getValue(testFeature)));
  }

  @Test
  public void testGaussianMixtureGenerator() {
    LogScale logScale = new SimpleLogScale(1.0001);
    AsrContext ctx = new AsrContext();
    ctx.setLogScale(logScale);
    AsrContext.setContext(ctx);
    MultivariateGaussian mixComp1 = new MultivariateGaussian(logScale, 100, 10);
    MultivariateGaussian mixComp2 = new MultivariateGaussian(logScale, 200, 10);
    double weight1 = 0.3;
    double weight2 = 0.7;


    GaussianMixture gmm = new GaussianMixture(logScale, new MultivariateGaussian[]{mixComp1, mixComp2},
        new double[]{logScale.linearToLog(weight1), logScale.linearToLog(weight2)});
    GaussianMixtureGenerator generator = new GaussianMixtureGenerator(gmm);

    int comp1 = 0;
    int comp2 = 0;
    int all = 100000;
    for (int i = 0; i < all; i++) {
      double[] random = generator.nextRandom();
      double diff1 = Math.abs(100 - random[0]);
      double diff2 = Math.abs(200 - random[0]);
      if (diff1 < diff2) {
        comp1++;
      } else {
        comp2++;
      }
    }

    double prob1 = (double) comp1 / (double) all;
    double prob2 = (double) comp2 / (double) all;

    log.info("Expected ratio={} result={}", weight1 / weight2, prob1 / prob2);
    double relDiff = Math.abs(((weight1 / weight2) - (prob1 / prob2)) / (weight1 / weight2));

    Assert.assertTrue(relDiff < 0.01);
  }

  @Test
  public void testZeroVariance() {
    LogScale logScale = new SimpleLogScale(1.0001);
    AsrContext ctx = new AsrContext();
    ctx.setLogScale(logScale);
    AsrContext.setContext(ctx);
    MultivariateGaussian mixComp1 = new MultivariateGaussian(logScale, 100, Double.MIN_VALUE);
  }
}
