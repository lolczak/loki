/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.decoder;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 26, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SearchArc {
  
  private SearchNode nextNode;

  private double transitionScore;

  /**
   * Getter for property 'nextNode'.
   *
   * @return Value for property 'nextNode'.
   */
  public SearchNode getNextNode() {
    return nextNode;
  }

  /**
   * Setter for property 'nextNode'.
   *
   * @param nextNode Value to set for property 'nextNode'.
   */
  public void setNextNode(SearchNode nextNode) {
    this.nextNode = nextNode;
  }

  /**
   * Getter for property 'transitionScore'.
   *
   * @return Value for property 'transitionScore'.
   */
  public double getTransitionScore() {
    return transitionScore;
  }

  /**
   * Setter for property 'transitionScore'.
   *
   * @param transitionScore Value to set for property 'transitionScore'.
   */
  public void setTransitionScore(double transitionScore) {
    this.transitionScore = transitionScore;
  }
}
