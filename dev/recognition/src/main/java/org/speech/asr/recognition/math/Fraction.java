/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.math;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 9, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class Fraction {
  private double numerator;

  private double denominator;

  public Fraction(double numerator, double denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  public double getDenominator() {
    return denominator;
  }

  public void setDenominator(double denominator) {
    this.denominator = denominator;
  }

  public double getNumerator() {
    return numerator;
  }

  public void setNumerator(double numerator) {
    this.numerator = numerator;
  }
}
