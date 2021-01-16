/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.util.dictionary.DictionariesSource;
import org.speech.asr.gui.util.image.ScalingSupportingImageSource;
import org.speech.asr.gui.widget.progress.ProgressDialog;
import org.speech.asr.gui.widget.progress.ProgressWidget;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;

import javax.swing.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 31, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class AppContext {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(AppContext.class.getName());

  private static AppContext instance;

  private DictionariesSource dictionarySource;

  private ScalingSupportingImageSource imageSource;

  private MessageSource messageSource;

  private Application application;

  private ProgressWidget progressWidget;

  public static AppContext getInstance() {
    if (instance == null) {
      instance = new AppContext();
    }

    return instance;
  }

  private AppContext() {
  }

  /**
   * Getter for property 'dictionarySource'.
   *
   * @return Value for property 'dictionarySource'.
   */
  public DictionariesSource getDictionarySource() {
    return dictionarySource;
  }

  /**
   * Setter for property 'dictionarySource'.
   *
   * @param dictionarySource Value to set for property 'dictionarySource'.
   */
  public void setDictionarySource(DictionariesSource dictionarySource) {
    this.dictionarySource = dictionarySource;
  }

  /**
   * Getter for property 'imageSource'.
   *
   * @return Value for property 'imageSource'.
   */
  public ScalingSupportingImageSource getImageSource() {
    return imageSource;
  }

  /**
   * Setter for property 'imageSource'.
   *
   * @param imageSource Value to set for property 'imageSource'.
   */
  public void setImageSource(ScalingSupportingImageSource imageSource) {
    this.imageSource = imageSource;
  }

  /**
   * Getter for property 'messageSource'.
   *
   * @return Value for property 'messageSource'.
   */
  public MessageSource getMessageSource() {
    return messageSource;
  }

  /**
   * Setter for property 'messageSource'.
   *
   * @param messageSource Value to set for property 'messageSource'.
   */
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Getter for property 'application'.
   *
   * @return Value for property 'application'.
   */
  public Application getApplication() {
    return application;
  }

  /**
   * Setter for property 'application'.
   *
   * @param application Value to set for property 'application'.
   */
  public void setApplication(Application application) {
    this.application = application;
  }

  public JFrame getActiveWindow() {
    return application.getActiveWindow().getControl();
  }

  /**
   * Getter for property 'progressWidget'.
   *
   * @return Value for property 'progressWidget'.
   */
  public ProgressWidget getProgressWidget() {
    if (progressWidget == null) {
      progressWidget = new ProgressDialog("progress.title");
    }
    return progressWidget;
  }

  /**
   * Setter for property 'progressWidget'.
   *
   * @param progressWidget Value to set for property 'progressWidget'.
   */
  public void setProgressWidget(ProgressWidget progressWidget) {
    this.progressWidget = progressWidget;
  }
}
