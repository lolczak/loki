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
import org.speech.asr.gui.util.command.CommandButtonFactory;
import org.speech.asr.gui.util.image.ScalingSupportingImageSource;
import org.speech.asr.gui.view.editor.ButtonDescriptor;
import org.springframework.context.MessageSource;
import org.springframework.core.closure.Closure;
import org.springframework.richclient.dialog.InputApplicationDialog;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 3, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class TableWidget {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(TableWidget.class.getName());

  private PaginationSupportingTableModel model;

  private JTable table;

  private ScalingSupportingImageSource imageSource;

  private MessageSource messageSource;

  private JLabel pageField;

  private boolean showNagigationPanel;

  private String pageText;

  private String pageFromText;

  public TableWidget(TableDataProvider provider, String[] propertyNames, boolean showNagigationPanel,
                     ScalingSupportingImageSource imageSource, MessageSource messageSource) {
    model = new PaginationSupportingTableModel(provider, propertyNames, propertyNames, 10);
    this.showNagigationPanel = showNagigationPanel;
    this.imageSource = imageSource;
    this.messageSource = messageSource;
  }

  public TableWidget(TableDataProvider provider, String[] propertyNames, String[] columnNames,
                     boolean showNagigationPanel,
                     ScalingSupportingImageSource imageSource, MessageSource messageSource) {
    model = new PaginationSupportingTableModel(provider, propertyNames, columnNames, 10);
    this.showNagigationPanel = showNagigationPanel;
    this.imageSource = imageSource;
    this.messageSource = messageSource;
  }

  public Component getComponent() {
    JPanel panel = new JPanel(new BorderLayout());
    pageText = messageSource.getMessage("page.tabel.label", new Object[]{}, "Page:", Locale.getDefault());
    pageFromText = messageSource.getMessage("from.tabel.label", new Object[]{}, "from", Locale.getDefault());
    table = new JTable(model);
    JScrollPane sp = new JScrollPane(table);
    panel.add(sp, BorderLayout.CENTER);
    if (showNagigationPanel) {
      panel.add(createNavigationPanel(), BorderLayout.SOUTH);
    }
    return panel;
  }

  public JTable getJTable() {
    return table;
  }

  public void setCellRenderer(int index, TableCellRenderer renderer) {
    table.getColumnModel().getColumn(index + 1).setCellRenderer(renderer);
  }

  public void setCellEditor(int index, TableCellEditor editor) {
    table.getColumnModel().getColumn(index + 1).setCellEditor(editor);
  }

  private String getPageText() {
    return pageText + " " + getPage() + " " + pageFromText + " " + getMaxPages();
  }

  public void insertRow() {
    int i = table.getSelectedRow();
    if (i < 0) {
      i = 0;
    }
    model.insertRow(i);
    refreshPageNumber();
  }

  public void removeSelectedRows() {
    int[] indices = table.getSelectedRows();
    if (indices != null && indices.length > 0) {
      model.removeRows(indices);
    }
    refreshPageNumber();
  }

  public void nextPage() {
    model.nextPage();
  }

  public void previousPage() {
    model.previousPage();
  }

  public void setPage(int no) {
    model.setPage(no - 1);
  }

  public void setFirstPage() {
    model.setPage(0);
  }

  public void setLastPage() {
    model.setLastPage();
  }

  public int getPage() {
    return model.getPage() + 1;
  }

  public int getMaxPages() {
    return model.getMaxPages();
  }

  public void setPageSize(int pageSize) {
    model.setPageSize(pageSize);
  }

  public void refresh() {
    model.fireRefresh();
  }

  private Component createNavigationPanel() {
    JPanel navigationPanel = new JPanel();
    JLabel pageSizeLabel =
        new JLabel(messageSource.getMessage("pageSize.label", new Object[0], "Page size:", Locale.getDefault()));
    final JComboBox pageSizeCombo = new JComboBox(new String[]{"10", "20", "50", "100"});
    pageSizeCombo.setSelectedIndex(0);
    pageSizeCombo.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        String item = (String) pageSizeCombo.getSelectedItem();
        int pageSize = Integer.valueOf(item);
        setPageSize(pageSize);
        refreshPageNumber();
      }
    });
    pageSizeCombo.setSelectedItem("10");
    navigationPanel.add(pageSizeLabel);
    navigationPanel.add(pageSizeCombo);

    navigationPanel.add(CommandButtonFactory.createButton(new ButtonDescriptor("firstPage", new FirstPageClosure()),
        imageSource, messageSource));
    navigationPanel
        .add(CommandButtonFactory.createButton(new ButtonDescriptor("previousPage", new PreviousPageClosure()),
            imageSource, messageSource));
    pageField = new JLabel(getPageText());
//    pageField.setEditable(false);
//    pageField.setColumns(6);
    pageField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    navigationPanel.add(pageField);

    navigationPanel.add(CommandButtonFactory.createButton(new ButtonDescriptor("gotoPage", new GotoPageClosure()),
        imageSource, messageSource));
    navigationPanel.add(CommandButtonFactory.createButton(new ButtonDescriptor("nextPage", new NextPageClosure()),
        imageSource, messageSource));
    navigationPanel.add(CommandButtonFactory.createButton(new ButtonDescriptor("lastPage", new LastPageClosure()),
        imageSource, messageSource));
    return navigationPanel;
  }

  public void refreshPageNumber() {
    pageField.setText(getPageText());
  }

  private class NextPageClosure implements Closure {
    public Object call(Object argument) {
      nextPage();
      refreshPageNumber();
      return null;
    }
  }

  private class PreviousPageClosure implements Closure {
    public Object call(Object argument) {
      previousPage();
      refreshPageNumber();
      return null;
    }
  }

  private class LastPageClosure implements Closure {
    public Object call(Object argument) {
      setLastPage();
      refreshPageNumber();
      return null;
    }
  }

  private class FirstPageClosure implements Closure {
    public Object call(Object argument) {
      setFirstPage();
      refreshPageNumber();
      return null;
    }
  }

  private class GotoPageClosure implements Closure {
    public Object call(Object argument) {
      final InputApplicationDialog dialog = new InputApplicationDialog(new GotoModel(), "pageNumber");
      String title =
          messageSource.getMessage("gotoPage.number.tabel.label", null, "Go to page", Locale.getDefault());
      String label = messageSource.getMessage("gotoPage.label.table.label", null, "Page number: ", Locale.getDefault());
      dialog.setInputLabelMessage(label);
      dialog.setTitle(title);
      dialog.setFinishAction(new Closure() {
        public Object call(Object argument) {
          setPage(Integer.valueOf(dialog.getFormModel().getValueModel("pageNumber").getValue().toString()));
          refreshPageNumber();
          return null;
        }
      });
      dialog.showDialog();
      return null;
    }
  }

  private class GotoModel {
    private String pageNumber;

    public String getPageNumber() {
      return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
      this.pageNumber = pageNumber;
    }
  }


}
