package org.speech.asr.recognition.decoder;

import org.speech.asr.recognition.acoustic.Feature;

import java.util.Collection;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jul 26, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface SearchNode {

  boolean isEmitting();

  boolean isHypothesis();

  double score(Feature feature);

  Collection<SearchArc> getSuccessors();

  String getNodeId();

  String getWord();

  void addSuccessor(SearchNode next, double transitionScore);

  void addExclusiveSuccessor(SearchNode next, double transitionScore);
}
