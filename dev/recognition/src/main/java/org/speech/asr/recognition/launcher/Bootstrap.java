/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 11, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class Bootstrap {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(Bootstrap.class.getName());

  public static void main(String[] args) {
    ClassPathXmlApplicationContext springContext =
        new ClassPathXmlApplicationContext(args);

  }
}
