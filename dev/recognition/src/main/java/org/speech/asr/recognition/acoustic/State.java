package org.speech.asr.recognition.acoustic;

import org.speech.asr.recognition.math.Pdf;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Aug 11, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface State {
  String getId();

  Pdf<Feature> getScorer();
}
