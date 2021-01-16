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
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.acoustic.FeatureImpl;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.trainer.baumwelch.mock.HmmStateMock;

import java.util.HashMap;
import java.util.Random;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 21, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class BaseBaumWelchTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(BaseBaumWelchTest.class.getName());

  protected static final int NO_STATES = 10;

  protected static final int NO_FEATURES = 100;

  protected static final double MAX_RELATIVE_DIFF = 0.001;

  protected static final int NO_TEST_CASES = 1000;

  protected BaseBaumWelchLearner initLearner() {
    BaseBaumWelchLearner learner = new BaseBaumWelchLearner();
    learner.logScale = AsrContext.getContext().getLogScale();
    learner.matrixA = generateMatrixA();
    learner.stateLookupMap = new HashMap();
    Random random = new Random();
    for (int i = 1; i <= NO_STATES; i++) {
      double mean = i * 10;
      double deviation = 10;
      learner.stateLookupMap.put(i, new HmmStateMock(mean, deviation));
    }
    Feature[] observations = new Feature[NO_FEATURES];
    for (int i = 0; i < NO_FEATURES; i++) {
      observations[i] = new FeatureImpl(random.nextDouble() * 100);
    }
    learner.noStates = NO_STATES;
    learner.observations = observations;
    return learner;
  }


  protected PosterioriHolder forwardBackwardPass(BaseBaumWelchLearner learner) {
    double forwardPosteriori = learner.forwardPass();
    double backwardPosteriori = learner.backwardPass();

    return new PosterioriHolder(forwardPosteriori, backwardPosteriori);
  }

  protected double[][] generateMatrixA() {
    double[][] matrixA = new double[NO_STATES + 2][NO_STATES + 2];
    Random random = new Random();

    for (int i = 1; i <= NO_STATES; i++) {
      for (int j = 1; j <= NO_STATES + 1; j++) {
        matrixA[i][j] = random.nextDouble();
      }
    }

    //normalize
    for (int i = 1; i <= NO_STATES; i++) {
      double sum = 0;
      for (int j = 1; j <= NO_STATES + 1; j++) {
        sum += matrixA[i][j];
      }
      for (int j = 1; j <= NO_STATES + 1; j++) {

        matrixA[i][j] = matrixA[i][j] / sum;
      }
    }
    matrixA[0][1] = 1.0;
    matrixA[NO_STATES][NO_STATES + 1] = 0.5;

    return matrixA;
  }

  protected static class PosterioriHolder {
    double forwardPosteriori;
    double backwardPosteriori;

    PosterioriHolder(double f, double b) {
      forwardPosteriori = f;
      backwardPosteriori = b;
    }
  }
}
