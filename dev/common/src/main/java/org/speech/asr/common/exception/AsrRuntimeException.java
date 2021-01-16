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
 * Creation date: May 10, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class AsrRuntimeException extends RuntimeException {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(AsrRuntimeException.class.getName());

  public AsrRuntimeException() {
    super();
  }

  public AsrRuntimeException(Throwable cause) {
    super(cause);
  }

  public AsrRuntimeException(String message) {
    super(message);
  }

  public AsrRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}
