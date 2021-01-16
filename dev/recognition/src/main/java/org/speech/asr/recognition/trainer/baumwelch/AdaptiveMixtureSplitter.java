/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.baumwelch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.AcousticModel;
import org.speech.asr.recognition.acoustic.StateDescriptor;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.math.GaussianMixture;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.MultivariateGaussian;
import org.speech.asr.recognition.util.MatrixUtils;

import java.util.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 18, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class AdaptiveMixtureSplitter implements MixtureSplitter {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(AdaptiveMixtureSplitter.class.getName());

  private static final double TRANSITION_FACTOR = 0.2;

  private AcousticModel<StateDescriptor> acousticModel;

  /**
   * Aktualna liczba komponentow Gaussa.
   */
  private int noMixtures;

  /**
   * Pozadana liczba komponentow Gaussa.
   */
  private int desiredNoMixtures;

  public AdaptiveMixtureSplitter(AcousticModel<StateDescriptor> acousticModel, int desiredNoMixtures) {
    this.acousticModel = acousticModel;
    this.desiredNoMixtures = desiredNoMixtures;
    StateDescriptor exampleState = acousticModel.getAllStates().get(0);
    GaussianMixture gmm = (GaussianMixture) exampleState.getScorer();
    noMixtures = gmm.getNoMixtures();
    log.info("Found {} no mixtures, desired no mixtures is {}", noMixtures, desiredNoMixtures);
  }

  public void split() {
    int noSplits;
    if (2 * noMixtures <= desiredNoMixtures) {
      noSplits = noMixtures;
    } else {
      noSplits = desiredNoMixtures - noMixtures;
    }
    log.info("Performing splitting noMixtures {} number to split {}...", noMixtures, noSplits);
    for (StateDescriptor state : acousticModel.getAllStates()) {
      split(state, noSplits);
    }
    noMixtures += noSplits;
  }

  protected void split(StateDescriptor state, int noSplits) {
    log.debug("Splitting {} mixtures of state {}", noSplits, state);
    GaussianMixture gmm = (GaussianMixture) state.getScorer();
    List<WeightHolder> weightList = new ArrayList(gmm.getNoMixtures());
    for (int m = 0; m < gmm.getNoMixtures(); m++) {
      weightList.add(new WeightHolder(m, gmm.getLogWeights()[m]));
    }
    Collections.sort(weightList, new WeightComparator());
    List<Double> newLogWeights = new LinkedList();
    List<MultivariateGaussian> newMixtures = new LinkedList();
    for (int m = 0; m < gmm.getNoMixtures(); m++) {
      int index = weightList.get(m).getIndex();
      if (m < noSplits) {
        MultivariateGaussian[] split = splitMixture(gmm.getMixtureComponents()[index]);
        assert split.length == 2 : "Value " + split.length;
        double newLogWeight = gmm.getLogWeights()[index] - AsrContext.getContext().getLogScale().linearToLog(2.0);
        newMixtures.add(split[0]);
        newLogWeights.add(newLogWeight);
        newMixtures.add(split[1]);
        newLogWeights.add(newLogWeight);
      } else {
        newMixtures.add(gmm.getMixtureComponents()[index]);
        newLogWeights.add(gmm.getLogWeights()[index]);
      }
    }
    assert newLogWeights.size() == newMixtures.size() : "Value " + newLogWeights.size() + "," + newMixtures.size();
    double[] logWeights = new double[newLogWeights.size()];
    MultivariateGaussian[] components = new MultivariateGaussian[newMixtures.size()];
    for (int m = 0; m < logWeights.length; m++) {
      logWeights[m] = newLogWeights.get(m);
      components[m] = newMixtures.get(m);
    }
    GaussianMixture newGmm = new GaussianMixture(AsrContext.getContext().getLogScale(), components, logWeights);
    state.setScorer(newGmm);
  }

  protected MultivariateGaussian[] splitMixture(MultivariateGaussian toSplit) {
    LogScale logScale = AsrContext.getContext().getLogScale();

    MultivariateGaussian leftMixture = new MultivariateGaussian(logScale, MatrixUtils.copyVector(toSplit.getMean()),
        MatrixUtils.copyVector(toSplit.getDeviation()));
    MultivariateGaussian rightMixture = new MultivariateGaussian(logScale, MatrixUtils.copyVector(toSplit.getMean()),
        MatrixUtils.copyVector(toSplit.getDeviation()));

    int noDimensions = toSplit.getMean().length;
    //todo refactor it, extract method
    for (int d = 0; d < noDimensions; d++) {
      leftMixture.getMean()[d] = leftMixture.getMean()[d] - TRANSITION_FACTOR * leftMixture.getDeviation()[d];
      rightMixture.getMean()[d] = rightMixture.getMean()[d] + TRANSITION_FACTOR * rightMixture.getDeviation()[d];
    }

    return new MultivariateGaussian[]{leftMixture, rightMixture};
  }

  public int getNoMixtures() {
    return noMixtures;
  }

  private class WeightHolder {
    private double weight;

    private int index;

    public WeightHolder(int index, double weight) {
      this.index = index;
      this.weight = weight;
    }

    public int getIndex() {
      return index;
    }

    public void setIndex(int index) {
      this.index = index;
    }

    public double getWeight() {
      return weight;
    }

    public void setWeight(double weight) {
      this.weight = weight;
    }
  }

  private class WeightComparator implements Comparator<WeightHolder> {
    public int compare(WeightHolder o1, WeightHolder o2) {
      if (o1.getWeight() == o2.getWeight())
        return 0;
      if (o1.getWeight() < o2.getWeight()) {
        return 1;
      } else {
        return -1;
      }
    }
  }
}
