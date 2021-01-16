/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.application.statusbar.StatusBar;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 7, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class StatusBarLogger {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(StatusBarLogger.class.getName());

  private static StatusBar statusBar;

  public static void init(StatusBar statusBar) {
    log.debug("Initializing status bar");
    StatusBarLogger.statusBar = statusBar;
  }

  public static void log(String message) {
    statusBar.setMessage(message);
  }

  public static StatusBar getStatusBar() {
    return statusBar;
  }
}
