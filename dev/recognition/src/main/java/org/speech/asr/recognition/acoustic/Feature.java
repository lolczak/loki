package org.speech.asr.recognition.acoustic;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jun 19, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface Feature {

  long getSequenceNumber();

  double[] getData();
}
