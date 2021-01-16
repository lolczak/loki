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
import org.speech.asr.common.event.EventDispatcher;
import org.speech.asr.gui.logic.CorpusBean;
import org.speech.asr.gui.util.command.ContextAwareActionCommand;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public abstract class AbstractCorpusCommand extends ContextAwareActionCommand {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(AbstractCorpusCommand.class.getName());

  protected CorpusBean corpusBean;

  protected EventDispatcher eventDispatcher;

  /**
   * Setter dla pola 'corpusBean'.
   *
   * @param corpusBean wartosc ustawiana dla pola 'corpusBean'.
   */
  public void setCorpusBean(CorpusBean corpusBean) {
    this.corpusBean = corpusBean;
  }

  /**
   * Getter dla pola 'corpusBean'.
   *
   * @return wartosc pola 'corpusBean'.
   */
  public CorpusBean getCorpusBean() {
    return corpusBean;
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
