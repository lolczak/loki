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
import org.speech.asr.recognition.ann.InputBlock;
import org.speech.asr.recognition.ann.TrainPattern;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 31, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class NeuralNetUtils {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(NeuralNetUtils.class.getName());

  public static List<TrainPattern> convertPatterns(double[][] patternArray, int firstOutputIndex) {
    int inputSize = firstOutputIndex;
    int outputSize = patternArray[0].length - firstOutputIndex;
    List<TrainPattern> trainPatterns = new LinkedList();
    for (int i = 0; i < patternArray.length; i++) {
      double[] input = new double[inputSize];
      double[] output = new double[outputSize];
      System.arraycopy(patternArray[i], 0, input, 0, inputSize);
      System.arraycopy(patternArray[i], firstOutputIndex, output, 0, outputSize);
      TrainPattern pattern = new TrainPattern(input, output);

      trainPatterns.add(pattern);
    }

    return trainPatterns;
  }

  public static Map<String, double[][]> createInputArrayMap(List<TrainPattern> trainingPatterns) {
    int noTrainPatterns = trainingPatterns.size();
    Map<String, double[][]> map = new HashMap();
    for (InputBlock block : trainingPatterns.get(0).getInputs()) {
      map.put(block.getId(), new double[noTrainPatterns][]);
    }
    int index = 0;
    for (TrainPattern pattern : trainingPatterns) {
      for (InputBlock block : pattern.getInputs()) {
        double[][] blockArray = map.get(block.getId());
        blockArray[index] = block.getInput();
      }
      index++;
    }
    return map;
  }

  public static double[][] createInputArray(List<TrainPattern> trainingPatterns) {
    double[][] inputArray = new double[trainingPatterns.size()][];
    int index = 0;
    for (TrainPattern pattern : trainingPatterns) {
      InputBlock inputBlock = pattern.getInputs().get(0);//todo
      inputArray[index++] = inputBlock.getInput();
    }
    return inputArray;
  }

  public static double[][] createOutputArray(List<TrainPattern> trainingPatterns) {
    double[][] outputArray = new double[trainingPatterns.size()][];
    int index = 0;
    for (TrainPattern pattern : trainingPatterns) {
      outputArray[index++] = pattern.getDesiredOutput();
    }
    return outputArray;
  }
}
