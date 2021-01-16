/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.widget.filechooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.context.AppContext;
import org.springframework.binding.form.ConfigurableFormModel;
import org.springframework.context.MessageSource;
import org.springframework.richclient.form.binding.swing.SandboxSwingBindingFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class FileChooserWidget {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(FileChooserWidget.class.getName());

  private JTextField textField;

  private JButton button;

  private ConfigurableFormModel formModel;

  private JFileChooser jFileChooser;

  private SelectionMode selectionMode;

  private String propertyName;

  public FileChooserWidget(ConfigurableFormModel formModel, SelectionMode selectionMode, String propertyName) {
    this.formModel = formModel;
    this.selectionMode = selectionMode;
    this.propertyName = propertyName;
  }

  public JComponent createComponents() {
    jFileChooser = new JFileChooser();
    jFileChooser.setFileSelectionMode(selectionMode.getMode());
    MessageSource msgSrc = AppContext.getInstance().getMessageSource();
    String buttonLabel =
        msgSrc.getMessage("fileChooser.button.label", null, "fileChooser.button.label", Locale.getDefault());
    button = new JButton(buttonLabel);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buttonClicked();
      }
    });

    textField = (JTextField) new SandboxSwingBindingFactory(formModel).createBoundTextField(propertyName).getControl();

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout(10, 10));
    panel.add(textField, BorderLayout.CENTER);
    panel.add(button, BorderLayout.EAST);
    return panel;
  }

  private void buttonClicked() {
    jFileChooser.setCurrentDirectory(new File(textField.getText()));
    int returnVal = jFileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(button));
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File selectedFile = jFileChooser.getSelectedFile();
      textField.setText(selectedFile.getPath());
    }
  }
}
