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
import org.speech.asr.gui.context.AppContext;
import org.speech.asr.gui.util.dictionary.DictionariesSource;
import org.speech.asr.gui.util.dictionary.model.DictionaryValueModelWrapper;
import org.speech.asr.gui.util.dictionary.model.ValueType;
import org.speech.asr.gui.widget.filechooser.FileChooserWidget;
import org.speech.asr.gui.widget.filechooser.SelectionMode;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.context.MessageSource;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.dialog.TitledPageApplicationDialog;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.swing.SandboxSwingBindingFactory;
import org.springframework.richclient.layout.TableLayoutBuilder;

import javax.swing.*;
import java.io.File;
import java.util.Locale;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 16, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class CorpusImportDialog extends TitledPageApplicationDialog {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(CorpusImportDialog.class.getName());

  private ValidatingFormModel formModel;

  private CorpusImportModel model;

  private ConfirmationResult result;

  private String dialogId;

  public CorpusImportDialog() {
    this.dialogId = "corpusImport";
    model = new CorpusImportModel();
    formModel = FormModelHelper.createFormModel(model, true, dialogId);
    wrapProperty("format", ValueType.STRING, "corpus.importer");
    Form form = new DictionaryImportForm();
    setDialogPage(new FormBackedDialogPage(form));
    setTitle(getMessage(dialogId + ".title"));
  }

  private void wrapProperty(String propertyName, ValueType type, String prefix) {
    DictionariesSource dictSource = AppContext.getInstance().getDictionarySource();
    ValueModel vm = formModel.getValueModel(propertyName);
    DictionaryValueModelWrapper wrapper =
        new DictionaryValueModelWrapper(dictSource, type, prefix, vm);
    formModel.add(propertyName, wrapper.getValueModel());
  }

  protected boolean onFinish() {
    formModel.commit();
    result = ConfirmationResult.OK;
    return true;
  }

  protected void onCancel() {
    super.onCancel();
    result = ConfirmationResult.CANCEL;
  }

  private class DictionaryImportForm extends AbstractForm {
    public DictionaryImportForm() {
      super(formModel);
      setId(dialogId);
    }

    protected JComponent createFormControl() {
      MessageSource msgSrc = AppContext.getInstance().getMessageSource();
      DictionariesSource dictSource = AppContext.getInstance().getDictionarySource();
      String promptLabel =
          msgSrc.getMessage("corpusImport.path.label", null, "corpusImport.path.label", Locale.getDefault());
      String dirLabel =
          msgSrc.getMessage("corpusImport.rootDir.label", null, "corpusImport.rootDir.label", Locale.getDefault());
      String formatLabel =
          msgSrc.getMessage("corpusImport.format.label", null, "corpusImport.format.label", Locale.getDefault());
      TableLayoutBuilder table = new TableLayoutBuilder();
      SandboxSwingBindingFactory bf = new SandboxSwingBindingFactory(getFormModel());
      FileChooserWidget fileChooser = new FileChooserWidget(getFormModel(), SelectionMode.FILES_ONLY, "path");
      FileChooserWidget dirChooser = new FileChooserWidget(getFormModel(), SelectionMode.DIRECTORIES_ONLY, "rootDir");
      table.row();
      table.cell(new JLabel(promptLabel), "colGrId=labels colSpec=right:pref").labelGapCol();
      table.cell(fileChooser.createComponents());
      table.relatedGapRow();
      table.cell(new JLabel(dirLabel), "colGrId=labels colSpec=right:pref").labelGapCol();
      table.cell(dirChooser.createComponents());
      table.relatedGapRow();
      table.cell(new JLabel(formatLabel), "colGrId=labels colSpec=right:pref").labelGapCol();
      table.cell(bf.createBoundComboBox("format", dictSource.getItems("corpus.importer"), "text").getControl());

      return table.getPanel();
    }
  }

  private class CorpusImportModel {
    private String path;

    private String format;

    private String rootDir;

    public String getRootDir() {
      return rootDir;
    }

    public void setRootDir(String rootDir) {
      this.rootDir = rootDir;
    }

    public String getPath() {
      return path;
    }

    public void setPath(String path) {
      this.path = path;
    }

    public String getFormat() {
      return format;
    }

    public void setFormat(String format) {
      this.format = format;
    }
  }

  public File getRootDir() {
    return new File(model.getRootDir());
  }

  public File getSelectedFile() {
    return new File(model.getPath());
  }

  public String getFormat() {
    return model.getFormat();
  }

  /**
   * Getter dla pola 'result'.
   *
   * @return wartosc pola 'result'.
   */
  public ConfirmationResult getResult() {
    return result;
  }
}
