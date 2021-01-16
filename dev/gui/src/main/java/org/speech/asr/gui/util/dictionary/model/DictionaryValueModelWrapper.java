/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.util.dictionary.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.util.dictionary.DictionariesSource;
import org.speech.asr.gui.util.dictionary.DictionaryItem;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.TypeConverter;
import org.springframework.core.closure.Closure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 10, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class DictionaryValueModelWrapper {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DictionaryValueModelWrapper.class.getName());

  private String prefix;

  private DictionariesSource source;

  private Map<Object, DictionaryItem> valuesMap;

  private ValueType valueType;

  private Closure getterConverter;

  private Closure setterConverter;

  private ValueModel wrappedValueModel;

  private ValueModel wrapper;

  public DictionaryValueModelWrapper(DictionariesSource source, ValueType valueType, String prefix,
                              ValueModel wrappedValueModel) {
    this.source = source;
    this.prefix = prefix;
    this.valueType = valueType;
    this.wrappedValueModel = wrappedValueModel;
    initMap();
    getterConverter = new DictionaryGetterClosure();
    setterConverter = new DictionarySetterClosure();
    this.wrapper = new TypeConverter(wrappedValueModel, getterConverter, setterConverter);
  }

  private void initMap() {
    valuesMap = new HashMap();
    List<DictionaryItem> items = source.getItems(prefix);
    for (DictionaryItem item : items) {
      Object value = valueType.getValue(item);
      valuesMap.put(value, item);
    }
  }

  public ValueModel getValueModel() {
    return wrapper;
  }

  private class DictionarySetterClosure implements Closure {
    public Object call(Object argument) {
      DictionaryItem item = (DictionaryItem) argument;
      return valueType.getValue(item);
    }
  }

  private class DictionaryGetterClosure implements Closure {
    public Object call(Object argument) {
      return valuesMap.get(argument);
    }
  }

}