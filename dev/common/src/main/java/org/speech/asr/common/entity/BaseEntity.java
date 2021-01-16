package org.speech.asr.common.entity;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 22, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public abstract class BaseEntity {

  protected String uuid;

  /**
   * Getter dla pola 'uuid'.
   *
   * @return wartosc pola 'uuid'.
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * Setter dla pola 'uuid'.
   *
   * @param uuid wartosc ustawiana dla pola 'uuid'.
   */
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
}
