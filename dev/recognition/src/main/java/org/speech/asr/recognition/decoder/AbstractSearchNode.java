/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 27, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public abstract class AbstractSearchNode implements SearchNode {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(AbstractSearchNode.class.getName());

  protected List<SearchArc> successors;

  public AbstractSearchNode() {
    successors = new LinkedList();
  }

  public void addExclusiveSuccessor(SearchNode next, double transitionScore) {
    successors.clear();
    addSuccessor(next, transitionScore);
  }

  public void addSuccessor(SearchNode next, double transitionScore) {
    SearchArc arc = new SearchArc();
    arc.setNextNode(next);
    arc.setTransitionScore(transitionScore);
    successors.add(arc);
  }

  public void addSelfLoop(double transitionScore) {
    addSuccessor(this, transitionScore);
  }

  public Collection<SearchArc> getSuccessors() {
    return new LinkedList(successors);
  }


  public String toString() {
    return "AbstractSearchNode{" +
        "successors=" + successors +
        '}';
  }
}
