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
 * Creation date: Apr 27, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class NewDictionaryCommand extends AbstractDictionaryCommand {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(NewDictionaryCommand.class.getName());

  protected void doExecuteCommand() {
    DictionaryEntity dictionary = new DictionaryEntity();
    DictionaryPropertiesDialog dialog = DictionaryPropertiesDialog.createNewDialog(dictionary);
    dialog.showDialog();
    if (dialog.getResult() == ConfirmationResult.OK && dictionary != null) {
      String uuid = dictionaryBean.create(dictionary);
      log.info("Created dict {}", uuid);
      DictionaryEvent event = DictionaryEvent.createNewDictionaryEvent(uuid);
      eventDispatcher.dispatchEvent(event);
    }
  }

}
