/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.view.editor;

import com.vlsolutions.swing.docking.DockGroup;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.toolbars.ToolBarConstraints;
import com.vlsolutions.swing.toolbars.ToolBarContainer;
import com.vlsolutions.swing.toolbars.ToolBarPanel;
import com.vlsolutions.swing.toolbars.VLToolBar;
import static org.speech.asr.gui.constant.EditorsContainerConstants.*;
import org.speech.asr.gui.util.image.ScalingSupportingImageSource;
import org.speech.asr.gui.util.command.CommandButtonFactory;
import org.springframework.context.MessageSource;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Locale;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 1, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ItemEditorAdapter implements Dockable {

  private static DockGroup dockGroup = new DockGroup("editorGroup");

  private ScalingSupportingImageSource imageSource;

  private MessageSource messageSource;

  private ItemEditor adapted;

  private Object dockumentId;

  private Component form;

  private DockKey dockKey;

  //todo delegowanie zdarzen

  public ItemEditorAdapter(Object dockId, ItemEditor adapted) {
    this.adapted = adapted;
    this.dockumentId = dockId;
    form = null;
    dockKey = null;
    //todo init
  }

  public Component getComponent() {
    if (form == null) {
      if (isAnyToolbar()) {
        form = createToolbarPane();
      } else {
        form = adapted.getFormControl();
      }
    }
    return form;
  }

  private boolean isAnyToolbar() {
    return isLeftToolbar() || isRightToolbar() || isTopToolbar() || isBottomToolbar();
  }

  private boolean isRightToolbar() {
    return adapted.getRightToolbars() != null && !adapted.getRightToolbars().isEmpty();
  }

  private boolean isLeftToolbar() {
    return adapted.getLeftToolbars() != null && !adapted.getLeftToolbars().isEmpty();
  }

  private boolean isTopToolbar() {
    return adapted.getTopToolbars() != null && !adapted.getTopToolbars().isEmpty();
  }

  private boolean isBottomToolbar() {
    return adapted.getBottomToolbars() != null && !adapted.getBottomToolbars().isEmpty();
  }

  private Component createToolbarPane() {
    ToolBarContainer container =
        ToolBarContainer.createDefaultContainer(isTopToolbar(), isLeftToolbar(), isBottomToolbar(), isRightToolbar());
    Component component = adapted.getFormControl();
    container.add(component, BorderLayout.CENTER);

    if (isLeftToolbar()) {
      ToolBarPanel leftPanel = container.getToolBarPanelAt(BorderLayout.WEST);
      putToolbarsOnPanel(leftPanel, adapted.getLeftToolbars());
    }
    if (isRightToolbar()) {
      ToolBarPanel rightPanel = container.getToolBarPanelAt(BorderLayout.EAST);
      putToolbarsOnPanel(rightPanel, adapted.getRightToolbars());
    }
    if (isTopToolbar()) {
      ToolBarPanel topPanel = container.getToolBarPanelAt(BorderLayout.NORTH);
      putToolbarsOnPanel(topPanel, adapted.getTopToolbars());
    }
    if (isBottomToolbar()) {
      ToolBarPanel bottomPanel = container.getToolBarPanelAt(BorderLayout.SOUTH);
      putToolbarsOnPanel(bottomPanel, adapted.getBottomToolbars());
    }

    return container;
  }

  private void putToolbarsOnPanel(ToolBarPanel panel, List<ToolbarDescriptor> toolbars) {
    int i = 0;
    for (ToolbarDescriptor toolDesc : toolbars) {
      String toolbarKey = toolDesc.getKey() + TITLE_MSG_KEY_SUFFIX;
      String toolbarTitle = messageSource.getMessage(toolbarKey, new Object[]{}, toolbarKey, Locale.getDefault());
      VLToolBar toolbar = new VLToolBar(toolbarTitle);
      for (ButtonDescriptor buttonDesc : toolDesc.getButtons()) {
        toolbar.add(CommandButtonFactory.createButton(buttonDesc, imageSource, messageSource));
      }
      panel.add(toolbar, new ToolBarConstraints(0, i++));
    }
  }

  public DockKey getDockKey() {
    if (dockKey == null) {
      dockKey = createDockKey();
    }
    return dockKey;
  }

  private DockKey createDockKey() {
    String title = adapted.getTitle();
    if (title == null) {
      String titleKey = adapted.getKey() + TITLE_MSG_KEY_SUFFIX;
      title = messageSource.getMessage(titleKey, new Object[]{}, "error", Locale.getDefault());
    }
    String tooltipKey = adapted.getKey() + TOOLTIP_MSG_KEY_SUFFIX;
    String tooltip = messageSource.getMessage(tooltipKey, new Object[]{}, "", Locale.getDefault());
    Icon icon = imageSource.getIcon16x16(adapted.getKey() + ICON_MSG_KEY_SUFFIX);
    DockKey key = new DockKey(dockumentId.toString(), title, tooltip, icon);
    key.setDockGroup(dockGroup);
    key.setFloatEnabled(false);
    key.setAutoHideEnabled(false);
    key.setMaximizeEnabled(false);
    return key;
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
   * Getter dla pola 'dockId'.
   *
   * @return wartosc pola 'dockId'.
   */
  public Object getDockumentId() {
    return dockumentId;
  }
}
