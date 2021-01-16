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

import java.util.concurrent.ThreadFactory;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 24, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class NiceThreadFactory implements ThreadFactory {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(NiceThreadFactory.class.getName());

  public Thread newThread(Runnable r) {
    log.info("Creating new thread");
    Thread thread = new Thread(r);
    thread.setDaemon(true);
    thread.setPriority(Thread.MIN_PRIORITY);
    thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
      public void uncaughtException(Thread t, Throwable e) {
        log.error("{} {}", t, e);
      }
    });
    return thread;
  }
}
