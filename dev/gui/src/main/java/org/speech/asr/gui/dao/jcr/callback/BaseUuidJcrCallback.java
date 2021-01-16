/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.dao.jcr.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springmodules.jcr.JcrCallback;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 1, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public abstract class BaseUuidJcrCallback implements JcrCallback {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(BaseUuidJcrCallback.class.getName());

  protected String uuid;

  public BaseUuidJcrCallback(String uuid) {
    this.uuid = uuid;
  }

  /**
   * Getter dla pola 'uuid'.
   *
   * @return wartosc pola 'uuid'.
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * Setter dla pola 'uuid'.
   *
   * @param uuid wartosc ustawiana dla pola 'uuid'.
   */
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
}
