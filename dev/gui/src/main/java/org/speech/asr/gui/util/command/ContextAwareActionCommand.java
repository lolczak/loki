package org.speech.asr.gui.util.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.command.ActionCommand;
import org.speech.asr.gui.util.command.ContextProvider;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 27, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public abstract class ContextAwareActionCommand extends ActionCommand {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ContextAwareActionCommand.class.getName());

  protected ContextProvider contextProvider;


  /**
   * Setter dla pola 'contextProvider'.
   *
   * @param contextProvider wartosc ustawiana dla pola 'contextProvider'.
   */
  public void setContextProvider(ContextProvider contextProvider) {
    this.contextProvider = contextProvider;
  }

}
