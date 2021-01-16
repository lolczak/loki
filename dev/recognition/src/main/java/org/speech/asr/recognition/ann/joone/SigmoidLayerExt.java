/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann.joone;

import org.joone.engine.SigmoidLayer;
import org.joone.exception.JooneRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 27, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SigmoidLayerExt extends SigmoidLayer {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SigmoidLayerExt.class.getName());

  public void backward(double[] pattern)
      throws JooneRuntimeException {
    super.backward(pattern);
    double dw, absv;
    int x;
    int n = getRows();
    for (x = 0; x < n; ++x) {
      gradientOuts[x] = pattern[x] * (outs[x] * (1 - outs[x]));
    }
    myLearner.requestBiasUpdate(gradientOuts);
  }
}
