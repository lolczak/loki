package org.speech.asr.gui.view.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.constant.ConfirmationResult;
import org.speech.asr.common.entity.DictionaryEntity;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.dialog.TitledPageApplicationDialog;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.swing.SandboxSwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

import javax.swing.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 27, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class DictionaryPropertiesDialog extends TitledPageApplicationDialog {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DictionaryPropertiesDialog.class.getName());

  private ValidatingFormModel dictionaryFormModel;

  private DictionaryEntity dictionary;

  private ConfirmationResult result;

  private String dialogId;

  private DictionaryPropertiesDialog(DictionaryEntity dictionary, String dialogId) {
    this.dictionary = dictionary;
    this.dialogId = dialogId;
    createValueModel();
    Form form = new DictionaryForm();
    setDialogPage(new FormBackedDialogPage(form));
    setTitle(getMessage(dialogId + ".title"));
  }

  public static DictionaryPropertiesDialog createNewDialog(DictionaryEntity dictionary) {
    return new DictionaryPropertiesDialog(dictionary, "newDictionary");
  }

  public static DictionaryPropertiesDialog createEditDialog(DictionaryEntity dictionary) {
    return new DictionaryPropertiesDialog(dictionary, "editDictionary");
  }

  private void createValueModel() {
    dictionaryFormModel = FormModelHelper.createFormModel(dictionary, true, dialogId);
  }

  protected boolean onFinish() {
    dictionaryFormModel.commit();
    result = ConfirmationResult.OK;
    return true;
  }

  protected void onCancel() {
    super.onCancel();
    result = ConfirmationResult.CANCEL;
  }

  private class DictionaryForm extends AbstractForm {
    protected DictionaryForm() {
      super(dictionaryFormModel);
      setId(dialogId);
    }

    protected JComponent createFormControl() {
      SandboxSwingBindingFactory bf = new SandboxSwingBindingFactory(getFormModel());
      TableFormBuilder formBuilder = new TableFormBuilder(getBindingFactory());
      formBuilder.setLabelAttributes("colGrId=label colSpec=right:pref");
      formBuilder.addSeparator("General");
      formBuilder.row();
      formBuilder.add("name");
      formBuilder.row();
      formBuilder.add("description");
      formBuilder.row();
      //*****************************8
//       Map<String, Object> langContext = new HashMap<String, Object>();
//      langContext.put(ListSelectionDialogBinder.SELECTABLE_ITEMS_HOLDER_KEY, new ValueHolder(Arrays.asList(new String[] {"pl-pl", "en-us"})));
//      langContext.put(ListSelectionDialogBinder.LABEL_PROVIDER_KEY, new LabelProvider() {
//        public String getLabel(Object item) {
//          return item.toString();
//        }
//      });
////      langContext.put(ListSelectionDialogBinder.FILTERED_KEY, Boolean.TRUE);
//      //langContext.put(ListSelectionDialogBinder.FILTER_PROPERTIES_KEY, new String[]{"name"});
//      formBuilder.add(bf.createBinding(JComboBox.class, "language", langContext), "colSpan=2 align=left");

      //*********************************
      formBuilder.add("language");
//      JComboBox combo = (JComboBox) formBuilder.add(bf.createBoundComboBox("language", new ValueHolder(Arrays.asList(new String[] {"pl-pl", "en-us"}))))[1];
      formBuilder.row();
      formBuilder.add("phoneticAlphabet");
      formBuilder.row();
      return formBuilder.getForm();
    }

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
