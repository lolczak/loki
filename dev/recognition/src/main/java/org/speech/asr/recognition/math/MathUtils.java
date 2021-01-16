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

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 28, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class MathUtils {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MathUtils.class.getName());

  public static final double NEGATIVE_INFINITY_VALUE = -Double.MAX_VALUE;

  public static final double POSITIVE_INFINITY_VALUE = Double.MAX_VALUE;

  private static final double RELATIVE_DIFF = 1E-10;

  public static double sqr(double value) {
    double result = value * value;
    assert result >= 0 : "Value " + result;
    return result;
  }

  public static boolean compare(double v1, double v2) {
    return compare(v1, v2, RELATIVE_DIFF);
  }

  public static boolean compare(double v1, double v2, double relativeDiff) {
    double diff = Math.abs(v1 - v2);
    if (v1 == 0 && v2 == 0) {
      return true;
    }
    double d;
    d = v1;
    if (v1 == 0) {
      d = v2;
    }
    return (diff / d) < relativeDiff;
  }

  public static boolean isPositiveInfinity(double val) {
    if (val == Double.POSITIVE_INFINITY) {
      return true;
    }
    return false;
  }

  public static boolean isNegativeInfinity(double val) {
    if (val == Double.NEGATIVE_INFINITY) {
      return true;
    }
    return false;
  }

  public static boolean isReal(double val) {
    if (Double.isInfinite(val) || Double.isNaN(val)) {
      return false;
    }

    return true;
  }

  public static double sum(double val1, double... values) {
    double sum = val1;
    for (double val : values) {
      if (val != 0.0) {
        sum = sum2(sum, val);
      }
    }
    return sum;
  }

  /**
   * Oblicza sume dwoch parametrow uwzgledniajac plus i minus nieskonczonosc.
   * Rezultatem funkcji w przypadku gdy std. operacja zrwaca nieskonczonosc jest NEGATIVE_INFINITY_VALUE lub
   * POSITIVE_INFINITY_VALUE.
   *
   * @param val1
   * @param val2
   * @return
   */
  static double sum2(double val1, double val2) {
    double result = val1 + val2;
    if (isReal(result)) {
      return result;
    }
    assert !Double.isNaN(val1) : "Value " + val1;
    assert !Double.isNaN(val2) : "Value " + val2;
    assert !((isPositiveInfinity(val1) && isNegativeInfinity(val2)) ||
        (isPositiveInfinity(val2) && isNegativeInfinity(val1))) : "Values " + val1 + " , " + val2;
    if (isPositiveInfinity(result)) {
      return POSITIVE_INFINITY_VALUE;
    }
    if (isNegativeInfinity(result)) {
      return NEGATIVE_INFINITY_VALUE;
    }
    if (Double.isNaN(result)) {
      if (Double.isInfinite(val1)) {
        return convert(val1);
      }
      if (Double.isInfinite(val2)) {
        return convert(val2);
      }
    }
    assert false;
    return Double.NaN;
  }

  public static double convert(double val) {
    assert !Double.isNaN(val) : "Value " + val;

    if (isPositiveInfinity(val)) {
      return POSITIVE_INFINITY_VALUE;
    }
    if (isNegativeInfinity(val)) {
      return NEGATIVE_INFINITY_VALUE;
    }

    return val;
  }
}
