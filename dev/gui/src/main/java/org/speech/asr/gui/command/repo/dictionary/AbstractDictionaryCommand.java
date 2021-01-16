package org.speech.asr.gui.command.repo.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.event.EventDispatcher;
import org.speech.asr.gui.logic.DictionaryBean;
import org.speech.asr.gui.util.command.ContextAwareActionCommand;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 29, 2009 <br/>
 *
 * @author Lukasz Olczak
 */
public abstract class AbstractDictionaryCommand extends ContextAwareActionCommand {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(AbstractDictionaryCommand.class.getName());

  protected DictionaryBean dictionaryBean;

  protected EventDispatcher eventDispatcher;

  /**
   * Getter dla pola 'dictionaryBean'.
   *
   * @return wartosc pola 'dictionaryBean'.
   */
  public DictionaryBean getDictionaryBean() {
    return dictionaryBean;
  }

  /**
   * Setter dla pola 'dictionaryBean'.
   *
   * @param dictionaryBean wartosc ustawiana dla pola 'dictionaryBean'.
   */
  public void setDictionaryBean(DictionaryBean dictionaryBean) {
    this.dictionaryBean = dictionaryBean;
  }

  /**
   * Getter dla pola 'eventDispatcher'.
   *
   * @return wartosc pola 'eventDispatcher'.
   */
  public EventDispatcher getEventDispatcher() {
    return eventDispatcher;
  }

  /**
   * Setter dla pola 'eventDispatcher'.
   *
   * @param eventDispatcher wartosc ustawiana dla pola 'eventDispatcher'.
   */
  public void setEventDispatcher(EventDispatcher eventDispatcher) {
    this.eventDispatcher = eventDispatcher;
  }
}
