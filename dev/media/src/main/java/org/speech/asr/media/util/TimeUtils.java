/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.media.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.media.vo.Time;

import javax.media.format.AudioFormat;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 30, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class TimeUtils {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(TimeUtils.class.getName());

  public static final int NANOS_IN_MILLI_SECOND = 1000000;

  public static final int NANOS_IN_SECOND = 1000000000;


  public static String formatTime(Time time) {
    StringBuilder sb = new StringBuilder();
    sb.append(time.getMinutes()).append(":");
    sb.append(time.getSeconds()).append(",");
    sb.append(time.getMillis());

    return sb.toString();
  }

  public static long getTimeInNano(int offset, AudioFormat format) {
    long sampleNo = offset / (format.getSampleSizeInBits() / 8);
    long interval = NANOS_IN_SECOND / (long) format.getSampleRate();
    long duration = sampleNo * interval;
    return duration;
  }

  public static double getTimeInSeconds(int offset, AudioFormat format) {
    double sampleNo = offset / (format.getSampleSizeInBits() / 8);
    double interval = 1 / format.getSampleRate();
    double duration = sampleNo * interval;
    return duration;
  }

  public static int timeToOffset(long time, AudioFormat format) {
    long interval = NANOS_IN_SECOND / (long) format.getSampleRate();
    long index = time / interval;
    index *= (format.getSampleSizeInBits() / 8);
    return (int) index;
  }

  public static long nanosToMillis(long nanoTime) {
    return nanoTime / NANOS_IN_MILLI_SECOND;
  }

  public static long millisToNanos(long milliTime) {
    return milliTime * NANOS_IN_MILLI_SECOND;
  }

  public static javax.media.Time getTime(int offset, AudioFormat format) {
    return new javax.media.Time(getTimeInSeconds(offset, format));
  }

  private TimeUtils() {
  }
}
