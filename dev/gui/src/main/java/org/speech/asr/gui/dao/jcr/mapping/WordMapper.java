/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.dao.jcr.mapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.speech.asr.gui.constant.JcrDictionaryProperties.WORD_GRAPHEMES_PROPERTY;
import static org.speech.asr.gui.constant.JcrDictionaryProperties.WORD_PHONEMES_PROPERTY;
import org.speech.asr.common.entity.Word;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 10, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class WordMapper implements JcrMapper<Word> {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(WordMapper.class.getName());

  public Word mapNode(Node node) {
    Word word = null;
    try {
      word = new Word();
      word.setGraphemes(node.getProperty(WORD_GRAPHEMES_PROPERTY).getString());
      word.setPhonemes(node.getProperty(WORD_PHONEMES_PROPERTY).getString());
    } catch (RepositoryException e) {
      throw new IllegalStateException(e);
    }

    return word;
  }

  public void mapEntity(Word fromEntity, Node toNode) throws RepositoryException {
    //TODO To change body of implemented methods use File | Settings | File Templates.
  }
}
