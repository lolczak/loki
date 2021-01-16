/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.view.editor;

import org.springframework.core.closure.Closure;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 2, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ButtonDescriptor {

  private Closure closure;

  private String key;

  public ButtonDescriptor(String key, Closure closure) {
    this.closure = closure;
    this.key = key;
  }

  public Closure getClosure() {
    return closure;
  }

  public void setClosure(Closure closure) {
    this.closure = closure;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
}
