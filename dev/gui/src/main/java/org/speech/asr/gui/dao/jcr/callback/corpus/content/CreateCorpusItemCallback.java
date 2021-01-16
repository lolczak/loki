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
import static org.speech.asr.gui.constant.JcrCorpusProperties.TRANSCRIBED_UTTERANCE_NODE;
import org.speech.asr.gui.dao.jcr.callback.BaseUuidJcrCallback;
import org.speech.asr.gui.dao.jcr.mapping.MapperUtils;
import org.speech.asr.common.entity.TranscribedUtterance;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 17, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class CreateCorpusItemCallback extends BaseUuidJcrCallback {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(CreateCorpusItemCallback.class.getName());

  private TranscribedUtterance utterance;

  public CreateCorpusItemCallback(String uuid, TranscribedUtterance utterance) {
    super(uuid);
    this.utterance = utterance;
  }

  public Object doInJcr(Session session) throws IOException, RepositoryException {
    log.debug("Persisting transcribed utterance {}", utterance);
    Node corpusNode = session.getNodeByUUID(getUuid());

    Node utteranceNode = corpusNode.addNode(TRANSCRIBED_UTTERANCE_NODE, TRANSCRIBED_UTTERANCE_NODE);
    MapperUtils.mapEntity(utterance, utteranceNode);
    utterance.setUuid(utteranceNode.getUUID());
    session.save();
    return utterance;
  }
}
