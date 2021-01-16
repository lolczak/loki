/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.view.editor.corpus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.constant.ConfirmationResult;
import org.speech.asr.common.entity.CorpusEntity;
import org.speech.asr.gui.logic.CorpusBean;
import org.speech.asr.gui.renderer.MediaContentCellEditor;
import org.speech.asr.gui.renderer.MediaContentCellRenderer;
import org.speech.asr.gui.util.StatusBarLogger;
import org.speech.asr.gui.util.image.ScalingSupportingImageSource;
import org.speech.asr.gui.view.editor.ItemEditor;
import org.speech.asr.gui.view.editor.ToolbarBuilder;
import org.speech.asr.gui.view.editor.ToolbarDescriptor;
import org.speech.asr.gui.view.editor.corpus.importer.An4CorpusImporter;
import org.speech.asr.gui.widget.table.TableWidget;
import org.springframework.context.MessageSource;
import org.springframework.core.closure.Closure;
import org.springframework.richclient.dialog.MessageDialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 14, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class CorpusEditor implements ItemEditor<CorpusEntity> {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(CorpusEditor.class.getName());

  private CorpusEntity corpus;

  private List<ToolbarDescriptor> topToolbar = null;

  private TableWidget table;

  private ScalingSupportingImageSource imageSource;

  private MessageSource messageSource;

  private CorpusBean corpusBean;

  private List utterances;

  private void init() {
    if (topToolbar == null) {
      ToolbarBuilder builder = ToolbarBuilder.newInstance();
      createFileToolbar(builder);
      createTableToolbar(builder);
      createMiscellaneousToolbar(builder);
      topToolbar = builder.getToolbarsList();
    }
    utterances = corpusBean.getCorpusContent(corpus.getUuid());
    if (utterances == null) {
      utterances = new LinkedList();
    }
  }

  public Component getFormControl() {

    StatusBarLogger.log("Corpus editor for " + getTitle());
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    table = new TableWidget(new CorpusContentProvider(corpus, corpusBean),
        new String[]{"transcription", "audioContent"},
        new String[]{"utterance", "speech"}, true, imageSource, messageSource);
    MediaContentCellRenderer renderer = new MediaContentCellRenderer(imageSource);
    MediaContentCellEditor editor = new MediaContentCellEditor(imageSource);
    panel.add(table.getComponent(), BorderLayout.CENTER);
    table.setCellRenderer(1, renderer);
    table.setCellEditor(1, editor);
    table.getJTable().setRowHeight(32);
    table.getJTable().setIntercellSpacing(new Dimension(5, 5));
    return panel;
  }

  public String getKey() {
    return "corpusEditor";
  }


  public String getTitle() {
    return corpus.getName();
  }

  public List<ToolbarDescriptor> getTopToolbars() {
    return topToolbar;
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

  public List<ToolbarDescriptor> getBottomToolbars() {
    return null;
  }

  public List<ToolbarDescriptor> getLeftToolbars() {
    return null;
  }

  public List<ToolbarDescriptor> getRightToolbars() {
    return null;
  }

  public void setContext(CorpusEntity context) {
    this.corpus = context;
    init();
  }

  private void createTableToolbar(ToolbarBuilder builder) {
    builder.addToolbar("Table");
    builder.addButton("addRow", new InsertRowClosure());
    builder.addButton("removeRow", new RemoveRowsClosure());
  }

  private void createFileToolbar(ToolbarBuilder builder) {
    builder.addToolbar("File");
    builder.addButton("saveCorpus", new SaveContentClosure());
    builder.addButton("importCorpus", new CorpusImportClosure());
  }

  private void createMiscellaneousToolbar(ToolbarBuilder builder) {
    builder.addToolbar("Miscellaneous");
    builder.addButton("findUtterance", new MessageClosure());
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

      new MessageDialog("test", corpus.toString()).showDialog();
      return null;
    }
  }

  private class SaveContentClosure implements Closure {
    public Object call(Object argument) {
      // corpus.saveDictionaryContent(dictionary.getUuid(), words);
      return null;
    }
  }

  private class CorpusImportClosure implements Closure {
    public Object call(Object argument) {
      CorpusImportDialog dialog = new CorpusImportDialog();
      dialog.showDialog();
      if (dialog.getResult() == ConfirmationResult.OK) {
        final File file = dialog.getSelectedFile();
        final File rootDir = dialog.getRootDir();
        String format = dialog.getFormat();
        final An4CorpusImporter importer = new An4CorpusImporter(corpus.getUuid(), corpusBean);
        new Thread(new Runnable() {
          public void run() {
            importer.importCorpus(file, rootDir, corpus.getAudioFormat().toAudioFormat());
            table.refresh();
          }
        }).start();
      }
      return null;
    }
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
   * Setter dla pola 'imageSource'.
   *
   * @param imageSource wartosc ustawiana dla pola 'imageSource'.
   */
  public void setImageSource(ScalingSupportingImageSource imageSource) {
    this.imageSource = imageSource;
  }

  /**
   * Setter dla pola 'corpusBean'.
   *
   * @param corpusBean wartosc ustawiana dla pola 'corpusBean'.
   */
  public void setCorpusBean(CorpusBean corpusBean) {
    this.corpusBean = corpusBean;
  }
}
