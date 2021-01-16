package org.speech.asr.recognition.acoustic;

import org.speech.asr.recognition.math.Pdf;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jun 21, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface LeftRightHmmState extends HmmState {

  void setNext(HmmState next);

  double getSelfLoopProbability();

  void setSelfLoopProbability(double prob);
}
