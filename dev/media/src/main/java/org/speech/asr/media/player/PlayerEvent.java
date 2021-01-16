/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.media.player;

import org.speech.asr.media.vo.AudioSource;
import org.speech.asr.media.vo.Time;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 19, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class PlayerEvent {

  private long eventTimestamp;

  private Time audioTime;

  private Time audioDuration;

  private AudioSource audioSource;

  private PlayerState playerState;

  /**
   * Getter dla pola 'audioDuration'.
   *
   * @return wartosc pola 'audioDuration'.
   */
  public Time getAudioDuration() {
    return audioDuration;
  }

  /**
   * Setter dla pola 'audioDuration'.
   *
   * @param audioDuration wartosc ustawiana dla pola 'audioDuration'.
   */
  public void setAudioDuration(Time audioDuration) {
    this.audioDuration = audioDuration;
  }

  /**
   * Getter dla pola 'playerState'.
   *
   * @return wartosc pola 'playerState'.
   */
  public PlayerState getPlayerState() {
    return playerState;
  }

  /**
   * Setter dla pola 'playerState'.
   *
   * @param playerState wartosc ustawiana dla pola 'playerState'.
   */
  public void setPlayerState(PlayerState playerState) {
    this.playerState = playerState;
  }

  /**
   * Getter dla pola 'audioSource'.
   *
   * @return wartosc pola 'audioSource'.
   */
  public AudioSource getAudioSource() {
    return audioSource;
  }

  /**
   * Setter dla pola 'audioSource'.
   *
   * @param audioSource wartosc ustawiana dla pola 'audioSource'.
   */
  public void setAudioSource(AudioSource audioSource) {
    this.audioSource = audioSource;
  }

  /**
   * Getter dla pola 'eventTimestamp'.
   *
   * @return wartosc pola 'eventTimestamp'.
   */
  public long getEventTimestamp() {
    return eventTimestamp;
  }

  /**
   * Setter dla pola 'eventTimestamp'.
   *
   * @param eventTimestamp wartosc ustawiana dla pola 'eventTimestamp'.
   */
  public void setEventTimestamp(long eventTimestamp) {
    this.eventTimestamp = eventTimestamp;
  }

  /**
   * Getter dla pola 'audioTime'.
   *
   * @return wartosc pola 'audioTime'.
   */
  public Time getAudioTime() {
    return audioTime;
  }

  /**
   * Setter dla pola 'audioTime'.
   *
   * @param audioTime wartosc ustawiana dla pola 'audioTime'.
   */
  public void setAudioTime(Time audioTime) {
    this.audioTime = audioTime;
  }


  public String toString() {
    return "PlayerEvent{" +
        "audioDuration=" + audioDuration +
        ", eventTimestamp=" + eventTimestamp +
        ", audioTime=" + audioTime +
        ", audioSource=" + audioSource +
        ", playerState=" + playerState +
        '}';
  }
}
