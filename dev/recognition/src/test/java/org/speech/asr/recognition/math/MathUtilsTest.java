/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 5, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class MathUtilsTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MathUtilsTest.class.getName());

  @Test
  public void testNegativeInfinityTest() {
    Assert.assertFalse(MathUtils.isNegativeInfinity(-Double.MAX_VALUE));
    Assert.assertFalse(MathUtils.isNegativeInfinity(Double.MAX_VALUE));
    double d = 0.0;
    Assert.assertFalse(MathUtils.isNegativeInfinity(1.0 / d));
    Assert.assertTrue(MathUtils.isNegativeInfinity(-1.0 / d));
    Assert.assertTrue(MathUtils.isNegativeInfinity(Double.NEGATIVE_INFINITY));
    Assert.assertFalse(MathUtils.isNegativeInfinity(-0.0));
    Assert.assertFalse(MathUtils.isNegativeInfinity(+0.0));
    Assert.assertFalse(MathUtils.isNegativeInfinity(Double.POSITIVE_INFINITY));
    Assert.assertFalse(MathUtils.isNegativeInfinity(Double.NaN));
  }

  @Test
  public void testPositiveInfinityTest() {
    Assert.assertFalse(MathUtils.isPositiveInfinity(-Double.MAX_VALUE));
    Assert.assertFalse(MathUtils.isPositiveInfinity(Double.MAX_VALUE));
    double d = 0.0;
    Assert.assertFalse(MathUtils.isPositiveInfinity(-1.0 / d));
    Assert.assertTrue(MathUtils.isPositiveInfinity(1.0 / d));
    Assert.assertTrue(MathUtils.isPositiveInfinity(Double.POSITIVE_INFINITY));
    Assert.assertFalse(MathUtils.isPositiveInfinity(-0.0));
    Assert.assertFalse(MathUtils.isPositiveInfinity(+0.0));
    Assert.assertFalse(MathUtils.isPositiveInfinity(Double.NEGATIVE_INFINITY));
    Assert.assertFalse(MathUtils.isPositiveInfinity(Double.NaN));
  }

  @Test
  public void testSum2() {
    Assert.assertEquals(MathUtils.sum2(12, 14), 26.0);
    Assert.assertEquals(MathUtils.sum2(Double.NEGATIVE_INFINITY, 14), MathUtils.NEGATIVE_INFINITY_VALUE);
    try {
      MathUtils.sum2(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
      Assert.assertTrue(false);
    } catch (AssertionError e) {
      Assert.assertTrue(true);
    }
    Assert.assertEquals(MathUtils.sum2(MathUtils.NEGATIVE_INFINITY_VALUE, MathUtils.NEGATIVE_INFINITY_VALUE),
        MathUtils.NEGATIVE_INFINITY_VALUE);
    Assert.assertEquals(MathUtils.sum2(MathUtils.POSITIVE_INFINITY_VALUE, MathUtils.POSITIVE_INFINITY_VALUE),
        MathUtils.POSITIVE_INFINITY_VALUE);
    Assert.assertEquals(MathUtils.sum2(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY),
        MathUtils.NEGATIVE_INFINITY_VALUE);
    Assert.assertEquals(MathUtils.sum2(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
        MathUtils.POSITIVE_INFINITY_VALUE);
  }
}
