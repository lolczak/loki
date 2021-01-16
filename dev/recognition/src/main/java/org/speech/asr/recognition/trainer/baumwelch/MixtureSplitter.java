package org.speech.asr.recognition.trainer.baumwelch;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jul 14, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface MixtureSplitter {

  int getNoMixtures();

  void split();
}
