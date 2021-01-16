package org.speech.asr.gui.command.repo.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.constant.ConfirmationResult;
import org.speech.asr.common.entity.DictionaryEntity;
import org.speech.asr.gui.event.DictionaryEvent;
import org.speech.asr.gui.view.repo.DictionaryPropertiesDialog;
import org.speech.asr.gui.command.repo.dictionary.AbstractDictionaryCommand;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 29, 2009 <br/>
 *
 * @author Lukasz Olczak
 */
public class EditDictionaryCommand extends AbstractDictionaryCommand {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(EditDictionaryCommand.class.getName());

  protected void doExecuteCommand() {
    DictionaryEntity dictionary = (DictionaryEntity) contextProvider.getContext();
    DictionaryPropertiesDialog dialog = DictionaryPropertiesDialog.createEditDialog(dictionary);
    dialog.showDialog();
    if (dialog.getResult() == ConfirmationResult.OK && dictionary != null) {
      DictionaryEntity updated = dictionaryBean.update(dictionary);
      log.info("Updated dictionary {}", updated);
      DictionaryEvent event = DictionaryEvent.createUpdatedDictionaryEvent(updated.getUuid());
      eventDispatcher.dispatchEvent(event);
    }
  }
}
