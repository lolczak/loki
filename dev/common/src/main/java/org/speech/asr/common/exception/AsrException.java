/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 8, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class AsrException extends Exception {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(AsrException.class.getName());

  public AsrException() {
    super();
  }

  public AsrException(Throwable cause) {
    super(cause);
  }

  public AsrException(String message, Throwable cause) {
    super(message, cause);
  }

  public AsrException(String message) {
    super(message);    
  }
}
