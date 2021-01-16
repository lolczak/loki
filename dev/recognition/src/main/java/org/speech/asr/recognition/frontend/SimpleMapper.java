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

import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 27, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SimpleMapper implements InputMapper {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SimpleMapper.class.getName());

  public List<InputBlock> mapInput(double[] input) {
    InputBlock defBlock = new InputBlock();
    defBlock.setId("input");
    defBlock.setInput(input);
    List<InputBlock> inputs = new LinkedList();
    inputs.add(defBlock);
    return inputs;
  }
}
