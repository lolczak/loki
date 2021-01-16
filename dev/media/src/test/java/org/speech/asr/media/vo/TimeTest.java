/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.media.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 31, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class TimeTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(TimeTest.class.getName());

  @Test
  public void test1() {
    long nanoTime = 19215011014730l;
    Time t = new Time(nanoTime);
    Assert.assertEquals(5, t.getHours());
    Assert.assertEquals(20, t.getMinutes());
    Assert.assertEquals(15, t.getSeconds());
    Assert.assertEquals(11, t.getMillis());
    Assert.assertEquals(14, t.getMicros());
    Assert.assertEquals(730, t.getNanos());
  }

  @Test
  public void test2() {
    long nanoTime = 36453050013l;
    Time t = new Time(nanoTime);
    Assert.assertEquals(0, t.getHours());
    Assert.assertEquals(0, t.getMinutes());
    Assert.assertEquals(36, t.getSeconds());
    Assert.assertEquals(453, t.getMillis());
    Assert.assertEquals(50, t.getMicros());
    Assert.assertEquals(13, t.getNanos());
  }
}
