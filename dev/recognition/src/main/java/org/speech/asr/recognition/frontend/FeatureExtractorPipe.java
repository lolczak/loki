package org.speech.asr.recognition.frontend;

import org.speech.asr.recognition.acoustic.Feature;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Aug 5, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface FeatureExtractorPipe {

  void write(AudioPayload payload);

  void stop();

  Feature read();

}
