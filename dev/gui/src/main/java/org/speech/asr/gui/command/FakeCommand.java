/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.command.repo.dictionary.AbstractDictionaryCommand;
import org.speech.asr.gui.widget.progress.ProgressDialog;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 1, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class FakeCommand extends AbstractDictionaryCommand {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(FakeCommand.class.getName());


  protected void doExecuteCommand() {
    new ProgressDialog("progress.title").showDialog();
  }

  public void setCommandId(String cmdId) {
    setId(cmdId);
  }
}
