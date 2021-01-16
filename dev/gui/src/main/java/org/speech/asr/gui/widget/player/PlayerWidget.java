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
import org.speech.asr.media.vo.AudioSource;

import javax.swing.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 18, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class PlayerWidget {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(PlayerWidget.class.getName());

  private PlayerResources icons;

  private PlayerModel model;

  public PlayerWidget(AudioSource audioSource, PlayerResources icons) {
    this.icons = icons;
    model = new PlayerModel(audioSource);
  }

  public JComponent getPlayerComponent() {
    PlayerView pv = new PlayerView(model, icons);
    pv.setOpaque(true);
    return pv;
  }

  public boolean isAudioSourceChanged() {
    return model.isAudioChanged();
  }

  public AudioSource getAudioSource() {
    return model.getAudioSource();
  }
}
