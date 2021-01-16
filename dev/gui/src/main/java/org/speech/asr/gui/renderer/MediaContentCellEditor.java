/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.util.image.ScalingSupportingImageSource;
import org.speech.asr.gui.widget.player.PlayerDialog;
import org.speech.asr.media.vo.AudioSource;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 17, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class MediaContentCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MediaContentCellEditor.class.getName());

  private JButton button;

  private ScalingSupportingImageSource imageSource;

  private Icon playerIcon;

  private AudioSource audioSource;

  public MediaContentCellEditor(ScalingSupportingImageSource imageSource) {
    button = new JButton();
    button.addActionListener(this);
    button.setBorderPainted(false);
    button.setOpaque(true);
    this.imageSource = imageSource;
    playerIcon = imageSource.getIcon22x22("player.icon");
    button.setIcon(playerIcon);
  }

  public Object getCellEditorValue() {
    log.debug("Getting cell editor value");
    return audioSource;
  }

  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    log.debug("Getting media editor value {}", value);
    audioSource = (AudioSource) value;
    if (isSelected) {
      button.setBackground(table.getSelectionBackground());
      button.setForeground(table.getSelectionForeground());
    } else {
      button.setBackground(table.getBackground());
      button.setForeground(table.getForeground());
    }

    return button;  //TODO To change body of implemented methods use File | Settings | File Templates.
  }


  public void actionPerformed(ActionEvent e) {
    PlayerDialog dialog = new PlayerDialog(audioSource);
    dialog.showDialog();
    if (dialog.isAudioSourceChanged()) {
      audioSource = dialog.getAudioSource();
      fireEditingStopped();
    } else {
      fireEditingCanceled();
    }
  }
}
