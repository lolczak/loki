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
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 20, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ForwardBackwardTest extends BaseBaumWelchTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ForwardBackwardTest.class.getName());


  @Test
  public void testFB() {
    for (int i = 0; i < NO_TEST_CASES; i++) {
      randomTest();
    }
  }

  protected void randomTest() {
    AsrContext ctx = new AsrContext();
    ctx.setLogScale(new SimpleLogScale(1.001));
    AsrContext.setContext(ctx);
    BaseBaumWelchLearner learner = initLearner();
    PosterioriHolder ph = forwardBackwardPass(learner);
    double forwardPosteriori = ph.forwardPosteriori;
    double backwardPosteriori = ph.backwardPosteriori;
    log.debug("Forward posteriori {} backward posteriori {} ", forwardPosteriori, backwardPosteriori);
    double diff = Math.abs(forwardPosteriori - backwardPosteriori);
    double relativeDiff = Math.abs(diff / backwardPosteriori);
    log.debug("Realitve diff: {}", relativeDiff);

    Assert.assertTrue(relativeDiff < MAX_RELATIVE_DIFF);
  }


}
