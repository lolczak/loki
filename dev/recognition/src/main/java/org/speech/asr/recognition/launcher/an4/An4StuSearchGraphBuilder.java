/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.launcher.an4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.PhoneticUnit;
import org.speech.asr.recognition.linguist.StuTranscriptionFactory;
import org.speech.asr.recognition.linguist.TranscriptionFactory;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Sep 1, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class An4StuSearchGraphBuilder extends An4SearchGraphBuilder {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(An4StuSearchGraphBuilder.class.getName());

  protected List<PhoneticUnit> createTranscription(String txt) {
    TranscriptionFactory transcriptionFactory = new StuTranscriptionFactory(model, dictionary);

    List<PhoneticUnit> trans = transcriptionFactory.createTranscription(txt);
    trans.remove(0);
    trans.remove(trans.size() - 1);
    return trans;
  }

}
