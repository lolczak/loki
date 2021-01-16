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

/**
 * Liniowa wartosc funkcji Gaussa.
 * <p/>
 * Creation date: Jun 19, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class Gaussian implements Pdf<Feature> {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(Gaussian.class.getName());

  private double mean;

  private double deviation;

  private double factor1;

  private double factor2;

  public Gaussian(double mean, double deviation) {
    this.mean = mean;
    this.deviation = deviation;
    factor1 = 2 * deviation * deviation;
    factor2 = 1 / Math.sqrt(Math.PI * factor1);
  }

  public double getValue(Feature o) {
    double x = o.getData()[0];
    //eq. 9.23
    return factor2 * Math.exp(-MathUtils.sqr(x - mean) / factor1);
  }

  public double getDeviation() {
    return deviation;
  }

  public double getMean() {
    return mean;
  }


  public String toString() {
    return "Gaussian{" +
        "mean=" + mean +
        ", deviation=" + deviation +
        '}';
  }
}
