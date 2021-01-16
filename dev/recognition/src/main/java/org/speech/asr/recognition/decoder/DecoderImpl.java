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
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.math.MathUtils;

import java.util.Collections;
import java.util.Comparator;
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
public class DecoderImpl implements Decoder {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DecoderImpl.class.getName());

  private SearchManager searchManager;

  private boolean isStarted = false;

  public void decode(Feature feature) {
    if (!isStarted) {
      searchManager.start();
      isStarted = true;
    }
    searchManager.feed(feature);
  }

  protected List<Result> getResults() {
    isStarted = false;
    searchManager.stop();
    List<Token> activeList = searchManager.getActiveList();
    List<Result> results = new LinkedList();

    for (Token token : activeList) {
      Result result = new ResultImpl(token.getHypothesisSentence(), token.getScore(), token.getFrameAlignments());
      results.add(result);
    }
    Collections.sort(results, new ResultComparator());
    return results;
  }

  public List<Result> getAllHypotheses() {
    return getResults();
  }

  public Result getBestHypothesis() {
    return getResults().get(0);
  }

  /**
   * Setter for property 'searchManager'.
   *
   * @param searchManager Value to set for property 'searchManager'.
   */
  public void setSearchManager(SearchManager searchManager) {
    this.searchManager = searchManager;
  }

  private class ResultComparator implements Comparator<Result> {
    public int compare(Result o1, Result o2) {
      assert MathUtils.isReal(o1.getScore()) : "Value " + o1.getScore();
      assert MathUtils.isReal(o2.getScore()) : "Value " + o2.getScore();
      if (o1.getScore() == o2.getScore()) {
        return 0;
      }
      if (o1.getScore() < o2.getScore()) {
        return 1;
      } else {
        return -1;
      }
    }
  }
}
