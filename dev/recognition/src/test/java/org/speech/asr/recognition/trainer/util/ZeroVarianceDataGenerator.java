/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.math.RandomGenerator;
import org.speech.asr.recognition.trainer.baumwelch.vo.Symbol;
import org.speech.asr.recognition.util.MatrixUtils;

import java.util.HashMap;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 14, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ZeroVarianceDataGenerator extends SyntheticDataGenerator {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ZeroVarianceDataGenerator.class.getName());

  public void generateSymbols() {
    double[] deviationVector = new double[noDimensions];
    for (int d = 0; d < noDimensions; d++) {
      deviationVector[d] = 0.0;
    }
    symbols = new HashMap();
    double lastMean = 0;
    for (int s = 0; s < noSymbols; s++) {
      lastMean += avgObservationDistance;
      Symbol symbol = new Symbol();
      symbol.setName(String.valueOf(s + 1));
      double[] meanVector = new double[noDimensions];
      for (int d = 0; d < noDimensions; d++) {
        double mean = lastMean;
        meanVector[d] = mean;
      }
      double[] mean = MatrixUtils.copyVector(meanVector);
      RandomGenerator generator = new ConstRandomGenerator(mean);
      symbol.setGenerator(generator);
      symbols.put(symbol.getName(), symbol);
    }
  }

  private class ConstRandomGenerator implements RandomGenerator {
    private double[] mean;

    public ConstRandomGenerator(double[] mean) {
      this.mean = mean;
    }

    public double[] nextRandom() {
      return mean;
    }
  }

}
