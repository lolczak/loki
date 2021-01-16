package org.speech.asr.common.entity;

import java.io.Serializable;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 24, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class Word implements Serializable {
  private static final long serialVersionUID = 5892520915430272882L;

  private String graphemes;

  private String phonemes;

  /**
   * Getter dla pola 'graphemes'.
   *
   * @return wartosc pola 'graphemes'.
   */
  public String getGraphemes() {
    return graphemes;
  }

  /**
   * Setter dla pola 'graphemes'.
   *
   * @param graphemes wartosc ustawiana dla pola 'graphemes'.
   */
  public void setGraphemes(String graphemes) {
    this.graphemes = graphemes;
  }

  /**
   * Getter dla pola 'phonemes'.
   *
   * @return wartosc pola 'phonemes'.
   */
  public String getPhonemes() {
    return phonemes;
  }

  /**
   * Setter dla pola 'phonemes'.
   *
   * @param phonemes wartosc ustawiana dla pola 'phonemes'.
   */
  public void setPhonemes(String phonemes) {
    this.phonemes = phonemes;
  }


  public String toString() {
    return "Word{" +
        "graphemes='" + graphemes + '\'' +
        ", phonemes='" + phonemes + '\'' +
        '}';
  }
}
