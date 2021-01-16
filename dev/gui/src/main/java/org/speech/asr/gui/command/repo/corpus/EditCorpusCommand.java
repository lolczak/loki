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
 * Creation date: May 14, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class EditCorpusCommand extends AbstractCorpusCommand {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(EditCorpusCommand.class.getName());

  protected void doExecuteCommand() {
    CorpusEntity corpus = (CorpusEntity) contextProvider.getContext();
    CorpusPropertiesDialog dialog = CorpusPropertiesDialog.createEditDialog(corpus);
    dialog.showDialog();
    if (dialog.getResult() == ConfirmationResult.OK && corpus != null) {
      CorpusEntity updated = corpusBean.update(corpus);
      log.info("Updated corpus {}", updated);
      CorpusEvent event = CorpusEvent.createUpdatedCorpusEvent(updated.getUuid());
      eventDispatcher.dispatchEvent(event);
    }
  }
}
