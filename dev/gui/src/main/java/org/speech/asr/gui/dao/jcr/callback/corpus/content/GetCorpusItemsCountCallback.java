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

import javax.jcr.Node;
import javax.jcr.NodeIterator;
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
public class GetCorpusItemsCountCallback extends BaseUuidJcrCallback {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(GetCorpusItemsCountCallback.class.getName());

  public GetCorpusItemsCountCallback(String uuid) {
    super(uuid);
  }

  public Object doInJcr(Session session) throws IOException, RepositoryException {
    // count nie jest zaimplementowany w xpath jcr
    Node node = session.getNodeByUUID(getUuid());
    NodeIterator iter = node.getNodes(JcrCorpusProperties.TRANSCRIBED_UTTERANCE_NODE);
    int i = 0;
    while (iter.hasNext()) {
      i++;
      iter.nextNode();
    }
    log.debug("Found {} nodes at path {}", i, node.getPath());
    return i;
  }
}
