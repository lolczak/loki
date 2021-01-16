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
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.util.MatrixUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 17, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class GaussianMixtureGenerator implements RandomGenerator {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(GaussianMixtureGenerator.class.getName());

  private Random baseGenerator;

  private List<Interval> intervals;

  private MultivariateGaussianGenerator[] generators;

  public GaussianMixtureGenerator(GaussianMixture gmm) {
    baseGenerator = new Random();
    LogScale logScale = AsrContext.getContext().getLogScale();
    double[] weights = new double[gmm.getLogWeights().length];
    for (int i = 0; i < weights.length; i++) {
      weights[i] = logScale.logToLinear(gmm.getLogWeights()[i]);
    }
    createHelperArray(weights);
    generators = new MultivariateGaussianGenerator[weights.length];
    for (int i = 0; i < weights.length; i++) {
      generators[i] = new MultivariateGaussianGenerator(gmm.getMixtureComponents()[i]);
    }
  }

  public double[] nextRandom() {
    int index = drawMixtureComponent();

    return generators[index].nextRandom();
  }

  private int drawMixtureComponent() {
    double point = baseGenerator.nextDouble();
    Iterator<Interval> iterator = intervals.iterator();
    Interval interval = iterator.next();

    while (iterator.hasNext() && !interval.contains(point)) {
      interval = iterator.next();
    }
    assert interval.contains(point) : "Value " + point;
    return interval.getId();
  }

  private void createHelperArray(double[] weights) {
    double sum = MatrixUtils.sum(weights);
    double point = 0.0;
    intervals = new LinkedList();

    int index = 0;
    for (double weight : weights) {
      double normalizedWeight = weight / sum;
      Interval weightInterval = new Interval(index++, point, normalizedWeight);
      intervals.add(weightInterval);
      point += normalizedWeight;
    }
  }

  private class Interval {
    private double begin;
    private double end;

    private int id;

    public Interval(int id, double begin, double distance) {
      this.id = id;
      this.begin = begin;
      this.end = begin + distance;
    }

    public boolean contains(double val) {
      if (val >= begin && val < end) {
        return true;
      }
      return false;
    }

    public double getBegin() {
      return begin;
    }

    public int getId() {
      return id;
    }

    public double getEnd() {
      return end;
    }
  }
}
