/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.testng.annotations.Configuration;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 11, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class BaseSpringTest extends AbstractDependencyInjectionSpringContextTests {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(BaseSpringTest.class.getName());

  @Configuration(beforeSuite = true, alwaysRun = true)
  public void testNGsetUp() throws Exception {
    super.setUp();
  }

  @Configuration(afterSuite = true, alwaysRun = true)
  public void testNGtearDown() throws Exception {
    super.tearDown();
  }

  protected String[] getConfigLocations() {
    return new String[]{
        "context/jcr-context.xml"
        // "test-context.xml"
    };
  }
}
