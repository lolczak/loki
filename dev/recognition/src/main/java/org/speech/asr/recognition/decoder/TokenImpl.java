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
public class TokenImpl implements Token {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(TokenImpl.class.getName());

  private List<FrameAlignment> frameAlignments;

  private List<String> words;

  private SearchNode activeNode;

  private List<SearchNode> nodeTrace;

  private double score;

  protected TokenImpl() {
    words = new LinkedList();
    frameAlignments = new LinkedList();
    nodeTrace = new LinkedList();
  }

  public TokenImpl(SearchNode activeNode, double score) {
    this();
    this.activeNode = activeNode;
    this.score = score;
    nodeTrace.add(activeNode);
    if (activeNode.isHypothesis()) {
      words.add(activeNode.getWord());
    }
  }

  public TokenImpl(Token prevToken, SearchNode activeNode, double score) {
    this();
    frameAlignments.addAll(prevToken.getFrameAlignments());
    words.addAll(prevToken.getWordHypotheses());
    this.activeNode = activeNode;
    this.score = score;
    nodeTrace.addAll(prevToken.getNodeTrace());
    if (!prevToken.getActiveNode().getNodeId().equals(activeNode.getNodeId())) {
      nodeTrace.add(activeNode);
    }
    if (activeNode.isHypothesis()) {
      words.add(activeNode.getWord());
    }
  }

  public TokenImpl(Token prevToken, SearchNode activeNode, double score, FrameAlignment frameAlignment) {
    this(prevToken, activeNode, score);
    frameAlignments.add(frameAlignment);
  }

  public SearchNode getActiveNode() {
    return activeNode;
  }

  public List<FrameAlignment> getFrameAlignments() {
    return frameAlignments;
  }

  public String getHypothesisSentence() {
    StringBuilder sb = new StringBuilder();
    for (String word : words) {
      sb.append(word).append(" ");
    }

    return sb.toString().trim();
  }

  public double getScore() {
    return score;
  }

  public List<String> getWordHypotheses() {
    return words;
  }

  public List<SearchNode> getNodeTrace() {
    return nodeTrace;
  }

  public String toString() {
    return "TokenImpl{" +
        "activeNode=" + activeNode +
        ", words=" + words +
        ", score=" + score +
        '}';
  }
}
