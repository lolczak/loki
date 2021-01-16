/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 17, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class MatrixUtils {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MatrixUtils.class.getName());

  public static double[] copyVector(double[] toCopy) {
    double[] newVector = new double[toCopy.length];
    System.arraycopy(toCopy, 0, newVector, 0, toCopy.length);
    return newVector;
  }

  public static String vectorToString(double[] vector) {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (double val : vector) {
      sb.append(val).append(",");
    }
    sb.append("]");
    return sb.toString();
  }

  public static double sum(double[] vector) {
    double sum = 0.0;
    for (double val : vector) {
      sum += val;
    }

    return sum;
  }

  public static void multiply(double[] vector, double scalar) {
    for (int i = 0; i < vector.length; i++) {
      vector[i] = vector[i] * scalar;
    }
  }

  public static void translate(double[] vector, double scalar) {
    for (int i = 0; i < vector.length; i++) {
      vector[i] = vector[i] + scalar;
    }
  }
}
