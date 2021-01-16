package org.speech.asr.recognition.decoder;

import org.speech.asr.recognition.acoustic.Feature;

import java.util.List;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jul 26, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface Decoder {

  void decode(Feature feature);

  Result getBestHypothesis();

  List<Result> getAllHypotheses();

}
