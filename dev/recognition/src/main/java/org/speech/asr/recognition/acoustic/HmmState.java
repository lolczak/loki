package org.speech.asr.recognition.acoustic;

import org.speech.asr.recognition.math.Pdf;

import java.util.Collection;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jun 19, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface HmmState extends State {

  String getId();

  boolean isEmitting();

  double score(Feature observation);

  Collection<HmmArc> getSuccessors();

  void setScorer(Pdf<Feature> pdf);

  Pdf<Feature> getScorer();

}
