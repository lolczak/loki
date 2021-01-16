/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.view.editor.dictionary;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.entity.Word;
import org.speech.asr.gui.widget.table.TableDataProvider;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 10, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class DictionaryContentProvider implements TableDataProvider {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DictionaryContentProvider.class.getName());


  private List<Word> rows;

  public DictionaryContentProvider(List<Word> rows) {
    this.rows = rows;
  }

  public int getRowCount() {
    return rows.size();
  }

  public Object getValueAt(int rowIndex, String propertyName) {
    Object bean = rows.get(rowIndex);
    Object value;
    try {
      value = PropertyUtils.getProperty(bean, propertyName);
    } catch (Exception e) {
      throw new IllegalArgumentException("There is no property " + propertyName, e);
    }
    return value;
  }

  public void removeRow(int rowIndex) {
    rows.remove(rowIndex);
  }

  public void insertRow(int rowIndex) {
    //todo fixme

    rows.add(rowIndex, new Word());
  }

  public void setValueAt(int rowIndex, String propertyName, Object value) {
    Object bean = rows.get(rowIndex);
    try {
      PropertyUtils.setProperty(bean, propertyName, value);
    } catch (Exception e) {
      throw new IllegalArgumentException("There is no property " + propertyName, e);
    }
  }
}
