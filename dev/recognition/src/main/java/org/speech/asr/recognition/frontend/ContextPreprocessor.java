/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.acoustic.FeatureImpl;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 31, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ContextPreprocessor {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ContextPreprocessor.class.getName());

  private int n;
  private static final int MFCC_VECTOR_SIZE = 39;

  public ContextPreprocessor(int n) {
    this.n = n;
  }

  public Feature process(List<Feature> window) {
    assert window.size() == 2 * n + 1 : "Window size " + window.size();
    List<Feature> leftContext = window.subList(0, n);
    Feature center = window.get(n);
    List<Feature> rightContext = window.subList(n + 1, 2 * n + 1);
    double[] vect = new double[(2 * n + 1) * MFCC_VECTOR_SIZE];
    copyContext(vect, 0, leftContext);
    System.arraycopy(center.getData(), 0, vect, n * MFCC_VECTOR_SIZE, MFCC_VECTOR_SIZE);
    copyContext(vect, (n + 1) * MFCC_VECTOR_SIZE, rightContext);
    FeatureImpl feature = new FeatureImpl(vect);
    feature.setSequenceNumber(center.getSequenceNumber());
    return feature;
  }

  protected void copyContext(double[] vect, int offset, List<Feature> context) {
    int index = offset;
    //E
    for (int i = 0; i < n; i++) {
      vect[index++] = context.get(i).getData()[0];
    }
    //CEP
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < 12; j++) {
        vect[index++] = context.get(i).getData()[1 + j];
      }
    }

    //dE
    for (int i = 0; i < n; i++) {
      vect[index++] = context.get(i).getData()[13];
    }
    //dCEP
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < 12; j++) {
        vect[index++] = context.get(i).getData()[14 + j];
      }
    }

    //ddE
    for (int i = 0; i < n; i++) {
      vect[index++] = context.get(i).getData()[26];
    }
    //ddCEP
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < 12; j++) {
        vect[index++] = context.get(i).getData()[27 + j];
      }
    }

  }
}
