/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.widget.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.util.Assert;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import java.util.Arrays;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 3, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class PaginationSupportingTableModel extends AbstractTableModel {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(PaginationSupportingTableModel.class.getName());

  private int pageSize;

  private int pageNumber;

  private String[] propertyNames;

  private String[] columnNames;

  private TableDataProvider source;

  private boolean isRowNumberPresent;

  public PaginationSupportingTableModel(TableDataProvider source, String[] propertyNames, String[] columnNames,
                                        int pageSize) {
    this.source = source;
    this.propertyNames = propertyNames;
    this.columnNames = columnNames;
    this.pageSize = pageSize;
    this.isRowNumberPresent = true;
    pageNumber = 0;
  }


  public int getColumnCount() {
    return columnNames.length + (isRowNumberPresent ? 1 : 0);
  }

  public int getRowCount() {
    int totalRows = source.getRowCount();
    if (pageSize >= totalRows) {
      Assert.isTrue(pageNumber == 0, "Page size have to be 0");
      return totalRows;
    } else {
      if (isLastPage(totalRows)) {
        return pageSize - ((pageNumber + 1) * pageSize - totalRows);
      } else {
        return pageSize;
      }
    }
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    if (isRowNumberPresent && columnIndex == 0) {
      return String.valueOf(getAbsoluteIndex(rowIndex) + 1);
    }
    return source.getValueAt(getAbsoluteIndex(rowIndex), propertyNames[columnIndex - (isRowNumberPresent ? 1 : 0)]);
  }

  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  public String getColumnName(int column) {
    if (column == 0) {
      return "No.";
    }
    return columnNames[column - (isRowNumberPresent ? 1 : 0)];
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    if (isRowNumberPresent && columnIndex == 0) {
      return false;
    }
    return true;
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    source.setValueAt(getAbsoluteIndex(rowIndex), propertyNames[columnIndex - (isRowNumberPresent ? 1 : 0)], aValue);
  }

  public void insertRow(int rowIndex) {
    source.insertRow(getAbsoluteIndex(rowIndex));
    fireTableChanged(new TableModelEvent(this));
  }

  public void fireRefresh() {
    fireTableChanged(new TableModelEvent(this));
  }

  public void removeRows(int[] rowsIndices) {
    //todo
    Arrays.sort(rowsIndices);
    int dec = 0;
    for (int item : rowsIndices) {
      source.removeRow(getAbsoluteIndex(item - dec++));
    }
    if (source.getRowCount() <= pageSize * pageNumber) {
      pageNumber--;
    }
    fireTableChanged(new TableModelEvent(this));
  }

  public void gotoPage(int no) {
    if (no < getMaxPages()) {
      pageNumber = no;
      fireTableChanged(new TableModelEvent(this));
    }
  }

  public void gotoRow(int no) {

  }

  public void nextPage() {
    setPage(pageNumber + 1);
  }

  public void previousPage() {
    setPage(pageNumber - 1);
  }

  public int getPage() {
    return pageNumber;
  }

  public void setPage(int no) {
    if (no >= 0 && no != pageNumber && no < getMaxPages(source.getRowCount())) {
      pageNumber = no;
      fireTableChanged(new TableModelEvent(this));
    }
  }

  public void setPageSize(int newSize) {
    int actualIndex = pageNumber * pageSize;
    pageSize = newSize;
    int newPageNumber = (int) Math.floor((double) actualIndex / (double) pageSize);
    pageNumber = newPageNumber;

    fireTableChanged(new TableModelEvent(this));
  }

  public void setLastPage() {
    setPage(getMaxPages(source.getRowCount()) - 1);
  }

  private boolean isLastPage(int totalRows) {
    int maxPages = getMaxPages(totalRows);
    return pageNumber == maxPages - 1;
  }

  public int getMaxPages() {
    return getMaxPages(source.getRowCount());
  }

  private int getMaxPages(int totalRows) {
    return (int) Math.ceil(((double) totalRows) / ((double) pageSize));
  }

  private int getAbsoluteIndex(int rowIndex) {
    return pageNumber * pageSize + rowIndex;
  }
}
