package org.speech.asr.common.entity;

import org.speech.asr.common.entity.BaseEntity;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 22, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class DictionaryEntity extends BaseEntity {

  private String name;

  private String description;

  private String language;

  private String phoneticAlphabet;

  private List<Word> words;

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
   * Getter dla pola 'phoneticAlphabet'.
   *
   * @return wartosc pola 'phoneticAlphabet'.
   */
  public String getPhoneticAlphabet() {
    return phoneticAlphabet;
  }

  /**
   * Setter dla pola 'phoneticAlphabet'.
   *
   * @param phoneticAlphabet wartosc ustawiana dla pola 'phoneticAlphabet'.
   */
  public void setPhoneticAlphabet(String phoneticAlphabet) {
    this.phoneticAlphabet = phoneticAlphabet;
  }

  /**
   * Getter dla pola 'words'.
   *
   * @return wartosc pola 'words'.
   */
  public List<Word> getWords() {
    return words;
  }

  /**
   * Setter dla pola 'words'.
   *
   * @param words wartosc ustawiana dla pola 'words'.
   */
  public void setWords(List<Word> words) {
    this.words = words;
  }


  public String toString() {
    return "Dictionary{" +
        "description='" + description + '\'' +
        ", name='" + name + '\'' +
        ", language='" + language + '\'' +
        ", phoneticAlphabet='" + phoneticAlphabet + '\'' +
        ", words=" + words +
        '}';
  }
}
