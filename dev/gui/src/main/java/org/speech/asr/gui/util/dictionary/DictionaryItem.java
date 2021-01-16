/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.util.dictionary;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 8, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class DictionaryItem {

  private String prefix;

  private String text;

  private String value;

  public DictionaryItem() {
  }

  public DictionaryItem(String prefix, String text, String value) {
    this.prefix = prefix;
    this.text = text;
    this.value = value;
  }

  public Integer getIntValue() {
    return Integer.valueOf(value);
  }

  public Double getDoubleValue() {
    return Double.valueOf(value);
  }

  /**
   * Getter for property 'value'.
   *
   * @return Value for property 'value'.
   */
  public String getStringValue() {
    return value;
  }

  /**
   * Getter for property 'prefix'.
   *
   * @return Value for property 'prefix'.
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * Setter for property 'prefix'.
   *
   * @param prefix Value to set for property 'prefix'.
   */
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  /**
   * Getter for property 'text'.
   *
   * @return Value for property 'text'.
   */
  public String getText() {
    return text;
  }

  /**
   * Setter for property 'text'.
   *
   * @param text Value to set for property 'text'.
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Setter for property 'value'.
   *
   * @param value Value to set for property 'value'.
   */
  public void setValue(String value) {
    this.value = value;
  }

  public String toString() {
    return "DictionaryItem{" +
        "prefix='" + prefix + '\'' +
        ", text='" + text + '\'' +
        ", value='" + value + '\'' +
        '}';
  }
}
