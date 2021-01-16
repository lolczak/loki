package org.speech.asr.recognition.math;

/**
 * Probability density function.
 * <p/>
 * Creation date: Jun 20, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface Pdf<V> {

  double getValue(V randomVariable);
}
