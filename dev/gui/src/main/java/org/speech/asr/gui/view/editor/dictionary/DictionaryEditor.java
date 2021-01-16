/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.view.editor.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.gui.constant.ConfirmationResult;
import org.speech.asr.common.entity.DictionaryEntity;
import org.speech.asr.common.entity.Word;
import org.speech.asr.gui.logic.DictionaryBean;
import org.speech.asr.gui.util.image.ScalingSupportingImageSource;
import org.speech.asr.gui.util.StatusBarLogger;
import org.speech.asr.gui.widget.table.TableWidget;
import org.speech.asr.gui.view.editor.ItemEditor;
import org.speech.asr.gui.view.editor.ToolbarBuilder;
import org.speech.asr.gui.view.editor.ToolbarDescriptor;
import org.speech.asr.gui.view.editor.dictionary.importer.DictionaryImporter;
import org.speech.asr.gui.view.editor.dictionary.importer.ElektronikJKFormatImporter;
import org.springframework.context.MessageSource;
import org.springframework.core.closure.Closure;
import org.springframework.richclient.dialog.MessageDialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 2, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class DictionaryEditor implements ItemEditor<DictionaryEntity> {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DictionaryEditor.class.getName());

  private DictionaryEntity dictionary;

  private List<ToolbarDescriptor> topToolbar = null;

  private TableWidget table;

  private ScalingSupportingImageSource imageSource;

  private MessageSource messageSource;

  private DictionaryBean dictionaryBean;

  private List<Word> words;

  public DictionaryEditor() {

  }

  private void init() {
    if (topToolbar == null) {
      ToolbarBuilder builder = ToolbarBuilder.newInstance();
      createFileToolbar(builder);
      createTableToolbar(builder);
      createMiscellaneousToolbar(builder);
//      createNavigationToolbar(builder);
      topToolbar = builder.getToolbarsList();
    }
    words = dictionaryBean.getDictionaryContent(dictionary.getUuid());
    if (words == null) {
      words = new LinkedList();
    }
  }

  public String getKey() {
    return "dictionaryEditor";  //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public String getTitle() {
    return dictionary.getName();//todo
  }

  public Component getFormControl() {
    StatusBarLogger.log("Dictionary editor for " + getTitle());
    StatusBarLogger.getStatusBar().getProgressMonitor().taskStarted("dupa", 100);
    StatusBarLogger.getStatusBar().getProgressMonitor().worked(0);
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    table = new TableWidget(new DictionaryContentProvider(words), new String[]{"graphemes", "phonemes"}, true,
        imageSource, messageSource);
    panel.add(table.getComponent(), BorderLayout.CENTER);
    return panel;
  }

  public void setContext(DictionaryEntity context) {
    dictionary = context;
    init();
  }

  public List<ToolbarDescriptor> getBottomToolbars() {
    return null;
  }

  public List<ToolbarDescriptor> getLeftToolbars() {

    return null;
  }

  public List<ToolbarDescriptor> getRightToolbars() {
    return null;
  }

  public List<ToolbarDescriptor> getTopToolbars() {
    return topToolbar;  //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public void onClose() {
    //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public void onCreate() {
    //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public void onSelect() {
    //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  private void createTableToolbar(ToolbarBuilder builder) {
    builder.addToolbar("Table");
    builder.addButton("addRow", new InsertRowClosure());
    builder.addButton("removeRow", new RemoveRowsClosure());
  }

  private void createFileToolbar(ToolbarBuilder builder) {
    builder.addToolbar("File");
    builder.addButton("saveDictionary", new SaveContentClosure());
    builder.addButton("importDictionary", new DictionaryImportClosure());
  }

  private void createMiscellaneousToolbar(ToolbarBuilder builder) {
    builder.addToolbar("Miscellaneous");
    builder.addButton("findWord", new MessageClosure());
    builder.addButton("sortWords", new MessageClosure());
    builder.addButton("convertWords", new MessageClosure());
  }

  private class InsertRowClosure implements Closure {
    public Object call(Object argument) {
      table.insertRow();
      return null;
    }
  }

  private class RemoveRowsClosure implements Closure {
    public Object call(Object argument) {
      table.removeSelectedRows();
      return null;
    }
  }


  private class MessageClosure implements Closure {
    public Object call(Object argument) {

      new MessageDialog("test", dictionary.toString()).showDialog();
      return null;
    }
  }

  private class SaveContentClosure implements Closure {
    public Object call(Object argument) {
      dictionaryBean.saveDictionaryContent(dictionary.getUuid(), words);
      return null;
    }
  }

  private class DictionaryImportClosure implements Closure {
    public Object call(Object argument) {
      DictionaryImportDialog dialog = new DictionaryImportDialog();
      dialog.showDialog();
      if (dialog.getResult() == ConfirmationResult.OK) {
        File file = dialog.getSelectedFile();
        String format = dialog.getFormat();
        //todo fixme
        DictionaryImporter importer = new ElektronikJKFormatImporter();
        try {
          List<Word> importedWords = importer.importDictionary(new FileInputStream(file));
          words.addAll(importedWords);
          table.refresh();
        } catch (FileNotFoundException e) {
          throw new AsrRuntimeException(e);
        }
      }
      return null;
    }
  }

  /**
   * Setter dla pola 'imageSource'.
   *
   * @param imageSource wartosc ustawiana dla pola 'imageSource'.
   */
  public void setImageSource(ScalingSupportingImageSource imageSource) {
    this.imageSource = imageSource;
  }

  /**
   * Setter dla pola 'messageSource'.
   *
   * @param messageSource wartosc ustawiana dla pola 'messageSource'.
   */
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Setter dla pola 'dictionaryBean'.
   *
   * @param dictionaryBean wartosc ustawiana dla pola 'dictionaryBean'.
   */
  public void setDictionaryBean(DictionaryBean dictionaryBean) {
    this.dictionaryBean = dictionaryBean;
  }
}
