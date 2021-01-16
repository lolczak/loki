/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann.joone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joone.engine.ExtendableLearner;
import org.joone.engine.LearnableLayer;
import org.joone.engine.LearnableSynapse;
import org.joone.engine.extenders.BatchModeExtender;
import org.joone.engine.extenders.MomentumExtender;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 24, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ParallelBatchLearner extends ExtendableLearner {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ParallelBatchLearner.class.getName());

   public ParallelBatchLearner() {
        setUpdateWeightExtender(new ParallelWeightUpdater());
        // please be careful of the order of extenders...
        addDeltaRuleExtender(new MomentumExtender());
    }

    /**
     * @deprecated not used, the BatchModeExtender takes care of everything
     */
    public void initiateNewBatch() {
        // if you want to call it any, probably the next lines are the best...
        if (learnable instanceof LearnableLayer) {
            theUpdateWeightExtender.preBiasUpdate(null);
        } else if (learnable instanceof LearnableSynapse) {
            theUpdateWeightExtender.preWeightUpdate(null, null);
        }
    }

    /**
     * @deprecated use monitor.setBatchSize()
     */
    public void setBatchSize(int newBatchSize) {
        ((BatchModeExtender)theUpdateWeightExtender).setBatchSize(newBatchSize);
    }

    /**
     * @deprecated use monitor.getBatchSize()
     */
    public int getBatchSize() {
        return ((BatchModeExtender)theUpdateWeightExtender).getBatchSize();
    }
}
