package org.speech.asr.gui.event;

import org.speech.asr.common.event.Event;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 1, 2009 <br/>
 *
 * @author Lukasz Olczak
 */
public class EditorsContainerEvent implements Event {

  public enum EventType {
    CREATE_EDITOR, SELECT_EDITOR, CREATE_OR_SELECT_EDITOR, CLOSE_WITHOUT_SAVE, CLOSE
  }

  private Object documentId;

  private String editorName;

  private Object payload;

  /**
   * Getter dla pola 'payload'.
   *
   * @return wartosc pola 'payload'.
   */
  public Object getPayload() {
    return payload;
  }

  /**
   * Setter dla pola 'payload'.
   *
   * @param payload wartosc ustawiana dla pola 'payload'.
   */
  public void setPayload(Object payload) {
    this.payload = payload;
  }

  /**
   * Getter dla pola 'editorName'.
   *
   * @return wartosc pola 'editorName'.
   */
  public String getEditorName() {
    return editorName;
  }

  /**
   * Setter dla pola 'editorName'.
   *
   * @param editorName wartosc ustawiana dla pola 'editorName'.
   */
  public void setEditorName(String editorName) {
    this.editorName = editorName;
  }

  /**
   * Getter dla pola 'documentId'.
   *
   * @return wartosc pola 'documentId'.
   */
  public Object getDocumentId() {
    return documentId;
  }

  /**
   * Setter dla pola 'documentId'.
   *
   * @param documentId wartosc ustawiana dla pola 'documentId'.
   */
  public void setDocumentId(Object documentId) {
    this.documentId = documentId;
  }
}
