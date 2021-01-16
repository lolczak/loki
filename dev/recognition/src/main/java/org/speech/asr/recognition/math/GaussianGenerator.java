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
 * //@todo class description
 * <p/>
 * Creation date: Jun 19, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class GaussianGenerator {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(GaussianGenerator.class.getName());

  private Gaussian gaussian;

  private Random baseGenerator;

  public GaussianGenerator(Gaussian gaussian) {
    this.gaussian = gaussian;
    baseGenerator = new Random(System.nanoTime());
  }

  public Gaussian getGaussian() {
    return gaussian;
  }

  public double nextGaussian() {
    double baseRandomVariable = baseGenerator.nextGaussian();

    return baseRandomVariable * gaussian.getDeviation() + gaussian.getMean();
  }
}
