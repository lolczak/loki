/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.constant.ConfirmationResult;
import org.springframework.richclient.dialog.ConfirmationDialog;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 1, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ConfirmationDialogExt extends ConfirmationDialog {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ConfirmationDialogExt.class.getName());

  private ConfirmationResult result;

  public ConfirmationDialogExt() {
    super();
  }

  public ConfirmationDialogExt(String title, String message) {
    super(title, message);
  }

  protected void onConfirm() {
    result = ConfirmationResult.OK;
  }

  protected void onCancel() {
    super.onCancel();
    result = ConfirmationResult.CANCEL;
  }

  /**
   * Getter dla pola 'result'.
   *
   * @return wartosc pola 'result'.
   */
  public ConfirmationResult getResult() {
    return result;
  }
}
