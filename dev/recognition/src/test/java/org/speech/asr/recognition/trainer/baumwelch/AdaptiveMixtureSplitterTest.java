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
import org.speech.asr.recognition.acoustic.GmmAcousticModel;
import org.speech.asr.recognition.acoustic.Phoneme;
import org.speech.asr.recognition.acoustic.PhoneticUnit;
import org.speech.asr.recognition.acoustic.StateDescriptor;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.math.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 18, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class AdaptiveMixtureSplitterTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(AdaptiveMixtureSplitterTest.class.getName());

  @Test
  public void splitAllTest() {
    LogScale logScale = new SimpleLogScale(1.0001);
    AsrContext asrContext = new AsrContext();
    asrContext.setLogScale(logScale);
    AsrContext.setContext(asrContext);
    GmmAcousticModel model = new GmmAcousticModel(1,1);
    Phoneme<StateDescriptor> testPhoneme = new Phoneme("test", 1);
    MultivariateGaussian comp1 = new MultivariateGaussian(logScale, 5, 2);
    MultivariateGaussian comp2 = new MultivariateGaussian(logScale, 10, 3);
    GaussianMixture gmm = new GaussianMixture(logScale, new MultivariateGaussian[]{comp1, comp2},
        new double[]{logScale.getLogHalf(), logScale.getLogHalf()});
    testPhoneme.getStatesSequence().get(0).setScorer(gmm);
    model.addPhoneticUnit(testPhoneme);
    AdaptiveMixtureSplitter splitter = new AdaptiveMixtureSplitter(model, 8);
    splitter.split();

    Assert.assertEquals(4, splitter.getNoMixtures());
    PhoneticUnit<StateDescriptor> unit = model.getPhoneticUnit("test");
    Assert.assertEquals(1, unit.getStatesSequence().size());
    GaussianMixture splitGmm = (GaussianMixture) unit.getStatesSequence().get(0).getScorer();
    Assert.assertEquals(4, splitGmm.getLogWeights().length);
    Assert.assertTrue(MathUtils.compare(logScale.linearToLog(0.25), splitGmm.getLogWeights()[0]));
    //todo doklandiejsze testy
  }


}
