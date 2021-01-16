/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.view.editor;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 3, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
class DockableRepository {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DockableRepository.class.getName());

  private List<ItemEditorAdapter> dockableSequence;

  private Map<Object, ItemEditorAdapter> dockableMap;

  public DockableRepository() {
    dockableSequence = new LinkedList();
    dockableMap = new HashedMap();
  }

  void put(ItemEditorAdapter dockable) {
    dockableSequence.add(dockable);
    dockableMap.put(dockable.getDockumentId(), dockable);
  }

  void remove(ItemEditorAdapter dockable) {
    dockableSequence.remove(dockable);
    dockableMap.remove(dockable.getDockumentId());
  }

  ItemEditorAdapter getById(Object documentId) {
    return dockableMap.get(documentId);
  }

  ItemEditorAdapter getLast() {
    if (dockableSequence.isEmpty()) {
      return null;
    }
    return dockableSequence.get(dockableSequence.size() - 1);
  }

  boolean contains(Object documentId) {
    return dockableMap.containsKey(documentId);
  }

  int getDockableCount() {
    return dockableSequence.size();
  }

  boolean isDesktopEmpty() {
    return dockableSequence.isEmpty();
  }

}
