package org.speech.asr.gui.event;

import org.apache.commons.collections.map.HashedMap;
import org.speech.asr.common.event.Event;
import org.speech.asr.gui.constant.ItemEventType;

import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 28, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class DictionaryEvent implements Event {

  public static final String UUID_KEY = "__uuid_key";

  public static final String CHANGED_UUIDS_KEY = "__changed_uuids_key";

  private ItemEventType eventType;

  private Map<String, Object> payload;

  public static DictionaryEvent createNewDictionaryEvent(String uuid) {
    DictionaryEvent event = new DictionaryEvent();
    event.setEventType(ItemEventType.NEW);
    Map<String, Object> payload = new HashedMap();
    payload.put(UUID_KEY, uuid);
    event.setPayload(payload);
    return event;
  }

  public static DictionaryEvent createUpdatedDictionaryEvent(String uuid) {
    DictionaryEvent event = new DictionaryEvent();
    event.setEventType(ItemEventType.UPDATED);
    Map<String, Object> payload = new HashedMap();
    payload.put(UUID_KEY, uuid);
    event.setPayload(payload);
    return event;
  }

  public static DictionaryEvent createDeleteDictionaryEvent(String uuid) {
    DictionaryEvent event = new DictionaryEvent();
    event.setEventType(ItemEventType.DELETED);
    Map<String, Object> payload = new HashedMap();
    payload.put(UUID_KEY, uuid);
    event.setPayload(payload);
    return event;
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
}
