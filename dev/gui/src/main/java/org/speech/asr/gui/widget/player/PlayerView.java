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
import org.speech.asr.media.util.TimeUtils;
import org.speech.asr.media.vo.Time;

import javax.media.format.AudioFormat;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 18, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class PlayerView extends JPanel implements PlayerChangeListener {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(PlayerView.class.getName());

  private static final int MAJOR_TICK_SPACING = 1000;

  private static final int MINOR_TICK_SPACING = 250;

  private JSlider timeLine;

  private JButton playButton;

  private JButton stopButton;

  private JButton pauseButton;

  private JToggleButton recordButton;

  private JLabel timeLabel;

  private JLabel stateLabel;

  private JLabel encodingLabel;

  private JLabel sampleRateLabel;

  private JLabel sampleSizeLabel;

  private JLabel endianLabel;

  private PlayerResources resources;

  private boolean isMousePressed;

  private PlayerModel model;

  public PlayerView(PlayerModel model, PlayerResources playerResources) {
    this.resources = playerResources;
    this.model = model;
    createComponents();
    layoutComponents();
    setListeners();
  }

  private void createComponents() {
    timeLabel = new JLabel(resources.getTimeText() + " 0:00 " + resources.getTimeOfText() + " 0:00");
    timeLine = new JSlider(JSlider.HORIZONTAL);
    timeLine.setDoubleBuffered(true);
    timeLine.setPaintTrack(true);
    timeLine.setMinimum(0);
    timeLine.setMaximum(100);
    timeLine.setValue(0);
    timeLine.setPreferredSize(new Dimension(500, 30));

    playButton = new JButton(resources.getPlayIcon());
    stopButton = new JButton(resources.getStopIcon());
    pauseButton = new JButton(resources.getPauseIcon());
    recordButton = new JToggleButton(resources.getRecordIcon());

    stateLabel = new JLabel(" ");
    stateLabel.setBorder(getLabelBorder());
    encodingLabel = new JLabel(" ");
    encodingLabel.setBorder(getLabelBorder());
    sampleRateLabel = new JLabel(" ");
    sampleRateLabel.setBorder(getLabelBorder());
    sampleSizeLabel = new JLabel(" ");
    sampleSizeLabel.setBorder(getLabelBorder());
    endianLabel = new JLabel(" ");
    endianLabel.setBorder(getLabelBorder());
  }

  private void layoutComponents() {
    Insets insets = new Insets(1, 1, 1, 1);
    setLayout(new GridBagLayout());

    add(timeLabel,
        new GridBagConstraints(0, 0, 5, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(5, 5, 5, 5), 2, 2));

    add(timeLine,
        new GridBagConstraints(0, 1, 5, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 2, 2));
    JPanel buttonsPanel = new JPanel(new GridBagLayout());
    buttonsPanel.add(playButton,
        new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 2, 2));
    buttonsPanel.add(pauseButton,
        new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 2, 2));
    buttonsPanel.add(stopButton,
        new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 2, 2));
    buttonsPanel.add(recordButton,
        new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 2, 2));

    buttonsPanel.add(new JPanel(),
        new GridBagConstraints(4, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 2, 2));

    add(buttonsPanel,
        new GridBagConstraints(0, 2, 5, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, insets, 2, 2));

    Insets statusInsets = new Insets(0, 0, 0, 0);
    add(stateLabel, new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        statusInsets, 0, 0));
    add(encodingLabel, new GridBagConstraints(1, 3, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        statusInsets, 0, 0));
    add(sampleRateLabel,
        new GridBagConstraints(2, 3, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, statusInsets, 0,
            0));
    add(sampleSizeLabel,
        new GridBagConstraints(3, 3, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, statusInsets, 0,
            0));
    add(endianLabel, new GridBagConstraints(4, 3, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        statusInsets, 0, 0));
  }

  private void setListeners() {
    model.addListener(this);

    playButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        model.start();
      }
    });

    pauseButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        model.pause();
      }
    });

    stopButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        model.stop();
      }
    });

    recordButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (recordButton.isSelected()) {
          model.startRecording();
        } else {
          model.stopRecording();
        }
      }
    });

    timeLine.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        isMousePressed = true;
      }

      public void mouseReleased(MouseEvent e) {
        isMousePressed = false;
        model.seek(new Time(TimeUtils.millisToNanos(timeLine.getValue())));
      }
    });
  }

  private Border getLabelBorder() {
    Border bevelBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED, UIManager
        .getColor("controlHighlight"), UIManager.getColor("controlShadow"));
    Border emptyBorder = BorderFactory.createEmptyBorder(1, 3, 1, 3);
    return BorderFactory.createCompoundBorder(bevelBorder, emptyBorder);
  }

  public void updateDuration(final Time duration) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        int millis = (int) TimeUtils.nanosToMillis(duration.getNanoTime());
        timeLine.setMinimum(0);
        timeLine.setMaximum(millis);
        timeLine.setExtent(1);
        timeLine.setMajorTickSpacing(MAJOR_TICK_SPACING);
        timeLine.setMinorTickSpacing(MINOR_TICK_SPACING);
        timeLine.setValue(0);
        timeLine.setPaintTicks(true);
        timeLine.setPaintTrack(true);
      }
    });
  }

  public void updateTime(final Time time, final Time duration) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        int millis = (int) TimeUtils.nanosToMillis(time.getNanoTime());
        if (!isMousePressed) {
          timeLine.setValue(millis);
        }
        String timeStr = TimeUtils.formatTime(time);
        String durationStr = TimeUtils.formatTime(duration);
        timeLabel
            .setText(resources.getTimeText() + " " + timeStr + " " + resources.getTimeOfText() + " " + durationStr);
      }
    });
  }

  public void updateFormatLabels(final AudioFormat format) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        encodingLabel.setText(format.getEncoding());
        sampleRateLabel.setText(format.getSampleRate() + " Hz");
        sampleSizeLabel.setText(format.getSampleSizeInBits() + "-bit");
        endianLabel.setText(format.getEndian() == 0 ? "LITTLE_ENDIAN" : "BIG_ENDIAN");
      }
    });

  }

  public void updateStateLabel(final String stateText) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        stateLabel.setText(stateText);
      }
    });
  }

}
