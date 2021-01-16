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
import org.speech.asr.gui.constant.JcrCorpusProperties;
import org.speech.asr.gui.dao.jcr.callback.BaseUuidJcrCallback;
import org.speech.asr.gui.dao.jcr.mapping.MapperUtils;
import org.speech.asr.common.entity.TranscribedUtterance;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 17, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class GetCorpusItemsCallback extends BaseUuidJcrCallback {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(GetCorpusItemsCallback.class.getName());

  public GetCorpusItemsCallback(String uuid) {
    super(uuid);
  }

  public Object doInJcr(Session session) throws IOException, RepositoryException {
    Node node = session.getNodeByUUID(getUuid());
    NodeIterator iter = node.getNodes(JcrCorpusProperties.TRANSCRIBED_UTTERANCE_NODE);
    List<TranscribedUtterance> utterances = new LinkedList();
    while (iter.hasNext()) {
      Node utteranceNode = iter.nextNode();
      log.debug("Mapping node {}", utteranceNode.getPath());
      utterances.add(MapperUtils.mapNode(utteranceNode, TranscribedUtterance.class));
    }
    log.debug("Loaded {} nodes at path {}", utterances.size(), node.getPath());
    return utterances;
  }
}
