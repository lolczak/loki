/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.widget.progress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.context.AppContext;
import org.speech.asr.gui.util.image.ScalingSupportingImageSource;
import org.springframework.context.MessageSource;
import org.springframework.richclient.util.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ProgressDialog extends JDialog implements ProgressWidget {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ProgressDialog.class.getName());

  private ScalingSupportingImageSource imageSource;

  private MessageSource messageSource;

  private JLabel statusLabel;

  private JProgressBar progressBar;

  private JButton cancelButton;

  private JToggleButton detailsButton;

  private JTextArea detailsText;

  private JScrollPane scrollPane;

  private JLabel icon;

  private String showDetailsTxt;

  private String hideDetailsTxt;

  private String cancelTxt;

  public ProgressDialog(String titleMessageCode) {
    super(AppContext.getInstance().getActiveWindow(), true);
    imageSource = AppContext.getInstance().getImageSource();
    messageSource = AppContext.getInstance().getMessageSource();

    showDetailsTxt = messageSource.getMessage("progress.showDetails.label", null, "Show details", Locale.getDefault());
    hideDetailsTxt = messageSource.getMessage("progress.hideDetails.label", null, "Hide details", Locale.getDefault());
    cancelTxt = messageSource.getMessage("progress.cancel.label", null, "Cancel", Locale.getDefault());

    setTitle(messageSource.getMessage(titleMessageCode, null, titleMessageCode, Locale.getDefault()));
    add(createDialogContentPane());
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    pack();
    setResizable(false);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });
  }

  protected void onClose() {
//    MessageSource msgSrc = AppContext.getInstance().getMessageSource();
//    String title =
//        msgSrc.getMessage("player.confirmation.title", null, "player.confirmation.title", Locale.getDefault());
//    String message =
//        msgSrc.getMessage("player.confirmation.message", null, "player.confirmation.message", Locale.getDefault());
//    ConfirmationDialogExt dialog = new ConfirmationDialogExt(title, message);
//    dialog.showDialog();
//    if (dialog.getResult() == ConfirmationResult.OK) {
//    }

  }

  protected void createComponents() {
    statusLabel = new JLabel("Status...");
    progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
    progressBar.setIndeterminate(true);
    progressBar.setValue(43);
    progressBar.setPreferredSize(new Dimension(400, 20));
    progressBar.setStringPainted(true);
    cancelButton = new JButton(cancelTxt);
    detailsButton = new JToggleButton(showDetailsTxt);
    detailsButton.setSelected(false);
    detailsButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (detailsButton.isSelected()) {
          scrollPane.setVisible(true);
          detailsButton.setText(hideDetailsTxt);
          pack();
        } else {
          scrollPane.setVisible(false);
          detailsButton.setText(showDetailsTxt);
          pack();
        }
      }
    });

    icon = new JLabel(imageSource.getIcon("working.icon"));
    detailsText = new JTextArea();
    detailsText.setColumns(30);
    detailsText.setRows(5);
    detailsText.setText("");
    scrollPane = new JScrollPane(detailsText);
    scrollPane.setVisible(false);
  }

  protected JComponent createDialogContentPane() {
    createComponents();
    Insets insets = new Insets(1, 1, 1, 5);
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());

    panel.add(icon,
        new GridBagConstraints(0, 0, 1, 4, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets(5, 5, 5, 5), 5, 5));

    panel.add(statusLabel,
        new GridBagConstraints(1, 0, 3, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets(10, 1, 5, 5), 5, 5));
    panel.add(progressBar,
        new GridBagConstraints(1, 1, 3, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets(5, 1, 10, 5), 5, 5));
    panel.add(scrollPane,
        new GridBagConstraints(1, 2, 3, 5, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 5, 5));

    JPanel buttonPanel = new JPanel();

    buttonPanel.add(new JPanel(),
        new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 5, 5));
    buttonPanel.add(detailsButton,
        new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 5, 5));
    buttonPanel.add(cancelButton,
        new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets(1, 1, 1, 0), 0, 0));

    panel.add(new JPanel(),
        new GridBagConstraints(1, 7, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            insets, 5, 5));
    panel.add(buttonPanel,
        new GridBagConstraints(3, 7, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.EAST, insets, 0, 0));
    panel.setOpaque(true);
    return panel;
  }


  public void showDialog() {
    WindowUtils.centerOnParent(this, getParent());
    setVisible(true);
  }

  public void log(final String msg) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        String old = detailsText.getText();
        detailsText.setText(old + "\n" + msg);
      }
    });
  }

  public void progress(final int p) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        progressBar.setValue(p);
      }
    });
  }

  public void setStatus(final String txt) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        statusLabel.setText(txt);
      }
    });
  }

  public void start() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        progressBar.setIndeterminate(true);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        if (!isVisible()) {
          showDialog();
        }
      }
    });
  }

  public void start(final int allWork) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        progressBar.setIndeterminate(false);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setMaximum(allWork);
        if (!isVisible()) {
          showDialog();
        }
      }
    });
  }

  public void stop() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        detailsText.setText("");
        setVisible(false);
      }
    });
  }
}
