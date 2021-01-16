/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann.joone;

import org.joone.engine.Layer;
import org.joone.engine.Matrix;
import org.joone.engine.Synapse;
import org.joone.engine.extenders.UpdateWeightExtender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 24, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ParallelWeightUpdater extends UpdateWeightExtender {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ParallelWeightUpdater.class.getName());

  /**
   * The batch size. This variable is mainly used for backward compatibility
   * with the old batch learner who had the method setBatchSize.
   */
  private int theBatchSize = -1; // -1 equals not set and retrieve batch size
  // value from monitor

  /**
   * The number of rows (biases or input neurons to the synapses).
   */
  private int theRows = -1;

  /**
   * The number of columns (output neurons of the synapses),
   */
  private int theColumns = -1;

  /**
   * The matrix in which we save the updates before storing the weights (or biases)
   * to the network itself.
   */
  private Matrix theMatrix;

  /**
   * The counter to check if we have reached batchsize cycles (if so, we need
   * to store the weights).
   */
  private int theCounter = 0;

  private DeltaAccumulator accumulator;

  /**
   * Creates a new instance of BatchModeExtender
   */
  public ParallelWeightUpdater() {
    accumulator = null;
  }

  protected void registerLayer() {
    if (accumulator == null) {
      Layer layer = (Layer) this.getLearner().getLayer();
      accumulator = ParallelNeuralNet.getManager().getLayerAccumulator(layer.getLayerName(), this);
    }
  }

  protected void registerSynapse() {
    if (accumulator == null) {
      Synapse synapse = (Synapse) this.getLearner().getSynapse();
      accumulator = ParallelNeuralNet.getManager().getSynapseAccumulator(synapse.getName(), this);
    }
  }

  public void postBiasUpdate(double[] currentGradientOuts) {
    if (storeWeightsBiases()) {
      registerLayer();
      accumulator.accumulate(theMatrix);
      theMatrix = (Matrix) accumulator.getAccumulated().clone();
      getLearner().getLayer().setBias((Matrix) theMatrix.clone()); // store updated biases
//      theCounter = 0;
    }
  }

  public void postWeightUpdate(double[] currentPattern, double[] currentInps) {
    if (storeWeightsBiases()) {
      registerSynapse();
      accumulator.accumulate(theMatrix);
      theMatrix = (Matrix) accumulator.getAccumulated().clone();
      getLearner().getSynapse().setWeights((Matrix) theMatrix.clone());
//      theCounter = 0;
    }
  }

  public void preBiasUpdate(double[] currentGradientOuts) {
    if (theRows != getLearner().getLayer().getRows()) {
      // dimensions have changed, so better start over
      initiateNewBatch();
    }
    theCounter++;
  }

  public void preWeightUpdate(double[] currentPattern, double[] currentInps) {

    if (theRows != getLearner().getSynapse().getInputDimension() ||
        theColumns != getLearner().getSynapse().getOutputDimension()) {
      initiateNewBatch();
    }
    theCounter++;
  }

  public void updateBias(int i, double aDelta) {
    theMatrix.delta[i][0] += aDelta; // update the delta in our local copy
  }

  public void updateWeight(int j, int k, double aDelta) {
    theMatrix.delta[j][k] += aDelta; // update the delta in our local copy
  }

  /**
   * Resets delta values to zero.
   *
   * @param aMatrix the matrix for which we need to set the delta values to zero.
   */
  protected void resetDelta(Matrix aMatrix) {
    // reset the delta values to 0
    for (int r = 0; r < aMatrix.delta.length; r++) {
      for (int c = 0; c < aMatrix.delta[0].length; c++) {
        aMatrix.delta[r][c] = 0;
      }
    }
  }

  /**
   * Initiates a new batch (at the beginning or when the dimensions change).
   */
  protected void initiateNewBatch() {
    if (getLearner().getLayer() != null) {
      theRows = getLearner().getLayer().getRows();
      theMatrix = (Matrix) getLearner().getLayer().getBias().clone(); // get a copy
    } else if (getLearner().getSynapse() != null) {
      theRows = getLearner().getSynapse().getInputDimension();
      theColumns = getLearner().getSynapse().getOutputDimension();
      theMatrix = (Matrix) getLearner().getSynapse().getWeights().clone(); // get a copy
    }
    resetDelta(theMatrix);
    theCounter = 0;
  }

  /**
   * Sets the batchsize. Used for backward compatibility. Use monitor.setBatchSize()
   * instead.
   *
   * @param aBatchSize the new batchsize.
   * @deprecated use monitor.setBatchSize()
   */
  public void setBatchSize(int aBatchSize) {
    theBatchSize = aBatchSize;
  }

  public Matrix getMatrix() {
    return theMatrix;
  }

  public int getColumns() {
    return theColumns;
  }

  public int getRows() {
    return theRows;
  }

  /**
   * Gets the batchsize. Used for backward compatibility. Use monitor.getBatchSize()
   * instead.
   *
   * @return the batch size.
   * @deprecated use monitor.getBatchSize()
   */
  public int getBatchSize() {
    if (theBatchSize < 0) {
      return getLearner().getMonitor().getBatchSize();
    }
    return theBatchSize;
  }

  public boolean storeWeightsBiases() {
    if (theCounter % getBatchSize() == 0) {
      return true;
    }
    if (theCounter == getLearner().getMonitor().getNumOfPatterns()) {
      theCounter = 0;//todo poprawic tak aby nie kolidowalo z MomentumExtender
      return true;
    }

    return false;
  }
}
