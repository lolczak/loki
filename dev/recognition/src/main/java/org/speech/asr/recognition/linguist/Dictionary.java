package org.speech.asr.recognition.linguist;

import java.util.List;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jul 26, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface Dictionary {

  Pronunciation getPronunciation(String spelling);

  List<Pronunciation> getAllPronunciation();

}
