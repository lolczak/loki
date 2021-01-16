/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.util.dictionary;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.gui.util.dictionary.DictionaryItem;
import org.speech.asr.gui.util.dictionary.DictionariesSource;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 8, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class DictionariesSourceImpl implements DictionariesSource {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DictionariesSourceImpl.class.getName());

  private String[] resourceNames;

  private Map<String, List<DictionaryItem>> dictionaryMap;

  @PostConstruct
  public void init() {
    for (String resource : resourceNames) {
      loadResource(resource);
    }
  }

  private void loadResource(String resourceName) {
    log.debug("Loading dictionary properties from {}", resourceName);
    ResourceBundle resourceBundle = ResourceBundle.getBundle(resourceName);
    if (resourceBundle == null) {
      throw new AsrRuntimeException("Cannot load resource bundle" + resourceName);
    }
    dictionaryMap = new HashedMap();
    Enumeration<String> keys = resourceBundle.getKeys();

    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      String value = resourceBundle.getString(key);
      DictionaryItem item = parseItem(key, value);
      addItem(item);
    }
  }

  private DictionaryItem parseItem(String key, String value) {
    DictionaryItem item = new DictionaryItem();
    int index = key.lastIndexOf(".");
    String prefix = key.substring(0, index);
    String dictVal = key.substring(index + 1, key.length());
    item.setPrefix(prefix);
    item.setText(value);
    item.setValue(dictVal);
    return item;
  }

  private void addItem(DictionaryItem item) {
    if (!dictionaryMap.containsKey(item.getPrefix())) {
      dictionaryMap.put(item.getPrefix(), new LinkedList());
    }
    List<DictionaryItem> items = dictionaryMap.get(item.getPrefix());
    items.add(item);
  }

  public List<DictionaryItem> getItems(String key) {
    return dictionaryMap.get(key);
  }

  /**
   * Setter for property 'resourceNames'.
   *
   * @param resourceNames Value to set for property 'resourceNames'.
   */
  public void setResourceNames(String[] resourceNames) {
    this.resourceNames = resourceNames;
  }
}
