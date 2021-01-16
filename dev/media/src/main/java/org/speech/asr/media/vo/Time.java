/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.media.vo;

/**
 * Value object reprezentujacy czas.
 * <p/>
 * Creation date: May 30, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class Time {

  //                                          nano, micro,mili,sec, min
  private static long[] divisors = new long[]{1000, 1000, 1000, 60, 60};

  private static final long NANO_DIVISOR = 1000;

  private static final long MICRO_DIVISOR = 1000;

  private static final long MILLI_DIVISOR = 1000;

  private static final long SEC_DIVISOR = 60;

  private static final long MIN_DIVISOR = 60;

  private long hours;

  private long minutes;

  private long seconds;

  private long millis;

  private long micros;

  private long nanos;

  private long nanoTime;

  public Time(long nanoTime) {
    this.nanoTime = nanoTime;
    evaluate();
  }

  private void evaluate() {
    long t = nanoTime;
    nanos = t % NANO_DIVISOR;
    t = t / NANO_DIVISOR;
    micros = t % MICRO_DIVISOR;
    t = t / MICRO_DIVISOR;
    millis = t % MILLI_DIVISOR;
    t = t / MILLI_DIVISOR;
    seconds = t % SEC_DIVISOR;
    t = t / SEC_DIVISOR;
    minutes = t % MIN_DIVISOR;
    t = t / MIN_DIVISOR;
    hours = t;
  }

  public long getHours() {
    return hours;
  }

  public long getMicros() {
    return micros;
  }

  public long getMillis() {
    return millis;
  }

  public long getMinutes() {
    return minutes;
  }

  public long getNanos() {
    return nanos;
  }

  public long getSeconds() {
    return seconds;
  }

  /**
   * Getter dla pola 'nanoTime'.
   *
   * @return wartosc pola 'nanoTime'.
   */
  public long getNanoTime() {
    return nanoTime;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Time{");

    sb.append(hours + ":");
    sb.append(minutes + ",");
    sb.append(seconds + "'");
    sb.append(millis + " ");
    sb.append(micros + " ");
    sb.append(nanos);
    sb.append("}");
    return sb.toString();
  }
}
