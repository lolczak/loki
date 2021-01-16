/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.AcousticModel;
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.acoustic.FeatureImpl;
import org.speech.asr.recognition.acoustic.PhoneticUnit;
import org.speech.asr.recognition.math.*;
import org.speech.asr.recognition.trainer.TrainSentence;
import org.speech.asr.recognition.trainer.baumwelch.vo.Symbol;
import org.speech.asr.recognition.util.MatrixUtils;

import java.util.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 2, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SyntheticDataGenerator implements DataGenerator {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SyntheticDataGenerator.class.getName());

  protected Map<String, Symbol> symbols;

  protected int noSymbols;

  protected double avgObservationDistance;

  protected double avgObservationDeviation;

  protected int noDimensions;

  protected int noMixtures;

  protected Random generator = new Random(13);

  protected LogScale logScale;

  public List<List<Double>> generateBuckets() {
    List<List<Double>> means = new ArrayList(noDimensions);
    for (int d = 0; d < noDimensions; d++) {
      List<Double> component = new ArrayList(noSymbols);
      double mean = 10 * avgObservationDistance;
      for (int s = 0; s < noSymbols; s++) {
        component.add(mean);
        mean += avgObservationDistance;
      }
      means.add(component);
    }
    return means;
  }

  public void generateSymbols() {
    List<List<Double>> meanBucket = generateBuckets();
    double[] deviationVector = new double[noDimensions];
    for (int d = 0; d < noDimensions; d++) {
      deviationVector[d] = avgObservationDeviation;
    }
    symbols = new HashMap();
    for (int s = 0; s < noSymbols; s++) {
      Symbol symbol = new Symbol();
      symbol.setName(String.valueOf(s + 1));
      double[] meanVector = new double[noDimensions];
      for (int d = 0; d < noDimensions; d++) {
        int index = generator.nextInt(noSymbols - s);
        double mean = meanBucket.get(d).remove(index);
        meanVector[d] = mean;
      }
//      MultivariateGaussian gaussian = new MultivariateGaussian(logScale, meanVector, deviationVector);
      double[] deviation = MatrixUtils.copyVector(deviationVector);
      MatrixUtils.multiply(deviation, 0.175);
      double[] mean = MatrixUtils.copyVector(meanVector);
      MultivariateGaussian middleGaussian = new MultivariateGaussian(logScale, mean, deviation);
      mean = MatrixUtils.copyVector(meanVector);
      MatrixUtils.translate(mean, -0.25 * avgObservationDistance);
      MultivariateGaussian leftGaussian = new MultivariateGaussian(logScale, mean, deviation);
      mean = MatrixUtils.copyVector(meanVector);
      MatrixUtils.translate(mean, 0.25 * avgObservationDistance);
      MultivariateGaussian rightGaussian = new MultivariateGaussian(logScale, mean, deviation);
      double[] logWeights =
          new double[]{logScale.linearToLog(0.333), logScale.linearToLog(0.333), logScale.linearToLog(0.333)};

      GaussianMixture gmm = new GaussianMixture(logScale,
          new MultivariateGaussian[]{middleGaussian, leftGaussian, rightGaussian}, logWeights);
      GaussianMixtureGenerator generator = new GaussianMixtureGenerator(gmm);
      symbol.setGenerator(generator);
      symbols.put(symbol.getName(), symbol);
    }
  }


  public List<TrainSentence> generateSentenceSet(int noSentences, int sentenceLength, int avgFeatureLength,
                                                 int featureLengthDeviation, AcousticModel model) {
    List<TrainSentence> sentenceSet = new LinkedList();
    for (int i = 0; i < noSentences; i++) {
      TrainSentence sentence = new TrainSentence();
      List<PhoneticUnit> transcription = new LinkedList();
      List<Feature> observations = new LinkedList();
      List<Symbol> symbolSequence = new LinkedList();
      //todo refactor it
      long sequenceNumber = 0;
      for (int t = 0; t < sentenceLength; t++) {
        Symbol symbol;
        Symbol last = symbolSequence.isEmpty() ? null : symbolSequence.get(symbolSequence.size() - 1);
        do {
          symbol = randomSymbol();
        } while (last != null && symbolSequence.contains(symbol));
        symbolSequence.add(symbol);
        PhoneticUnit phoneme = model.getPhoneticUnit(symbol.getName());
        assert phoneme != null : "Value " + phoneme;
        transcription.add(phoneme);
        int n = avgFeatureLength + generator.nextInt(featureLengthDeviation);
        RandomGenerator sg = symbol.getGenerator();
        for (int j = 0; j < n; j++) {
          double[] d = sg.nextRandom();
          FeatureImpl f = new FeatureImpl(d);
          f.setSequenceNumber(sequenceNumber++);
          observations.add(f);
        }
      }
      sentence.setTranscription(transcription);
      sentence.setObservations(observations);
      sentenceSet.add(sentence);
    }
    return sentenceSet;
  }

//  public List<SegmentedTrainingItem> generateSegmentedSet(int noSentences, int sentenceLength, int avgFeatureLength,
//                                                          int featureLengthDeviation, NeuralHybridAcousticModel model) {
//    List<SegmentedTrainingItem> sentenceSet = new LinkedList();
//    long sequenceNumber = 0;
//    for (int i = 0; i < noSentences; i++) {
//      SegmentedTrainingItem sentence = new SegmentedTrainingItem();
//      List<PhoneticUnit> transcription = new LinkedList();
//      List<FrameAlignment> alignment = new LinkedList();
//      List<Feature> observations = new LinkedList();
//      List<Symbol> symbolSequence = new LinkedList();
//      //todo refactor it
//      for (int t = 0; t < sentenceLength; t++) {
//        Symbol symbol;
//        Symbol last = symbolSequence.isEmpty() ? null : symbolSequence.get(symbolSequence.size() - 1);
//        do {
//          symbol = randomSymbol();
//        } while (last != null && symbolSequence.contains(symbol));
//        symbolSequence.add(symbol);
//        PhoneticUnit<NeuralStateDescriptor> phoneme = model.getPhoneticUnit(symbol.getName());
//        transcription.add(phoneme);
//        assert phoneme != null : "Value " + phoneme;
//        int n = avgFeatureLength + generator.nextInt(featureLengthDeviation);
//        RandomGenerator sg = symbol.getGenerator();
//        assert phoneme.getStatesSequence().size() == 1 : "Value " + phoneme.getStatesSequence().size();
//        NeuralStateDescriptor state = phoneme.getStatesSequence().get(0);
//        for (int j = 0; j < n; j++) {
//          double[] d = sg.nextRandom();
//          FeatureImpl f = new FeatureImpl(d);
//          f.setSequenceNumber(sequenceNumber);
//          observations.add(f);
//          FrameAlignment fa = new FrameAlignment(state.getId(), sequenceNumber++);
//          alignment.add(fa);
//        }
//      }
//      sentence.setTranscription(transcription);
//      sentence.setFrameAlignments(alignment);
//      sentence.setObservationSequence(observations);
//      sentenceSet.add(sentence);
//    }
//    return sentenceSet;
//  }

  public Symbol randomSymbol() {
    int nextSymbol = generator.nextInt(noSymbols) + 1;
    return symbols.get(String.valueOf(nextSymbol));
  }

  public double getAvgObservationDeviation() {
    return avgObservationDeviation;
  }

  public void setAvgObservationDeviation(double avgObservationDeviation) {
    this.avgObservationDeviation = avgObservationDeviation;
  }

  public double getAvgObservationDistance() {
    return avgObservationDistance;
  }

  public void setAvgObservationDistance(double avgObservationDistance) {
    this.avgObservationDistance = avgObservationDistance;
  }

  public LogScale getLogScale() {
    return logScale;
  }

  public void setLogScale(LogScale logScale) {
    this.logScale = logScale;
  }

  public int getNoDimensions() {
    return noDimensions;
  }

  public void setNoDimensions(int noDimensions) {
    this.noDimensions = noDimensions;
  }

  public int getNoMixtures() {
    return noMixtures;
  }

  public void setNoMixtures(int noMixtures) {
    this.noMixtures = noMixtures;
  }

  public int getNoSymbols() {
    return noSymbols;
  }

  public void setNoSymbols(int noSymbols) {
    this.noSymbols = noSymbols;
  }

  public Map<String, Symbol> getSymbols() {
    return symbols;
  }

  public void setSymbols(Map<String, Symbol> symbols) {
    this.symbols = symbols;
  }
}
