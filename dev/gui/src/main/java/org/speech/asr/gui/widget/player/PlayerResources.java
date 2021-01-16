/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.widget.player;

import javax.swing.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 19, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class PlayerResources {

  private Icon playIcon;

  private Icon stopIcon;

  private Icon pauseIcon;

  private Icon recordIcon;

  private String timeText;

  private String timeOfText;

  /**
   * Getter dla pola 'timeOfText'.
   *
   * @return wartosc pola 'timeOfText'.
   */
  public String getTimeOfText() {
    return timeOfText;
  }

  /**
   * Setter dla pola 'timeOfText'.
   *
   * @param timeOfText wartosc ustawiana dla pola 'timeOfText'.
   */
  public void setTimeOfText(String timeOfText) {
    this.timeOfText = timeOfText;
  }

  /**
   * Getter dla pola 'timeText'.
   *
   * @return wartosc pola 'timeText'.
   */
  public String getTimeText() {
    return timeText;
  }

  /**
   * Setter dla pola 'timeText'.
   *
   * @param timeText wartosc ustawiana dla pola 'timeText'.
   */
  public void setTimeText(String timeText) {
    this.timeText = timeText;
  }

  /**
   * Getter dla pola 'pauseIcon'.
   *
   * @return wartosc pola 'pauseIcon'.
   */
  public Icon getPauseIcon() {
    return pauseIcon;
  }

  /**
   * Setter dla pola 'pauseIcon'.
   *
   * @param pauseIcon wartosc ustawiana dla pola 'pauseIcon'.
   */
  public void setPauseIcon(Icon pauseIcon) {
    this.pauseIcon = pauseIcon;
  }

  /**
   * Getter dla pola 'playIcon'.
   *
   * @return wartosc pola 'playIcon'.
   */
  public Icon getPlayIcon() {
    return playIcon;
  }

  /**
   * Setter dla pola 'playIcon'.
   *
   * @param playIcon wartosc ustawiana dla pola 'playIcon'.
   */
  public void setPlayIcon(Icon playIcon) {
    this.playIcon = playIcon;
  }

  /**
   * Getter dla pola 'recordIcon'.
   *
   * @return wartosc pola 'recordIcon'.
   */
  public Icon getRecordIcon() {
    return recordIcon;
  }

  /**
   * Setter dla pola 'recordIcon'.
   *
   * @param recordIcon wartosc ustawiana dla pola 'recordIcon'.
   */
  public void setRecordIcon(Icon recordIcon) {
    this.recordIcon = recordIcon;
  }

  /**
   * Getter dla pola 'stopIcon'.
   *
   * @return wartosc pola 'stopIcon'.
   */
  public Icon getStopIcon() {
    return stopIcon;
  }

  /**
   * Setter dla pola 'stopIcon'.
   *
   * @param stopIcon wartosc ustawiana dla pola 'stopIcon'.
   */
  public void setStopIcon(Icon stopIcon) {
    this.stopIcon = stopIcon;
  }
}
