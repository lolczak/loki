/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.command.repo.corpus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.constant.ConfirmationResult;
import org.speech.asr.common.entity.CorpusEntity;
import org.speech.asr.gui.event.CorpusEvent;
import org.speech.asr.gui.view.repo.CorpusPropertiesDialog;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class NewCorpusCommand extends AbstractCorpusCommand {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(NewCorpusCommand.class.getName());

  protected void doExecuteCommand() {
    CorpusEntity corpus = new CorpusEntity();
    CorpusPropertiesDialog dialog = CorpusPropertiesDialog.createNewDialog(corpus);
    dialog.showDialog();
    if (dialog.getResult() == ConfirmationResult.OK && corpus != null) {
      String uuid = corpusBean.create(corpus);
      log.info("Created corpus {}", uuid);
      CorpusEvent event = CorpusEvent.createNewCorpusEvent(uuid);
      eventDispatcher.dispatchEvent(event);
    }
  }
}
