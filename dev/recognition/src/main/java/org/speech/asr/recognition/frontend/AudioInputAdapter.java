/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.frontend;

import edu.cmu.sphinx.frontend.*;
import edu.cmu.sphinx.util.props.Configurable;
import edu.cmu.sphinx.util.props.PropertyException;
import edu.cmu.sphinx.util.props.PropertySheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 5, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class AudioInputAdapter implements Configurable, DataProcessor, AudioConsumer {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(AudioInputAdapter.class.getName());

  /**
   * Const.
   */
  private static final double KILO = 1000.0;

  /**
   * Component name.
   */
  private static String COMPONENT_NAME = "AudioInputAdapter";

  /**
   * FIFO buffer for audio samples.
   */
  protected Queue<Data> samplesQueue;

  /**
   * Is sample first in stream - indicate begin of streaming.
   */
  private boolean isFirst;

  /**
   * Is stream closed.
   */
  private boolean end;

  /**
   * Start timestamp of signal.
   */
  private long startTimestamp;

  /**
   * End timestamp of signal.
   */
  private long endTimestamp;

  /**
   * Time stamp of last mark.
   */
  private long lastMarkTimestamp;

  /**
   * Constructor.
   */
  public AudioInputAdapter() {
    isFirst = true;
    end = false;
  }

  /**
   * Gets name of component.
   *
   * @return name of component.
   */
  public String getName() {
    return COMPONENT_NAME;
  }

  public void newProperties(PropertySheet ps) throws PropertyException {
//TODO To change body of implemented methods use File | Settings | File Templates.
  }

  /**
   * Gets data for next component in pipeline.
   *
   * @return data object.
   * @throws edu.cmu.sphinx.frontend.DataProcessingException
   *
   */
  public synchronized Data getData() throws DataProcessingException {
    Data result = null;
    if (end) {
      result = getEndSignal();
    } else {
      result = getDataFromQueue();
    }
    return result;
  }

  /**
   * Gets data form buffer (samples).
   *
   * @return
   */
  protected Data getDataFromQueue() {
    Data result = null;
    do {
      result = samplesQueue.poll();
      if (result == null) {
        try {
          wait();
        } catch (InterruptedException e) {
          log.error("Error while invoking wait() function.", e);
        }
        if (end) {
          result = getEndSignal();
        }
      }
    } while (result == null && !end);
    return result;
  }

  /**
   * Gets end signal.
   *
   * @return end signal.
   */
  private Data getEndSignal() {
    final long duration = endTimestamp - startTimestamp;
    log.debug("Sending end signal event ({}).", new Date(endTimestamp));
    return new DataEndSignal(duration);
  }

  /**
   * Gets predecessor object.
   *
   * @return predecessor object.
   */
  public DataProcessor getPredecessor() {
    return null;
  }

  /**
   * Initialize component.
   */
  public void initialize() {
    samplesQueue = new ConcurrentLinkedQueue<Data>();
  }

  /**
   * Sets predecessor component.
   *
   * @param predecessor
   */
  public void setPredecessor(DataProcessor predecessor) {
    //this.predecessor = predecessor;
  }

  /**
   * Assumption - all chunks all aligned to 1ms
   *
   * @param samples
   */
  public synchronized void put(AudioPayload samples, boolean addMarkAfter) {
    if (isFirst) {
      isFirst = false;
      startTimestamp = samples.getTimestamp();
      lastMarkTimestamp = samples.getTimestamp();
      log.debug("Sending start signal event ({}).", new Date(startTimestamp));
      samplesQueue.offer(new DataStartSignal((int) startTimestamp));
    }
    endTimestamp = samples.getTimestamp();
    endTimestamp += evaluateInterval(samples.getSamples().length, samples.getSamplingRate());

    double[] values = intsToDoubles(samples.getSamples());
    Data result = new DoubleData(values, samples.getSamplingRate(), samples.getTimestamp(), samples.getOffset());

    samplesQueue.offer(result);
    if (addMarkAfter) {
      addSpeechSeparator();
    }
    notifyAll();
  }

  /**
   * Evaluate interval in millis.
   *
   * @param noSamples
   * @param samplingRate
   * @return
   */
  private long evaluateInterval(int noSamples, int samplingRate) {
    return (long) (KILO * (double) noSamples / (double) samplingRate);
  }

  /**
   * Add mark for search manager in order to finish recognition.
   */
  public synchronized void addSpeechSeparator() {
    DataEndSignal des;
    DataStartSignal dss;

    des = new DataEndSignal(endTimestamp - lastMarkTimestamp);
    dss = new DataStartSignal((int) endTimestamp);

    lastMarkTimestamp = endTimestamp;
    samplesQueue.offer(des);
    samplesQueue.offer(dss);
    notifyAll();
  }

  /**
   * Assumption - all chunks all aligned to 1ms
   *
   * @param samples
   */
  public void put(AudioPayload samples) {
    put(samples, false);
  }

  /**
   * Stops stream.
   */
  public synchronized void stop() {
    end = true;
    notifyAll();
  }

  /**
   * Converts int array to double array.
   *
   * @param intTab - int array.
   * @return double array.
   */
  private double[] intsToDoubles(int[] intTab) {
    double[] doubleTab = new double[intTab.length];

    for (int i = 0; i < intTab.length; i++) {
      doubleTab[i] = (double) intTab[i];
    }

    return doubleTab;
  }
}