/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.util.dictionary.model;

import org.speech.asr.gui.util.dictionary.DictionaryItem;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 10, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public enum ValueType {
  STRING() {
    public Object getValue(DictionaryItem item) {
      return item.getStringValue();
    }},
  INTEGER() {
    public Object getValue(DictionaryItem item) {
      return item.getIntValue();
    }},
  DOUBLE() {
    public Object getValue(DictionaryItem item) {
      return item.getDoubleValue();
    }},
  BOOLEAN() {
    public Object getValue(DictionaryItem item) {
      return null;  //TODO To change body of implemented methods use File | Settings | File Templates.
    }};

  public abstract Object getValue(DictionaryItem item);
}
