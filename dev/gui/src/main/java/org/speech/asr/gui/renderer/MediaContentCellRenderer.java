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

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 17, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class MediaContentCellRenderer implements TableCellRenderer {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MediaContentCellRenderer.class.getName());

  private ScalingSupportingImageSource imageSource;

  private Icon playerIcon;

  private Icon recorderIcon;

  public MediaContentCellRenderer(ScalingSupportingImageSource imageSource) {
    this.imageSource = imageSource;
    playerIcon = imageSource.getIcon22x22("player.icon");
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                 int row, int column) {
    JLabel label = new JLabel();
    label.setOpaque(true);
    label.setIcon(playerIcon);
    label.setSize(22, 22);
    label.setHorizontalAlignment(JLabel.CENTER);

    if (isSelected) {
      label.setBackground(table.getSelectionBackground());
      label.setForeground(table.getSelectionForeground());
    } else {
      label.setBackground(table.getBackground());
      label.setForeground(table.getForeground());
    }

    return label;
  }
}
