/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.baumwelch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.*;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.math.Fraction;
import org.speech.asr.recognition.math.GaussianMixture;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.MathUtils;
import org.speech.asr.recognition.trainer.TrainingItem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Algorytm Bauma-Welcha.
 * Wersja z jednym jednowymiarowym komponentem Gaussa, prawdopodobienstwa w skali liniowej.
 * Dodatkowo wstawiam dwa sztuczne stany 0 i final, a wiec liczba stanow wynosi N+2.
 * <p/>
 * Creation date: Jun 19, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class BaseBaumWelchLearner {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(BaseBaumWelchLearner.class.getName());

  /**
   * Tablica asocjacyjna laczaca indeks stanu z obiektem HmmState.
   */
  Map<Integer, State> stateLookupMap;

  Map<State, Integer> stateReverseLookupMap;

  /**
   * Mapa wprowadzona w celu optymalizacji, indeksy tylko dla stanow <1, noStates>.
   */
  Map<Integer, List<Integer>> transitionFromMap;

  Map<Integer, List<Integer>> transitionToMap;

  /**
   * Prawdopodobienstwo alfa, pierwszy wymiar to numer obserwacji, drugi to indeks stanu hmm.
   */
  double[][] alpha;

  /**
   * Prawdopodobienstwo beta, pierwszy wymiar to numer obserwacji, drugi to indeks stanu hmm.
   */
  double[][] beta;

  /**
   * Liczba trenowanych stanow.
   */
  int noStates;

  /**
   * Macierz prawdopodobienstw przejsc miedzy stanami.
   */
  double[][] matrixA;


  /**
   * Macierz gamma, pierwszy wymiar to numer obserwacji, drugi to indeks stanu hmm, a trzeci to numer komponentu Gaussa.
   */
  double[][][] gamma;

  /**
   * Prawdziwe ksi: t, stan w chwili t, stan w chwili t+1
   */
  double[][][] ksi;

  /**
   * Kolejnosc wymiarow: stan, komponent, wymiar.
   */
  Fraction[][][] mean;

  /**
   * Kolejnosc wymiarow: stan, komponent, wymiar
   */
  Fraction[][][] variance;

  /**
   * Estymatory wag dla komponentow Gaussa.
   * Pierwszy wymiar to stan drugi numer komponentu.
   */
  Fraction[][] logWeights;

  Feature[] observations;

  Feature[] logObservations;

  LogScale logScale;

  int noDimmensions;

  int noMixtures;

  public double train(TrainingItem item, int noActualMixtures) {
    log.trace("Performing full EM step...");
    logScale = AsrContext.getContext().getLogScale();
    observations = item.getObservationSequence();
    logObservations = item.getLogObservationSequence();
    //todo refactor it
    noMixtures = noActualMixtures;
    noDimmensions = observations[0].getData().length;
    createLookupMap(item.getSentenceHmm());
    createMatrixA(item.getSentenceHmm());
    createHelperArrays();
    double posteriori = forwardPass();
    backwardPass();
    estimationStep(posteriori);
    maximizationStep();
    return posteriori;
  }

  public double trainLeftToRight(TrainingItem item, int noActualMixtures) {
    log.trace("Performing full EM step...");
    logScale = AsrContext.getContext().getLogScale();
    observations = item.getObservationSequence();
    logObservations = item.getLogObservationSequence();
    //todo refactor it
    noMixtures = noActualMixtures;
    noDimmensions = observations[0].getData().length;
    createLookupMap(item.getTranscription());
    createMatrixA();
    createHelperArrays();
    double posteriori = forwardPass();
    backwardPass();
    estimationStep(posteriori);
    maximizationStep();
    return posteriori;
  }

  public Map<String, StateEstimates> getEstimates() {
    Map<String, StateEstimates> estimatesMap = new HashMap();
    for (int i = 1; i <= noStates; i++) {
      State state = getState(i);
      estimatesMap.put(state.getId(), new StateEstimates(logWeights[i], mean[i], variance[i], matrixA[i][i]));
    }
    return estimatesMap;
  }

  protected void createLookupMap(Hmm hmm) {
    stateLookupMap = new HashMap();
    stateReverseLookupMap = new HashMap();
    transitionFromMap = new HashMap();
    transitionToMap = new HashMap();
    int index = 1;
    for (HmmState state : hmm.getAllStates()) {
      transitionFromMap.put(index, new LinkedList());
      transitionToMap.put(index, new LinkedList());
      stateLookupMap.put(index, state);
      stateReverseLookupMap.put(state, index++);
    }
    noStates = index - 1;
  }

  protected void createLookupMap(List<PhoneticUnit> transcription) {
    stateLookupMap = new HashMap();
    stateReverseLookupMap = new HashMap();
    transitionFromMap = new HashMap();
    transitionToMap = new HashMap();
    int index = 1;
    for (PhoneticUnit<StateDescriptor> phoneme : transcription) {
      for (StateDescriptor state : phoneme.getStatesSequence()) {
        transitionFromMap.put(index, new LinkedList());
        transitionToMap.put(index, new LinkedList());
        stateLookupMap.put(index, state);
        stateReverseLookupMap.put(state, index++);
      }
    }
    noStates = index - 1;
  }

  protected void createMatrixA() {
    initMatrixA();
    int initialIndex = 1;
    int finalIndex = noStates;

    for (int i = initialIndex; i <= finalIndex; i++) {
      StateDescriptor state = (StateDescriptor) getState(i);
      matrixA[i][i] = state.getLogSelfLoopProbability();
      List<Integer> transitionsForI = transitionFromMap.get(i);
      transitionsForI.add(i);

      List<Integer> transitionsForJ = transitionToMap.get(i);
      transitionsForJ.add(i);
      if (i < finalIndex) {
        matrixA[i][i + 1] = state.getLogSelfLoopProbability();
        transitionsForI = transitionFromMap.get(i);
        transitionsForI.add(i + 1);

        transitionsForJ = transitionToMap.get(i + 1);
        transitionsForJ.add(i);
      }
    }

    matrixA[getInitialState()][initialIndex] = logScale.getLogOne();//zdarzenie pewne
    double prob = logScale.getLogZero();//zdarzenie niemozliwe
    for (int i = 1; i <= noStates; i++) {
      prob = logScale.addAsLinear(prob, matrixA[finalIndex][i]);
    }
    matrixA[finalIndex][getFinalState()] = logScale.subtractAsLinear(logScale.getLogOne(), prob);
  }

  private void initMatrixA() {
    matrixA = new double[getCompleteNoStates()][getCompleteNoStates()];
    double initVal = logScale.getLogZero();
    for (int i = 0; i < getCompleteNoStates(); i++) {
      for (int j = 0; j < getCompleteNoStates(); j++) {
        matrixA[i][j] = initVal;
      }
    }
  }

  protected void createMatrixA(Hmm hmm) {
    initMatrixA();

    for (HmmState state : hmm.getAllStates()) {
      int i = getIndex(state);
      for (HmmArc arc : state.getSuccessors()) {
        int j = getIndex(arc.getNextState());
        matrixA[i][j] = arc.getTransitionProbability();
        List<Integer> transitionsForI = transitionFromMap.get(i);
        transitionsForI.add(j);

        List<Integer> transitionsForJ = transitionToMap.get(j);
        transitionsForJ.add(i);
      }
    }
    HmmState initialState = hmm.getInitialState();
    HmmState finalState = hmm.getFinalState();
    int initialIndex = getIndex(initialState);
    int finalIndex = getIndex(finalState);
    matrixA[getInitialState()][initialIndex] = logScale.getLogOne();//zdarzenie pewne
    double prob = logScale.getLogZero();//zdarzenie niemozliwe
    for (int i = 1; i <= noStates; i++) {
      prob = logScale.addAsLinear(prob, matrixA[finalIndex][i]);
    }
    matrixA[finalIndex][getFinalState()] = logScale.subtractAsLinear(logScale.getLogOne(), prob);
  }

  protected void createHelperArrays() {
    gamma = new double[observations.length][getCompleteNoStates()][noMixtures];
//    ksi = new double[observations.length][getCompleteNoStates()][getCompleteNoStates()];
    mean = new Fraction[getCompleteNoStates()][noMixtures][noDimmensions];
    variance = new Fraction[getCompleteNoStates()][noMixtures][noDimmensions];
    logWeights = new Fraction[getCompleteNoStates()][noMixtures];
  }

  protected void estimationStep(double posteriori) {
    int noObservations = observations.length;
    estimateGamma(posteriori, noObservations);
//    estimateKsi(posteriori, noObservations);
  }

  private void estimateKsi(double posteriori, int noObservations) {
    //est. real ksi
    //eg. 6.37
    for (int t = 0; t < noObservations; t++) {
      for (int i = 1; i <= noStates; i++) {
        List<Integer> jList = new LinkedList(transitionFromMap.get(i));
        jList.add(getFinalState());
        //for (int j = 1; j <= noStates + 1; j++) {
        for (int j : jList) {
          /*
          aby zagwarantowac przejscie do stanu koncowego jest ta konstrukcja if-else
           */
          double bValue = logScale.getLogZero();
          double betaValue = logScale.getLogZero();
          if (t == noObservations - 1) {
            if (i == noStates && j == noStates + 1) {
              bValue = logScale.getLogOne();
              betaValue = logScale.getLogOne();
            }
          } else {
            if (j != noStates + 1) {
              bValue = pdfValue(j, observations[t + 1]);
              betaValue = beta[t + 1][j];
            }
          }
          ksi[t][i][j] = MathUtils.sum(alpha[t][i], matrixA[i][j], bValue, betaValue, -posteriori);
          assert !Double.isNaN(ksi[t][i][j]) : "Value " + ksi[t][i][j];
        }
      }
    }
  }

  private void estimateGamma(double posteriori, int noObservations) {
    //est. gamma
    //eq. 6.42
    for (int t = 0; t < noObservations; t++) {
      for (int j = 1; j <= noStates; j++) {
        //wersja z 1 komponentem gaussa
        //gamma[t][j] = MathUtils.sum(alpha[t][j], beta[t][j], -posteriori);
        double gammmaTJ = MathUtils.sum(alpha[t][j], beta[t][j], -posteriori);
        double denominator = logScale.getLogZero();
        for (int k = 0; k < noMixtures; k++) {
          double logWeight = getLogWeight(j, k);
          double bValue = pdfValue(j, k, observations[t]);
          denominator = logScale.addAsLinear(denominator, MathUtils.sum(logWeight, bValue));
        }
        for (int m = 0; m < noMixtures; m++) {
          double logWeight = getLogWeight(j, m);
          double bValue = pdfValue(j, m, observations[t]);
          double mixtureFactor = MathUtils.sum(logWeight, bValue, -denominator);
          gamma[t][j][m] = gammmaTJ + mixtureFactor;
        }
      }
    }
  }

  protected void maximizationStep() {
//    updateMatrixA();
    reestimateMeans();
    reestimateVariances();
    reestimateWeights();
  }

  private void reestimateWeights() {
    int noObservations = observations.length;
    //eq. 9.41
    //Pierwszy wymiar to stan drugi numer komponentu.
    for (int i = 1; i <= noStates; i++) {
      for (int m = 0; m < noMixtures; m++) {
        double numerator = logScale.getLogZero();
        double denominator = logScale.getLogZero();
        for (int t = 0; t < noObservations; t++) {
          numerator = logScale.addAsLinear(numerator, gamma[t][i][m]);
          for (int k = 0; k < noMixtures; k++) {
            denominator = logScale.addAsLinear(denominator, gamma[t][i][k]);
          }
        }
        logWeights[i][m] = new Fraction(numerator, denominator);
      }
    }
  }

  private void reestimateVariances() {
//    int noObservations = observations.length;
//    //eq. 9.42 (9.30) , Rabiner str 352  eq.6.54
//    //kolejnosc: stan, komponent, wymiar
//    for (int i = 1; i <= noStates; i++) {
//      for (int m = 0; m < noMixtures; m++) {
//        double[] numerator = new double[noDimmensions];
//        for (int d = 0; d < noDimmensions; d++) {
//          numerator[d] = logScale.getLogZero();
//        }
//        double denominator = logScale.getLogZero();
//        for (int t = 0; t < noObservations; t++) {
//          for (int d = 0; d < noDimmensions; d++) {
//            //razy 2 to do kwadratu w sali log
//            double diff = 2.0 * logScale.linearToLog(Math.abs(observations[t].getData()[d] - mean[i][m][d]));
//            numerator[d] = logScale.addAsLinear(numerator[d], gamma[t][i][m] + diff);
//          }
//          denominator = logScale.addAsLinear(denominator, gamma[t][i][m]);
//        }
//        for (int d = 0; d < noDimmensions; d++) {
//          variance[i][m][d] = logScale.logToLinear(0.5 * (numerator[d] - denominator));
//        }
//      }
//    }
  }

  private void reestimateMeans() {
    int noObservations = observations.length;
    //kolejnosc: stan, komponent, wymiar
    //count GMM re-estimates
    //eq. 9.40 (9.29) , Rabiner str 351 , eq.6.53
    for (int i = 1; i <= noStates; i++) {
      for (int m = 0; m < noMixtures; m++) {
        double[] meanNumerator = new double[noDimmensions];
        double[] varNumerator = new double[noDimmensions];
        for (int d = 0; d < noDimmensions; d++) {
          meanNumerator[d] = logScale.getLogZero();
          varNumerator[d] = logScale.getLogZero();
        }
        double denominator = logScale.getLogZero();
        for (int t = 0; t < noObservations; t++) {
          for (int d = 0; d < noDimmensions; d++) {
            //zakladam ze obserwacje sa znormalizowane
//            double logObservation = logScale.linearToLog(observations[t].getData()[d]);
            meanNumerator[d] = logScale.addAsLinear(meanNumerator[d], gamma[t][i][m] + logObservations[t].getData()[d]);

            double sqrObs = 2.0 * logObservations[t].getData()[d];
            varNumerator[d] = logScale.addAsLinear(varNumerator[d], gamma[t][i][m] + sqrObs);
          }
          denominator = logScale.addAsLinear(denominator, gamma[t][i][m]);
        }
        for (int d = 0; d < noDimmensions; d++) {
          variance[i][m][d] = new Fraction(varNumerator[d], denominator);
          mean[i][m][d] = new Fraction(meanNumerator[d], denominator);
//          assert MathUtils.isReal(mean[i][m][d]) : "Numerator=" + numerator[d] + ", denominator=" + denominator;
        }
      }
    }
  }

  private void updateMatrixA() {
    int noObservations = observations.length;
    //update matrix A
    //eq. 6.38
    for (int i = 1; i <= noStates; i++) {
      //count matrixA denominator
      double denominator = logScale.getLogZero();
      for (int t = 0; t < noObservations; t++) {
        List<Integer> jbisList = new LinkedList(transitionFromMap.get(i));
        jbisList.add(getFinalState());
        // for (int jbis = 1; jbis <= noStates + 1; jbis++) {       
        for (int jbis : jbisList) {
          denominator = logScale.addAsLinear(denominator, ksi[t][i][jbis]);
        }
      }
//      for (int j = 1; j <= noStates; j++) {
      for (int j : transitionFromMap.get(i)) {
        //count numerator
        double numerator = logScale.getLogZero();
        for (int t = 0; t < noObservations; t++) {
          numerator = logScale.addAsLinear(numerator, ksi[t][i][j]);
        }
        double newVal = numerator - denominator;
        assert MathUtils.isReal(newVal) : "Value " + newVal;
        matrixA[i][j] = newVal;
      }
    }
  }


  protected double forwardPass() {
    int noObservations = observations.length;
    alpha = new double[noObservations][noStates + 2];//plus dwa sztuczne stany
    //init alpha step
    //eq. 6.15
    for (int j = 1; j <= noStates; j++) {
      alpha[0][j] = MathUtils.sum(matrixA[0][j], pdfValue(j, observations[0]));
    }
    //recursion step
    //eq. 6.16
    for (int t = 1; t < noObservations; t++) {
      for (int j = 1; j <= noStates; j++) {
        double sum = logScale.getLogZero();
//        for (int i = 1; i <= noStates; i++) {
        for (int i : transitionToMap.get(j)) {
          double tempAlpha = alpha[t - 1][i];
          double tempA = matrixA[i][j];
          double tempB = pdfValue(j, observations[t]);
          double multi = MathUtils.sum(tempAlpha, tempA, tempB);
          assert MathUtils.isReal(tempAlpha) : "Value " + tempAlpha;
          assert MathUtils.isReal(tempA) : "Value " + tempA;
          assert MathUtils.isReal(tempB) : "Value " + tempB;
          assert MathUtils.isReal(multi) : "Value " + multi;
          sum = logScale.addAsLinear(sum, multi);
          assert MathUtils.isReal(sum) : "Value " + sum;
        }
        alpha[t][j] = sum;
      }
    }
    //eg. 6.17
    double posteriori = logScale.getLogZero();
    for (int i = 1; i <= noStates; i++) {
      posteriori =
          logScale.addAsLinear(posteriori, MathUtils.sum(alpha[noObservations - 1][i], matrixA[i][getFinalState()]));
    }
    alpha[noObservations - 1][getFinalState()] = posteriori;
    return posteriori;
  }

  protected double backwardPass() {
    int noObservations = observations.length;
    beta = new double[noObservations][noStates + 2];
    //initialization
    //eq. 6.28
    for (int i = 1; i <= noStates; i++) {
      beta[noObservations - 1][i] = matrixA[i][getFinalState()];
    }

    //recursion
    //eq. 6.29
    for (int t = noObservations - 2; t >= 0; t--) {
      for (int i = 1; i <= noStates; i++) {
        double sum = logScale.getLogZero();
//        for (int j = 1; j <= noStates; j++) {
        for (int j : transitionFromMap.get(i)) {
          double tempBeta = beta[t + 1][j];
          double tempA = matrixA[i][j];
          double tempB = pdfValue(j, observations[t + 1]);
          double multi = MathUtils.sum(tempBeta, tempA, tempB);
          assert MathUtils.isReal(tempBeta) : "Value " + tempBeta;
          assert MathUtils.isReal(tempA) : "Value " + tempA;
          assert MathUtils.isReal(tempB) : "Value " + tempB;
          assert MathUtils.isReal(multi) : "Value " + multi;
          sum = logScale.addAsLinear(sum, multi);
        }
        beta[t][i] = sum;
      }
    }

    double posteriori = logScale.getLogZero();

    for (int j = 1; j <= noStates; j++) {
      posteriori =
          logScale.addAsLinear(posteriori, MathUtils.sum(matrixA[0][j], pdfValue(j, observations[0]), beta[0][j]));
    }
    beta[0][0] = posteriori;
    return posteriori;
  }

  protected State getState(int index) {
    assert stateLookupMap.containsKey(index) : "Value " + index;
    return stateLookupMap.get(index);
  }

  protected int getIndex(State state) {
    assert stateReverseLookupMap.containsKey(state) : "Value " + state;
    return stateReverseLookupMap.get(state);
  }

  protected double pdfValue(int index, Feature observation) {
    State state = getState(index);
    return state.getScorer().getValue(observation);
  }

  protected double pdfValue(int index, int mixture, Feature observation) {
    State state = getState(index);
    GaussianMixture gmm = (GaussianMixture) state.getScorer();
    return gmm.getMixtureComponents()[mixture].getValue(observation);
  }

  protected double getLogWeight(int index, int mixture) {
    State state = getState(index);
    GaussianMixture gmm = (GaussianMixture) state.getScorer();
    return gmm.getLogWeights()[mixture];
  }

  //todo refactor name for Fake
  protected int getInitialState() {
    return 0;
  }

  protected int getFinalState() {
    return noStates + 1;
  }

  protected int getCompleteNoStates() {
    return noStates + 2;
  }

}
