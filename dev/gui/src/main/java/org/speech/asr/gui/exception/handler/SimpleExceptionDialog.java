package org.speech.asr.gui.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.dialog.TitledPageApplicationDialog;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.swing.SandboxSwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 1, 2009 <br/>
 *
 * @author Lukasz Olczak
 */
public class SimpleExceptionDialog extends TitledPageApplicationDialog {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SimpleExceptionDialog.class.getName());

  private static final String DIALOG_ID = "exceptionHandlerDialog";

  private FormObject formObject;

  private ValidatingFormModel formModel;

  public SimpleExceptionDialog(Thread thread, Throwable throwable) {
    formObject = new FormObject(thread, throwable);
    formModel = FormModelHelper.createFormModel(formObject, true, DIALOG_ID);
    formModel.setReadOnly(true);
    Form form = new ExceptionForm();
    FormBackedDialogPage page = new FormBackedDialogPage(form);
    page.setIcon(getIconSource().getIcon("exceptionHandlerDialog.icon"));
    setDialogPage(page);
    setTitle(getMessage(DIALOG_ID + ".title"));
  }

  protected boolean onFinish() {
    return true;
  }

  private class ExceptionForm extends AbstractForm {

    public ExceptionForm() {
      super(formModel);
      setId(DIALOG_ID);
    }

    protected JComponent createFormControl() {
      SandboxSwingBindingFactory bf = new SandboxSwingBindingFactory(getFormModel());
      TableFormBuilder formBuilder = new TableFormBuilder(bf);
      formBuilder.setLabelAttributes("colGrId=label colSpec=right:pref");
      formBuilder.row();
      formBuilder.add("threadName");
      formBuilder.row();
      JTextArea jText = (JTextArea) formBuilder.addTextArea("throwable")[1];
      jText.setColumns(80);
      jText.setRows(20);
      formBuilder.row();
      return formBuilder.getForm();
    }
  }

  public String stackTraceToString(Throwable e) {
    String retValue = null;
    StringWriter sw = null;
    PrintWriter pw = null;
    try {
      sw = new StringWriter();
      pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      retValue = sw.toString();
    } finally {
      try {
        if (pw != null) pw.close();
        if (sw != null) sw.close();
      } catch (IOException ignore) {
      }
    }
    return retValue;
  }

  private class FormObject {
    private String threadName;
    private String throwable;

    public FormObject(Thread thread, Throwable throwable) {
      this.threadName = thread.getName();
      this.throwable = stackTraceToString(throwable);
    }

    public String getThreadName() {
      return threadName;
    }

    public void setThreadName(String threadName) {
      this.threadName = threadName;
    }

    public String getThrowable() {
      return throwable;
    }

    public void setThrowable(String throwable) {
      this.throwable = throwable;
    }
  }
}
