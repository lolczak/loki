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
import org.speech.asr.gui.context.AppContext;
import org.speech.asr.common.entity.CorpusEntity;
import org.speech.asr.gui.event.CorpusEvent;
import org.speech.asr.gui.util.ConfirmationDialogExt;
import org.speech.asr.gui.widget.progress.ProgressWidget;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 14, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class DeleteCorpusCommand extends AbstractCorpusCommand {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DeleteCorpusCommand.class.getName());

  protected void doExecuteCommand() {
    final CorpusEntity corpus = (CorpusEntity) contextProvider.getContext();
    ConfirmationDialogExt dialog = new ConfirmationDialogExt();
    dialog.showDialog();

    if (dialog.getResult() == ConfirmationResult.OK && corpus != null) {
      new Thread(new Runnable() {
        public void run() {
          performLogic(corpus);
        }
      }).start();

    }
  }

  private void performLogic(CorpusEntity corpus) {
    ProgressWidget progress = AppContext.getInstance().getProgressWidget();
    progress.start();
    progress.setStatus("Deleting corpus...");
    corpusBean.delete(corpus.getUuid());
    progress.stop();
    log.info("Corpus {} deleted", corpus.getName());
    CorpusEvent event = CorpusEvent.createDeleteCorpusEvent(corpus.getUuid());
    eventDispatcher.dispatchEvent(event);
  }

}
