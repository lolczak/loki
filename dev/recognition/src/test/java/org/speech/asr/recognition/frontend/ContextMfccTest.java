/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.ann.InputBlock;
import org.testng.annotations.Test;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 31, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ContextMfccTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ContextMfccTest.class.getName());

  @Test
  public void basicTest() {
    int n = 3;
    ContextMfccMapper mfccMapper = new ContextMfccMapper(n);
    double[] feat = new double[(2 * n + 1) * 39];
    for (int i = 0; i < feat.length; i++) {
      feat[i] = i;
    }

    List<InputBlock> blocks = mfccMapper.mapInput(feat);
    for (InputBlock block : blocks) {
      log.info("Block {} have values {}", block.getId(), block.getInput().length);
    }
  }
}
