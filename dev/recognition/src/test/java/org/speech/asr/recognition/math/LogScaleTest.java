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
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 28, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class LogScaleTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(LogScaleTest.class.getName());

  @Test
  public void simpleTest() {
    double m = 5;
    double d = 2;
    LogScale logScale = new SimpleLogScale(10);
    Gaussian gaussian = new Gaussian(m, d);
    MultivariateGaussian logGaussian = new MultivariateGaussian(logScale, m, d);

    double r = 4.45546;
    Feature f = new FeatureImpl(r);

    log.info("Linear pdf {}", gaussian.getValue(f));
    log.info("Log pdf {}", logGaussian.getValue(f));

    Assert.assertTrue(MathUtils.compare(logGaussian.getValue(f), logScale.linearToLog(gaussian.getValue(f))));
    Assert.assertTrue(MathUtils.compare(gaussian.getValue(f), logScale.logToLinear(logGaussian.getValue(f))));
  }

  @Test
  public void addAsLinearTest() {
    LogScale logScale = new SimpleLogScale(10);
    double logSum = logScale.addAsLinear(logScale.linearToLog(4), logScale.linearToLog(5), logScale.linearToLog(1));
    log.info("Log sum {}, proper value {}", logSum, logScale.linearToLog(10));
    Assert.assertTrue(MathUtils.compare(logSum, logScale.linearToLog(10)));
  }

  @Test
  public void addAsLinearTest2() {
    LogScale logScale = new SimpleLogScale(10);
    double logSum = logScale.addAsLinear(logScale.getLogZero(), logScale.linearToLog(5), logScale.linearToLog(1));
    log.info("Log sum {}, proper value {}", logSum, logScale.linearToLog(6));
    Assert.assertTrue(MathUtils.compare(logSum, logScale.linearToLog(6)));
  }

  @Test
  public void addAsLinearTest3() {
    LogScale logScale = new SimpleLogScale(10);
    double logSum = logScale.subtractAsLinear(logScale.linearToLog(5), logScale.linearToLog(1));
    log.info("Log sum {}, proper value {}", logSum, logScale.linearToLog(4));
    Assert.assertTrue(MathUtils.compare(logSum, logScale.linearToLog(4)));
  }


  @Test
  public void addAsLinearTest4() {
    LogScale logScale = new SimpleLogScale(10);
    double logSum = logScale.addAsLinear(-Double.MAX_VALUE, -Double.MAX_VALUE);
    log.info("Log sum {}, proper value {}", logSum, logScale.linearToLog(0));
    Assert.assertTrue(MathUtils.compare(logSum, logScale.linearToLog(0)));

    double minusInf = -Double.MAX_VALUE;
    minusInf = 2 * minusInf;
    logSum = logScale.addAsLinear(minusInf, minusInf);
    log.info("Log sum {}, proper value {}", logSum, logScale.linearToLog(0));
    Assert.assertTrue(MathUtils.compare(logSum, logScale.linearToLog(0)));

    logSum = logScale.addAsLinear(minusInf, logScale.linearToLog(5));
    log.info("Log sum {}, proper value {}", logSum, logScale.linearToLog(5));
    Assert.assertTrue(MathUtils.compare(logSum, logScale.linearToLog(5)));
  }

  @Test
  public void addAsLinearTest5() {
    LogScale logScale = new SimpleLogScale(10);
    double logSum = logScale.addAsLinear(0, -Double.MAX_VALUE / 3);
    log.info("Log sum {}, proper value {}", logSum, logScale.linearToLog(1));
    Assert.assertTrue(MathUtils.compare(logSum, logScale.linearToLog(1)));

  }
}
