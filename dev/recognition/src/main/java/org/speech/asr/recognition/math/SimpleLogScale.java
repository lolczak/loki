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

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 28, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SimpleLogScale implements LogScale {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SimpleLogScale.class.getName());

  private double base;

  private double logBase;

  private double logE;

  private double logHalf;

  public SimpleLogScale(double base) {
    this.base = base;
    logBase = Math.log(base);
    logE = linearToLog(Math.E);
    logHalf = linearToLog(0.5);
  }

  public double addAsLinear(double logVal1, double... logValues) {
    double sum = logVal1;
    for (double logVal : logValues) {
      sum = addAsLinear2(sum, logVal);
    }
    return sum;
  }

  private double addAsLinear2(double logVal1, double logVal2) {
    assert !Double.isNaN(logVal1) : "Value " + logVal1;
    assert !Double.isNaN(logVal2) : "Value " + logVal2;
    //moze byc tylko minus nieskonczonoscia
    assert !Double.isInfinite(logVal1) || logVal1 < 0 : "Value " + logVal1;
    assert !Double.isInfinite(logVal2) || logVal2 < 0 : "Value " + logVal2;
    if (Double.isInfinite(logVal1)) {
      logVal1 = -Double.MAX_VALUE;
    }
    if (Double.isInfinite(logVal2)) {
      logVal2 = -Double.MAX_VALUE;
    }

    //todo copy sphinx optimization
    double logE = getLogE();
    double logDiff = (logVal1 - logVal2) / logE;
    double factor;
    double log;
    if (Double.isInfinite(Math.abs(logDiff))) {
      factor = 1;
      if (logDiff < 0) {
        log = logVal2;
      } else {
        log = logVal1;
      }
    } else {
      factor = Math.exp(logDiff) + 1;
      log = logVal2;
      if (Double.isInfinite(factor)) {
        //przypadek gdy roznica logarytmow jest duza liczba dodatnia i exp jest wtedy rowne nieskonczonosc
        factor = 1;
        log = logVal1;
      }

    }
    double result = log + linearToLog(factor);
    assert !Double.isNaN(result) && !Double.isInfinite(result) : "Value " + result;
    return result;
  }

  public double subtractAsLinear(double logMinuend, double logSubtrahend) {
    //copied from sphinx
    double logInnerSummation;
    if (logMinuend < logSubtrahend) {
      throw new IllegalArgumentException("Subtraction results in log "
          + "of a negative number: " + logMinuend + " - "
          + logSubtrahend);
    }
    logInnerSummation = 1.0;
    logInnerSummation -= logToLinear(logSubtrahend - logMinuend);
    return logMinuend + linearToLog(logInnerSummation);
  }

  public double getBase() {
    return base;
  }

  public double getLogE() {
    return logE;
  }

  public double getLogOne() {
    return 0;
  }

  public double getLogZero() {
    return -Double.MAX_VALUE;
  }

  public double getLogHalf() {
    return logHalf;
  }

  public double linearToLog(double value) {
    assert !Double.isNaN(value) : "Value " + value;
    assert value >= 0 : "Value " + value;
    if (value == 0) {
      return getLogZero();
    }
    if (MathUtils.isPositiveInfinity(value)) {
      return Double.POSITIVE_INFINITY;
    }
    return Math.log(value) / logBase;
  }

  public double logToLinear(double value) {
    if (MathUtils.isNegativeInfinity(value)) {
      return 0;
    }
    if (MathUtils.isPositiveInfinity(value)) {
      return Double.POSITIVE_INFINITY;
    }

    return Math.exp(logToLn(value));
  }

  public void linearToLog(double[] vector) {
    for (int i = 0; i < vector.length; i++) {
      vector[i] = linearToLog(vector[i]);
    }
  }

  public void logToLinear(double[] vector) {
    for (int i = 0; i < vector.length; i++) {
      vector[i] = logToLinear(vector[i]);
    }
  }

  public double lnToLog(double value) {
    return value / logBase; //ln(base)
  }

  public double logToLn(double value) {
    return value / logE;//log(e)
  }
}
