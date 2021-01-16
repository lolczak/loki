/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.linguist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.AcousticModel;
import org.speech.asr.recognition.decoder.ConnectorSearchNode;
import org.speech.asr.recognition.decoder.SearchGraphSegment;
import org.speech.asr.recognition.decoder.SearchNode;
import org.speech.asr.recognition.decoder.WordSearchNode;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 26, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class WordListSearchGraphBuilder implements SearchGraphBuilder {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(WordListSearchGraphBuilder.class.getName());

  private static final String NODE_SUFFIX = "_node";

  private static final String START_NODE = "start_node";

  private static final String END_NODE = "end_node";

  private AcousticModel model;

  private Dictionary dictionary;

  private List<String> words;

  private double nullProbability;

  public SearchGraphSegment createSearchGraph() {
    log.debug("Creating search graph...");
    SearchNode startNode = new ConnectorSearchNode(START_NODE);
    SearchNode endNode = new ConnectorSearchNode(END_NODE);

    SearchNode lastNode;

    for (String word : words) {
      log.debug("Creating branch for {}", word);
      lastNode = startNode;
      Pronunciation pronunciation = dictionary.getPronunciation(word);
      for (String phoneme : pronunciation.getPhonemes()) {
        SearchGraphSegment phonemeSegment = model.getGraphSegment(phoneme);
        lastNode.addSuccessor(phonemeSegment.getInitialNode(), nullProbability);
        lastNode = phonemeSegment.getFinalNode();
      }
      WordSearchNode wordNode = new WordSearchNode(word + NODE_SUFFIX, word);
      lastNode.addSuccessor(wordNode, nullProbability);
      wordNode.addSuccessor(endNode, nullProbability);
    }
    //todo fix me
    endNode.addSuccessor(startNode, nullProbability - 1000);
    return new SearchGraphSegment(startNode, endNode);
  }

  protected void createFillerBranch() {
    //todo create filler branch in real system
  }

  /**
   * Setter for property 'dictionary'.
   *
   * @param dictionary Value to set for property 'dictionary'.
   */
  public void setDictionary(Dictionary dictionary) {
    this.dictionary = dictionary;
  }

  /**
   * Setter for property 'model'.
   *
   * @param model Value to set for property 'model'.
   */
  public void setModel(AcousticModel model) {
    this.model = model;
  }

  /**
   * Setter for property 'nullProbability'.
   *
   * @param nullProbability Value to set for property 'nullProbability'.
   */
  public void setNullProbability(double nullProbability) {
    this.nullProbability = nullProbability;
  }

  /**
   * Setter for property 'words'.
   *
   * @param words Value to set for property 'words'.
   */
  public void setWords(List<String> words) {
    this.words = words;
  }
}
