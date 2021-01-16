package org.speech.asr.gui.command.repo.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.constant.ConfirmationResult;
import org.speech.asr.common.entity.DictionaryEntity;
import org.speech.asr.gui.event.DictionaryEvent;
import org.speech.asr.gui.util.ConfirmationDialogExt;
import org.speech.asr.gui.command.repo.dictionary.AbstractDictionaryCommand;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 29, 2009 <br/>
 *
 * @author Lukasz Olczak
 */
public class DeleteDictionaryCommand extends AbstractDictionaryCommand {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DeleteDictionaryCommand.class.getName());

  protected void doExecuteCommand() {
    DictionaryEntity dictionary = (DictionaryEntity) contextProvider.getContext();
    ConfirmationDialogExt dialog = new ConfirmationDialogExt();
    dialog.showDialog();

    if (dialog.getResult() == ConfirmationResult.OK && dictionary != null) {
      dictionaryBean.delete(dictionary.getUuid());
      log.info("Dictionary {} deleted", dictionary.getName());
      DictionaryEvent event = DictionaryEvent.createDeleteDictionaryEvent(dictionary.getUuid());
      eventDispatcher.dispatchEvent(event);
    }
  }
}
