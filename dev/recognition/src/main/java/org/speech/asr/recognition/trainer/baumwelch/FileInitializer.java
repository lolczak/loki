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
import org.speech.asr.recognition.acoustic.AcousticModel;
import org.speech.asr.recognition.acoustic.GmmAcousticModel;
import org.speech.asr.recognition.acoustic.StateDescriptor;
import org.speech.asr.recognition.trainer.TrainSentence;

import java.io.File;
import java.util.Collection;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 14, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class FileInitializer implements ModelInitializer {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(FileInitializer.class.getName());

  private File previousModelFile;


  public void initializeStates(Collection<TrainSentence> trainingSet, GmmAcousticModel model) {
    GmmAcousticModel previousModel;
    //TODO To change body of implemented methods use File | Settings | File Templates.
  }
}
