/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.widget.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.context.AppContext;
import org.speech.asr.media.microphone.JmfMicrophone;
import org.speech.asr.media.player.AudioPlayer;
import org.speech.asr.media.player.PlayerEvent;
import org.speech.asr.media.player.PlayerListener;
import org.speech.asr.media.player.PlayerState;
import org.speech.asr.media.vo.AudioSource;
import org.speech.asr.media.vo.Time;
import org.springframework.context.MessageSource;

import javax.media.format.AudioFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 20, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class PlayerModel {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(PlayerModel.class.getName());

  private static final String STATE_PREFIX = "player.state.";

  private static final String STATE_SUFFIX = ".label";

  private List<PlayerChangeListener> listeners;

  private AudioPlayer player;

  private JmfMicrophone microphone;

  private AudioFormat audioFormat;

  private AudioSource audioSource;

  private PlayerListener playerListener;

  private boolean audioChanged;

  public PlayerModel(AudioSource audioSource) {
    this.audioSource = audioSource;
    this.audioFormat = audioSource.getAudioFormat();
    listeners = new CopyOnWriteArrayList();
    playerListener = new InnerPlayerListener();
    player = new AudioPlayer();
    audioChanged = false;
    player.addListener(new InnerPlayerListener());
  }

  public void addListener(PlayerChangeListener l) {
    listeners.add(l);
    //todo fixme czasowe obejscie
    if (!audioSource.isEmpty()) {
      player.setAudioSource(audioSource);
    }
  }

  public void start() {
    if (audioSource.isEmpty()) {
      fireUpdateStateLabel(getLabelForState(PlayerState.ERROR));
      return;
    }
    player.start();
  }

  public void stop() {
    player.stop();
  }

  public void pause() {
    player.pause();
  }

  /**
   * Getter for property 'audioChanged'.
   *
   * @return Value for property 'audioChanged'.
   */
  public boolean isAudioChanged() {
    return audioChanged;
  }

  /**
   * Getter for property 'audioSource'.
   *
   * @return Value for property 'audioSource'.
   */
  public AudioSource getAudioSource() {
    return audioSource;
  }

  public void startRecording() {
    microphone = new JmfMicrophone();
    microphone.setAudioFormat(audioFormat);
    microphone.start();
    fireUpdateStateLabel(getLabelForState(PlayerState.RECORDING));
    audioChanged = true;
  }

  public void stopRecording() {
    microphone.stop();
    AudioSource as = microphone.getAudioSource();
    log.debug("Recorded : {} bytes", as.getAudioContent().length);
    audioSource = as;
    player.setAudioSource(as);
    fireUpdateStateLabel(getLabelForState(PlayerState.STOPPED));
  }

  public void seek(Time time) {

  }

  private class InnerPlayerListener implements PlayerListener {
    public void playerInitialized(final PlayerEvent event) {
      log.debug("Player initialized {}", event);
      fireUpdateDuration(event.getAudioDuration());
      fireUpdateStateLabel(getLabelForState(event.getPlayerState()));
      fireUpdateFormatLabels(event.getAudioSource().getAudioFormat());
      fireUpdateTime(event.getAudioTime(), event.getAudioDuration());
    }

    public void playerPaused(final PlayerEvent event) {
      fireUpdateTime(event.getAudioTime(), event.getAudioDuration());
      fireUpdateStateLabel(getLabelForState(event.getPlayerState()));
    }

    public void playerStarted(final PlayerEvent event) {
      fireUpdateTime(event.getAudioTime(), event.getAudioDuration());
      fireUpdateStateLabel(getLabelForState(event.getPlayerState()));
    }

    public void playerStopped(final PlayerEvent event) {
      fireUpdateTime(event.getAudioTime(), event.getAudioDuration());
      fireUpdateStateLabel(getLabelForState(event.getPlayerState()));
    }

    public void positionChanged(final PlayerEvent event) {
      fireUpdateTime(event.getAudioTime(), event.getAudioDuration());
    }
  }

  private void fireUpdateDuration(Time duration) {
    for (PlayerChangeListener l : listeners) {
      l.updateDuration(duration);
    }
  }

  private void fireUpdateTime(Time time, Time duration) {
    for (PlayerChangeListener l : listeners) {
      l.updateTime(time, duration);
    }
  }

  private void fireUpdateFormatLabels(AudioFormat format) {
    for (PlayerChangeListener l : listeners) {
      l.updateFormatLabels(format);
    }
  }

  private void fireUpdateStateLabel(String stateText) {
    for (PlayerChangeListener l : listeners) {
      l.updateStateLabel(stateText);
    }
  }

  private String getLabelForState(String stateTxt) {
    MessageSource msgSrc = AppContext.getInstance().getMessageSource();
    String key = STATE_PREFIX + stateTxt + STATE_SUFFIX;
    return msgSrc.getMessage(key, null, stateTxt, Locale.getDefault());
  }

  private String getLabelForState(PlayerState state) {
    return getLabelForState(state.toString());
  }
}
