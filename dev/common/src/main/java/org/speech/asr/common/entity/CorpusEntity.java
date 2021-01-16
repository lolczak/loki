package org.speech.asr.common.entity;

import org.speech.asr.common.entity.BaseEntity;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 22, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class CorpusEntity extends BaseEntity {
  private String name;

  private String description;

  private String language;

  private JcrAudioFormat audioFormat;

  public CorpusEntity() {
    audioFormat = new JcrAudioFormat();
  }

  /**
   * Getter dla pola 'description'.
   *
   * @return wartosc pola 'description'.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Setter dla pola 'description'.
   *
   * @param description wartosc ustawiana dla pola 'description'.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Getter dla pola 'language'.
   *
   * @return wartosc pola 'language'.
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Setter dla pola 'language'.
   *
   * @param language wartosc ustawiana dla pola 'language'.
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Getter dla pola 'name'.
   *
   * @return wartosc pola 'name'.
   */
  public String getName() {
    return name;
  }

  /**
   * Setter dla pola 'name'.
   *
   * @param name wartosc ustawiana dla pola 'name'.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getter for property 'audioFormat'.
   *
   * @return Value for property 'audioFormat'.
   */
  public JcrAudioFormat getAudioFormat() {
    return audioFormat;
  }

  /**
   * Setter for property 'audioFormat'.
   *
   * @param audioFormat Value to set for property 'audioFormat'.
   */
  public void setAudioFormat(JcrAudioFormat audioFormat) {
    this.audioFormat = audioFormat;
  }

  public String toString() {
    return "Corpus{" +
        "audioFormat='" + audioFormat + '\'' +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", language='" + language + '\'' +
        '}';
  }
}
