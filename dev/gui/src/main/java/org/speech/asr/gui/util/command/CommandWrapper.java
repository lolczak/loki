/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.util.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.closure.Closure;
import org.springframework.richclient.command.ActionCommand;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 3, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class CommandWrapper extends ActionCommand {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(CommandWrapper.class.getName());

  private Closure closure;

  public CommandWrapper(Closure closure) {
    this.closure = closure;
  }

  protected void doExecuteCommand() {
    closure.call(getParameters());
  }
}
