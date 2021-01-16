/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.acoustic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.math.Fraction;
import org.speech.asr.recognition.math.GaussianMixture;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.MultivariateGaussian;
import org.speech.asr.recognition.trainer.baumwelch.StateEstimates;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 18, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class EstimatesAccumulator {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(EstimatesAccumulator.class.getName());

  private static final double DEFAULT_DEVIATION_FLOOR = 1E-300;

  private List<Fraction[]> logWeightsEstimates;

  private List<Fraction[]> selfProbEstimates;

  private Map<Integer, ComponentEstimates> estimatesMap;

  private int noMixtures;

  private double deviationFloor;

  private String stateId;

  public EstimatesAccumulator(String stateId) {
    this.stateId = stateId;
    estimatesMap = new HashMap();
    logWeightsEstimates = new LinkedList();
    selfProbEstimates = new LinkedList();
    if (AsrContext.getContext().getDeviationFloor() == null) {
      deviationFloor = DEFAULT_DEVIATION_FLOOR;
    } else {
      deviationFloor = AsrContext.getContext().getDeviationFloor();
    }
    deviationFloor = AsrContext.getContext().getLogScale().linearToLog(deviationFloor);
  }

  private double[] evaluateAvg(List<Fraction[]> estimates) {
    int vectorDimension = estimates.get(0).length;
    double[] numerator = new double[vectorDimension];
    double[] denominator = new double[vectorDimension];
    for (Fraction[] el : estimates) {
      for (int d = 0; d < vectorDimension; d++) {
        numerator[d] += el[d].getNumerator();
        denominator[d] += el[d].getDenominator();
      }
    }
    double[] avg = new double[vectorDimension];
    for (int d = 0; d < vectorDimension; d++) {
      avg[d] = numerator[d] / denominator[d];
    }
    return avg;
  }

  private double[] evaluateAvgInLogScale(List<Fraction[]> estimates) {
    if (estimates.size() == 0) {
      throw new IllegalArgumentException("List is empty");
    }
    int vectorDimension = estimates.get(0).length;
    LogScale logScale = AsrContext.getContext().getLogScale();
    double[] numerator = new double[vectorDimension];
    double[] denominator = new double[vectorDimension];
    for (int d = 0; d < vectorDimension; d++) {
      numerator[d] = logScale.getLogZero();
      denominator[d] = logScale.getLogZero();
    }
    for (Fraction[] el : estimates) {
      for (int d = 0; d < vectorDimension; d++) {
        numerator[d] = logScale.addAsLinear(numerator[d], el[d].getNumerator());
        denominator[d] = logScale.addAsLinear(denominator[d], el[d].getDenominator());
      }
    }
    double[] avg = new double[vectorDimension];
    for (int d = 0; d < vectorDimension; d++) {
      avg[d] = numerator[d] - denominator[d];
    }
    return avg;
  }

  public double getSelfLogProb() {
    double[] selfLog = evaluateAvgInLogScale(selfProbEstimates);
    selfProbEstimates.clear();
    return selfLog[0];
  }

  public GaussianMixture getNormalizedGmm() {
    LogScale logScale = AsrContext.getContext().getLogScale();
    noMixtures = logWeightsEstimates.get(0).length;
    double[] logWeights = evaluateAvgInLogScale(logWeightsEstimates);
    logWeightsEstimates.clear();
    MultivariateGaussian[] mixtureComponents = new MultivariateGaussian[noMixtures];
    for (int m = 0; m < noMixtures; m++) {
      ComponentEstimates comp = getComponentEstimates(m);
      double[] mean = comp.getAvgMean();
      double[] deviation = comp.getAvgSquareObs();
      for (int i = 0; i < deviation.length; i++) {
        //D^2(X)=E(X^2)-(E(x))^2; w skali liniowej jest:Math.sqrt(deviation[i] - MathUtils.sqr(mean[i]));
        double logMinuend = deviation[i];
        double logSubtrahend = 2.0 * mean[i];
        if (logMinuend <= logSubtrahend) {
          log.warn("Some deviation near 0");
          log.debug(
              "Variance for state {} mixture {} dimension {} is near or below 0, maybe you have too less data for all" +
                  " states and mixtures, logMinuend={}, logSubtrahend={}",
              new Object[]{stateId, m, i, logMinuend, logSubtrahend});
          deviation[i] = deviationFloor;
        } else {
          deviation[i] = 0.5 * logScale.subtractAsLinear(logMinuend, logSubtrahend);
          if (deviation[i] < deviationFloor) {
            log.warn("Some deviation lesser than floor");
            log.debug("Deviation lesser than floor for state {} mixture {} dimension {}", new Object[]{stateId, m, i});
            deviation[i] = deviationFloor;
          }
        }
      }
      logScale.logToLinear(mean);
      logScale.logToLinear(deviation);
      assert mean.length == deviation.length : "Values " + mean.length + " , " + deviation.length;
      mixtureComponents[m] = new MultivariateGaussian(logScale, mean, deviation);
    }

    return new GaussianMixture(logScale, mixtureComponents, logWeights);
  }

  public synchronized void collectStateEstimates(StateEstimates estimates) {
    LogScale logScale = AsrContext.getContext().getLogScale();
    Fraction[] logWeights = estimates.getLogWeights();
    logWeightsEstimates.add(logWeights);
    selfProbEstimates.add(new Fraction[]{new Fraction(estimates.getLogSelfProbability(), logScale.getLogOne())});
    for (int m = 0; m < logWeights.length; m++) {
      ComponentEstimates comp = getComponentEstimates(m);
      comp.collectMeanEstimates(estimates.getMean()[m]);
      comp.collectSquareObsEstimates(estimates.getSquareObs()[m]);
    }
  }

  private ComponentEstimates getComponentEstimates(int index) {
    if (!estimatesMap.containsKey(index)) {
      estimatesMap.put(index, new ComponentEstimates());
    }
    return estimatesMap.get(index);
  }

  private class ComponentEstimates {
    List<Fraction[]> meanEstimates;

    List<Fraction[]> squareObsEstimates;

    public ComponentEstimates() {
      meanEstimates = new LinkedList();
      squareObsEstimates = new LinkedList();
    }

    public void collectMeanEstimates(Fraction[] meanEst) {
      meanEstimates.add(meanEst);
    }

    public void collectSquareObsEstimates(Fraction[] deviationEst) {
      squareObsEstimates.add(deviationEst);
    }

    public double[] getAvgMean() {
      double[] avg = evaluateAvgInLogScale(meanEstimates);
      meanEstimates.clear();
      return avg;
    }

    public double[] getAvgSquareObs() {
      double[] avg = evaluateAvgInLogScale(squareObsEstimates);
      squareObsEstimates.clear();
      return avg;
    }
  }
}
