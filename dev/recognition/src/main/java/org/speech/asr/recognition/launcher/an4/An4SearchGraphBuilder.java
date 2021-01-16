/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.launcher.an4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.recognition.acoustic.AcousticModel;
import org.speech.asr.recognition.acoustic.PhoneticUnit;
import org.speech.asr.recognition.decoder.ConnectorSearchNode;
import org.speech.asr.recognition.decoder.SearchGraphSegment;
import org.speech.asr.recognition.decoder.SearchNode;
import org.speech.asr.recognition.decoder.WordSearchNode;
import org.speech.asr.recognition.linguist.Dictionary;
import org.speech.asr.recognition.linguist.Pronunciation;
import org.speech.asr.recognition.linguist.SearchGraphBuilder;
import org.speech.asr.recognition.util.StringUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 15, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class An4SearchGraphBuilder implements SearchGraphBuilder {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(An4SearchGraphBuilder.class.getName());

  protected static final String NODE_SUFFIX = "_node";

  protected static final String START_NODE = "start_node";

  protected static final String END_NODE = "end_node";

  protected AcousticModel model;

  protected Dictionary dictionary;

  protected Set<String> sentences;

  protected double nullProbability;

  public SearchGraphSegment createSearchGraph() {
    log.debug("Creating search graph for {} branches...", sentences.size());
    SearchNode startNode = new ConnectorSearchNode(START_NODE);
    SearchNode endNode = new ConnectorSearchNode(END_NODE);
    SearchNode lastNode;

    SearchGraphSegment startSegment = model.getGraphSegment("<sil>");
    startNode.addSuccessor(startSegment.getInitialNode(), nullProbability);
    SearchNode startFillerNode = startSegment.getFinalNode();

    SearchGraphSegment endSegment = model.getGraphSegment("</sil>");
    SearchNode endFillerNode = endSegment.getInitialNode();
    endSegment.getFinalNode().addSuccessor(endNode, nullProbability);

    for (String sentence : sentences) {
      log.debug("Creating branch for {}", sentence);
      lastNode = startFillerNode;
      List<PhoneticUnit> pronunciation = createTranscription(sentence);
      for (PhoneticUnit phoneme : pronunciation) {
        SearchGraphSegment phonemeSegment = model.getGraphSegment(phoneme.getName());
        lastNode.addSuccessor(phonemeSegment.getInitialNode(), nullProbability);
        lastNode = phonemeSegment.getFinalNode();
      }
      WordSearchNode wordNode = new WordSearchNode(sentence + NODE_SUFFIX, sentence);
      lastNode.addSuccessor(wordNode, nullProbability);
      wordNode.addSuccessor(endFillerNode, nullProbability);
    }

    //wywalam cykl
//    endNode.addSuccessor(startNode, nullProbability - 1000);
    return new SearchGraphSegment(startNode, endNode);
  }

  protected void createFillerBranch() {
    //todo create filler branch in real system
  }

  protected List<PhoneticUnit> createTranscription(String txt) {
    List<String> words = StringUtils.parse(txt);
    PhoneticUnit filler = model.getPhoneticUnit("@");
    List<PhoneticUnit> result = new LinkedList();
//    result.add(filler);
    for (String word : words) {
      Pronunciation pronunciation = dictionary.getPronunciation(word);
      for (String phoneme : pronunciation.getPhonemes()) {
        PhoneticUnit unit = model.getPhoneticUnit(phoneme);
        if (unit == null) {
          throw new AsrRuntimeException("There is no unit " + phoneme + " in model");
        }
        result.add(unit);
      }
    }
//    result.add(filler);
    return result;
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
   * @param sentences Value to set for property 'words'.
   */
  public void setSentences(List<String> sentences) {
    this.sentences = new HashSet(sentences);
  }
}
