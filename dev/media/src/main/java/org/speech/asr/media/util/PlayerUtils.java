/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.media.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.Player;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 9, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class PlayerUtils {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(PlayerUtils.class.getName());

  public static void waitForState(Player player, int state) {
    while (player.getState() < state) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        log.error("Error during sleeping thread", e);
      }
    }
  }
}
