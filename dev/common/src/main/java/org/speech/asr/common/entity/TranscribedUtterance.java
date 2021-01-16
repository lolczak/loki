/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.common.entity;

import org.speech.asr.common.jcr.JcrResource;
import org.speech.asr.common.entity.BaseEntity;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 16, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class TranscribedUtterance extends BaseEntity {

  private String transcription;

  private JcrResource audioContent;

  /**
   * Getter dla pola 'audioContent'.
   *
   * @return wartosc pola 'audioContent'.
   */
  public JcrResource getAudioContent() {
    return audioContent;
  }

  /**
   * Setter dla pola 'audioContent'.
   *
   * @param audioContent wartosc ustawiana dla pola 'audioContent'.
   */
  public void setAudioContent(JcrResource audioContent) {
    this.audioContent = audioContent;
  }

  /**
   * Getter dla pola 'transcription'.
   *
   * @return wartosc pola 'transcription'.
   */
  public String getTranscription() {
    return transcription;
  }

  /**
   * Setter dla pola 'transcription'.
   *
   * @param transcription wartosc ustawiana dla pola 'transcription'.
   */
  public void setTranscription(String transcription) {
    this.transcription = transcription;
  }
}
