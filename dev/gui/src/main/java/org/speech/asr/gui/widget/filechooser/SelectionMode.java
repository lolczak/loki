/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.widget.filechooser;

import javax.swing.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public enum SelectionMode {
  
  FILES_ONLY(JFileChooser.FILES_ONLY),
  DIRECTORIES_ONLY(JFileChooser.DIRECTORIES_ONLY),
  FILES_AND_DIRECTORIES(JFileChooser.FILES_AND_DIRECTORIES);

  private int mode;

  SelectionMode(int mode) {
    this.mode = mode;
  }

  public int getMode() {
    return mode;
  }
}
