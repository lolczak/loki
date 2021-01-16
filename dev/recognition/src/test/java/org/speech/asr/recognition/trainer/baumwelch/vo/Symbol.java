/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.baumwelch.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.math.MultivariateGaussianGenerator;
import org.speech.asr.recognition.math.GaussianMixtureGenerator;
import org.speech.asr.recognition.math.RandomGenerator;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 22, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class Symbol {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(Symbol.class.getName());

  private String name;

  private RandomGenerator generator;

  public RandomGenerator getGenerator() {
    return generator;
  }

  public void setGenerator(RandomGenerator generator) {
    this.generator = generator;
  }

  /**
   * Getter for property 'symbol'.
   *
   * @return Value for property 'symbol'.
   */
  public String getName() {
    return name;
  }

  /**
   * Setter for property 'symbol'.
   *
   * @param name Value to set for property 'symbol'.
   */
  public void setName(String name) {
    this.name = name;
  }
}
