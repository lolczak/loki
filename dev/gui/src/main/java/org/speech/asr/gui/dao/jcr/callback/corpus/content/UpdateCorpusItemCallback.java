/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.dao.jcr.callback.corpus.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.dao.jcr.mapping.MapperUtils;
import org.speech.asr.common.entity.TranscribedUtterance;
import org.springmodules.jcr.JcrCallback;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 12, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class UpdateCorpusItemCallback implements JcrCallback {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(CreateCorpusItemCallback.class.getName());

  private TranscribedUtterance utterance;

  public UpdateCorpusItemCallback(TranscribedUtterance utterance) {
    this.utterance = utterance;
  }

  public Object doInJcr(Session session) throws IOException, RepositoryException {
    log.debug("Persisting transcribed utterance {}", utterance);
    Node utteranceNode = session.getNodeByUUID(utterance.getUuid());
    MapperUtils.mapEntity(utterance, utteranceNode);
    session.save();
    return utterance;
  }
}
