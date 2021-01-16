/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.event;

import org.apache.commons.collections.map.HashedMap;
import org.speech.asr.common.event.Event;
import org.speech.asr.gui.constant.ItemEventType;

import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class CorpusEvent implements Event {
  public static final String UUID_KEY = "__uuid_key";

  public static final String CHANGED_UUIDS_KEY = "__changed_uuids_key";

  private ItemEventType eventType;

  private Map<String, Object> payload;

  public static CorpusEvent createNewCorpusEvent(String uuid) {
    CorpusEvent event = new CorpusEvent();
    event.setEventType(ItemEventType.NEW);
    Map<String, Object> payload = new HashedMap();
    payload.put(UUID_KEY, uuid);
    event.setPayload(payload);
    return event;
  }

  public static CorpusEvent createUpdatedCorpusEvent(String uuid) {
    CorpusEvent event = new CorpusEvent();
    event.setEventType(ItemEventType.UPDATED);
    Map<String, Object> payload = new HashedMap();
    payload.put(UUID_KEY, uuid);
    event.setPayload(payload);
    return event;
  }

  public static CorpusEvent createDeleteCorpusEvent(String uuid) {
    CorpusEvent event = new CorpusEvent();
    event.setEventType(ItemEventType.DELETED);
    Map<String, Object> payload = new HashedMap();
    payload.put(UUID_KEY, uuid);
    event.setPayload(payload);
    return event;
  }

  /**
   * Getter dla pola 'eventType'.
   *
   * @return wartosc pola 'eventType'.
   */
  public ItemEventType getEventType() {
    return eventType;
  }

  /**
   * Setter dla pola 'eventType'.
   *
   * @param eventType wartosc ustawiana dla pola 'eventType'.
   */
  public void setEventType(ItemEventType eventType) {
    this.eventType = eventType;
  }

  /**
   * Getter dla pola 'payload'.
   *
   * @return wartosc pola 'payload'.
   */
  public Map<String, Object> getPayload() {
    return payload;
  }

  /**
   * Setter dla pola 'payload'.
   *
   * @param payload wartosc ustawiana dla pola 'payload'.
   */
  public void setPayload(Map<String, Object> payload) {
    this.payload = payload;
  }
}
