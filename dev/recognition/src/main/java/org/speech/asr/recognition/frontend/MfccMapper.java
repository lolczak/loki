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

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 27, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class MfccMapper extends BaseInputMapper {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MfccMapper.class.getName());

  public MfccMapper() {
    addMapping(0, 0, "E");
    addMapping(1, 12, "CEP");
    addMapping(13, 13, "dE");
    addMapping(14, 25, "dCEP");
    addMapping(26, 26, "ddE");
    addMapping(27, 38, "ddCEP");
  }

}
