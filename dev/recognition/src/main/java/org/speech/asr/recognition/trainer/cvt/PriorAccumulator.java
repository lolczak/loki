/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.cvt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.exception.AsrRuntimeException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 31, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class PriorAccumulator {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(PriorAccumulator.class.getName());

  private int totalNoSamples;

  private Map<String, Integer> priorMap;

  public PriorAccumulator() {
    totalNoSamples = 0;
    priorMap = new HashMap();
  }

  public double getPrior(String classId) {
    Integer count = priorMap.get(classId);
    if (count == null) {
      throw new AsrRuntimeException("There is no estimates for " + classId);
    }
    return count / (double) totalNoSamples;
  }

  public int getCount(String classId) {
    Integer count = priorMap.get(classId);
    if (count == null) {
      throw new AsrRuntimeException("There is no estimates for " + classId);
    }
    return count;
  }

  public List<String> getClassIds() {
    return new LinkedList(priorMap.keySet());
  }

  public synchronized void collect(String classId, int noSamples) {
    totalNoSamples += noSamples;
    Integer count = priorMap.get(classId);
    if (count == null) {
      priorMap.put(classId, noSamples);
    } else {
      priorMap.put(classId, count + noSamples);
    }
  }

  public void clear() {
    totalNoSamples = 0;
    priorMap.clear();
  }

  public int getTotalNoSamples() {
    return totalNoSamples;
  }
}
