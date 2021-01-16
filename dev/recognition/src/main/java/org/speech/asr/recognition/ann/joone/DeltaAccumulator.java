/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann.joone;

import org.joone.engine.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 25, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class DeltaAccumulator {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DeltaAccumulator.class.getName());

  private int noWorkers;

  private volatile Matrix delta;

  private int rows;

  private int columns;

  public enum DeltaType {
    BIASES, WEIGHTS
  }

  private DeltaType type;

  private List<Matrix> deltas;

  private volatile CountDownLatch getSignal;

  private volatile CountDownLatch putSignal;

  private ReentrantLock putLock;

  private ReentrantLock getLock;

  public DeltaAccumulator(DeltaType type, int rows, int columns, int noWorkers) {
    this.type = type;
    this.rows = rows;
    this.columns = columns;
    this.noWorkers = noWorkers;
    deltas = new LinkedList();
    getSignal = new CountDownLatch(0);
    putSignal = new CountDownLatch(noWorkers);
    putLock = new ReentrantLock();
    getLock = new ReentrantLock();
  }

  public void accumulate(Matrix delta) {
    putLock.lock();
    try {
      //blokada az wszyscy pobiora
      try {
        getSignal.await();
      } catch (InterruptedException e) {
        log.error("", e);
      }
      deltas.add(delta);
      if (deltas.size() == noWorkers) {
        //ostatni triggeruje przeliczanie
        if (type == DeltaType.BIASES) {
          accumulateBiases();
        } else {
          accumulateWeights();
        }
        getSignal = new CountDownLatch(noWorkers);
      }
      putSignal.countDown();
    } finally {
      putLock.unlock();
    }
  }

  public Matrix getAccumulated() {
    //blokada az bedzie przeliczone
    getLock.lock();
    try {
      try {
        putSignal.await();
      } catch (InterruptedException e) {
        log.error("", e);
      }
      Matrix ret = delta;
      if (getSignal.getCount() == 1) {
        putSignal = new CountDownLatch(noWorkers);
        delta = null;
      }
      getSignal.countDown();
      return ret;
    } finally {
      getLock.unlock();
    }
  }

  private void accumulateBiases() {
    Matrix biases = null;
    for (Matrix delta : deltas) {
      if (biases == null) {
        biases = delta;
      } else {
        for (int x = 0; x < rows; ++x) {
          biases.delta[x][0] += delta.delta[x][0]; // adjust bias
        }
      }
    }
    for (int x = 0; x < rows; ++x) {
      biases.value[x][0] += biases.delta[x][0]; // adjust bias
      biases.delta[x][0] = 0.0;
    }
    deltas.clear();
    delta = biases;
  }

  private void accumulateWeights() {
    Matrix weights = null;
    for (Matrix delta : deltas) {
      if (weights == null) {
        weights = delta;
      } else {
        for (int x = 0; x < rows; ++x) {
          for (int y = 0; y < columns; ++y) {
            weights.delta[x][y] += delta.delta[x][y]; // adjust weights
          }
        }
      }
    }
    for (int x = 0; x < rows; ++x) {
      for (int y = 0; y < columns; ++y) {
        weights.value[x][y] += weights.delta[x][y]; // adjust weights
        weights.delta[x][y] = 0.0;
      }
    }
    deltas.clear();
    delta = weights;
  }

}
