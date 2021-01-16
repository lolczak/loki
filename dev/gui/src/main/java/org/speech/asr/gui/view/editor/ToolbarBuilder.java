/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.view.editor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.closure.Closure;

import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 3, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ToolbarBuilder {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ToolbarBuilder.class.getName());

  private List<ToolbarDescriptor> toolbars;

  private ToolbarDescriptor activeToolbar;

  private ToolbarBuilder() {
    toolbars = new LinkedList();
    activeToolbar = null;
  }

  public static ToolbarBuilder newInstance() {
    return new ToolbarBuilder();
  }

  public void addToolbar(String key) {
    activeToolbar = new ToolbarDescriptor(key, new LinkedList());
    toolbars.add(activeToolbar);
  }

  public void addButton(String key, Closure closure) {
    ButtonDescriptor buttonDescriptor = new ButtonDescriptor(key, closure);
    activeToolbar.getButtons().add(buttonDescriptor);
  }

  public List<ToolbarDescriptor> getToolbarsList() {
    return toolbars;
  }
}
