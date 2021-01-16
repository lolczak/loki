package org.speech.asr.gui.widget.table;

/**
 * //@todo interface description
 * <p/>
 * Creation date: May 3, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface TableDataProvider {

  Object getValueAt(int rowIndex, String propertyName);

  void insertRow(int rowIndex);

  void removeRow(int rowIndex);

  void setValueAt(int rowIndex, String propertyName, Object value);

  int getRowCount();

}
