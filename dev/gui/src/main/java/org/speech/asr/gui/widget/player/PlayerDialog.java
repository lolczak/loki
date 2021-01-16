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
import org.speech.asr.gui.constant.ConfirmationResult;
import org.speech.asr.gui.context.AppContext;
import org.speech.asr.gui.util.ConfirmationDialogExt;
import org.speech.asr.gui.util.image.ScalingSupportingImageSource;
import org.speech.asr.media.vo.AudioSource;
import org.springframework.context.MessageSource;
import org.springframework.richclient.util.WindowUtils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 11, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class PlayerDialog extends JDialog {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(PlayerDialog.class.getName());

  private ScalingSupportingImageSource imageSource;

  private MessageSource messageSource;

  private PlayerWidget widget;

  private AudioSource audioSource;

  private boolean audioSourceChanged;

  public PlayerDialog(AudioSource audioSource) {
    super(AppContext.getInstance().getActiveWindow(), true);
    this.audioSource = audioSource;
    audioSourceChanged = false;
    imageSource = AppContext.getInstance().getImageSource();
    messageSource = AppContext.getInstance().getMessageSource();
    setTitle(messageSource.getMessage("player.dialog.title", null, "player.dialog.title", Locale.getDefault()));
    add(createDialogContentPane());
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    pack();
    setResizable(false);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });
  }

  protected void onClose() {
    log.debug("Closing window");
    if (widget.isAudioSourceChanged()) {
      MessageSource msgSrc = AppContext.getInstance().getMessageSource();
      String title =
          msgSrc.getMessage("player.confirmation.title", null, "player.confirmation.title", Locale.getDefault());
      String message =
          msgSrc.getMessage("player.confirmation.message", null, "player.confirmation.message", Locale.getDefault());
      ConfirmationDialogExt dialog = new ConfirmationDialogExt(title, message);
      dialog.showDialog();
      if (dialog.getResult() == ConfirmationResult.OK) {
        audioSourceChanged = true;
        audioSource = widget.getAudioSource();
      }

    }
  }

  /**
   * Getter for property 'audioSource'.
   *
   * @return Value for property 'audioSource'.
   */
  public AudioSource getAudioSource() {
    return audioSource;
  }

  /**
   * Getter for property 'audioSourceChanged'.
   *
   * @return Value for property 'audioSourceChanged'.
   */
  public boolean isAudioSourceChanged() {
    return audioSourceChanged;
  }

  protected JComponent createDialogContentPane() {
    PlayerResources playerResources = new PlayerResources();
    playerResources.setPlayIcon(imageSource.getIcon32x32("play.icon"));
    playerResources.setStopIcon(imageSource.getIcon32x32("stop.icon"));
    playerResources.setPauseIcon(imageSource.getIcon32x32("pause.icon"));
    playerResources.setRecordIcon(imageSource.getIcon32x32("record.icon"));
    playerResources
        .setTimeText(messageSource.getMessage("player.time.label", null, "player.time.label", Locale.getDefault()));
    playerResources.setTimeOfText(
        messageSource.getMessage("player.timeOf.label", null, "player.timeOf.label", Locale.getDefault()));

    widget = new PlayerWidget(audioSource, playerResources);
    return widget.getPlayerComponent();
  }

  public void showDialog() {
    WindowUtils.centerOnParent(this, getParent());
    setVisible(true);
  }

}
