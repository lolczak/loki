/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.baumwelch;

import org.speech.asr.recognition.math.Fraction;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 14, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class StateEstimates {

  private Fraction[] logWeights;

  //mixture, dimension
  private Fraction[][] mean;

  //mixture, dimension
  private Fraction[][] squareObs;

  private double logSelfProbability;

  public StateEstimates(Fraction[] logWeights, Fraction[][] mean, Fraction[][] deviation, double logSelfProbability) {
    this.logWeights = logWeights;
    this.mean = mean;
    this.squareObs = deviation;
    this.logSelfProbability = logSelfProbability;
  }

  public Fraction[][] getSquareObs() {
    return squareObs;
  }

  public void setSquareObs(Fraction[][] squareObs) {
    this.squareObs = squareObs;
  }

  public double getLogSelfProbability() {
    return logSelfProbability;
  }

  public void setLogSelfProbability(double logSelfProbability) {
    this.logSelfProbability = logSelfProbability;
  }

  public Fraction[] getLogWeights() {
    return logWeights;
  }

  public void setLogWeights(Fraction[] logWeights) {
    this.logWeights = logWeights;
  }

  public Fraction[][] getMean() {
    return mean;
  }

  public void setMean(Fraction[][] mean) {
    this.mean = mean;
  }
}
