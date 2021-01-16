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
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.math.SimpleLogScale;
import org.testng.annotations.Test;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 21, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ConvergenceTest extends BaseBaumWelchTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ConvergenceTest.class.getName());

  private static final int MAX_ITERATIONS = 1000;

  private static final double MIN_RELATIVE_CHANGE = 1.0E-4;

  private double lastProb = 0.0;

  @Test
  public void testEMStep() {
    AsrContext ctx = new AsrContext();
    ctx.setLogScale(new SimpleLogScale(1.001));
    AsrContext.setContext(ctx);
    BaseBaumWelchLearner learner = initLearner();
    learner.createHelperArrays();
    for (int i = 0; i < MAX_ITERATIONS; i++) {
      PosterioriHolder ph = forwardBackwardPass(learner);
      double forwardPosteriori = ph.forwardPosteriori;
      double backwardPosteriori = ph.backwardPosteriori;
      learner.estimationStep(forwardPosteriori);
      learner.maximizationStep();
      if (i > 0) {
        double relativeDiff = Math.abs((forwardPosteriori - lastProb) / lastProb);
        log.debug("Relative change {}", relativeDiff);
        if (relativeDiff < MIN_RELATIVE_CHANGE) {
          log.debug("Convergence obtained after {} iterations", i);
          break;
        }
      }
      log.debug("Posteriori prob.: {}", forwardPosteriori);
//      Assert.assertTrue(forwardPosteriori > lastProb);
      lastProb = forwardPosteriori;

    }
  }
}
