/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.view.editor;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 2, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ToolbarDescriptor {

  private String key;

  private List<ButtonDescriptor> buttons;

  public ToolbarDescriptor(String key, List<ButtonDescriptor> buttons) {
    this.key = key;
    this.buttons = buttons;
  }

  public List<ButtonDescriptor> getButtons() {
    return buttons;
  }

  public void setButtons(List<ButtonDescriptor> buttons) {
    this.buttons = buttons;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
}
