/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.dao.jcr.callback.corpus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.speech.asr.gui.constant.JcrConstants.ASR_ROOT_NODE_NAME;
import static org.speech.asr.gui.constant.JcrConstants.CORPORA_ROOT_NODE_NAME;
import static org.speech.asr.gui.constant.JcrCorpusProperties.CORPUS_NODE_NAME;
import org.speech.asr.gui.dao.jcr.mapping.MapperUtils;
import org.speech.asr.common.entity.CorpusEntity;
import org.springmodules.jcr.JcrCallback;

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
 * Creation date: May 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ListCorpusCallback implements JcrCallback {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ListCorpusCallback.class.getName());

  public Object doInJcr(Session session) throws IOException, RepositoryException {
    Node rootNode = session.getRootNode();
    Node asrRootNode = rootNode.getNode(ASR_ROOT_NODE_NAME);
    Node corporaNode = asrRootNode.getNode(CORPORA_ROOT_NODE_NAME);
    NodeIterator iterator = corporaNode.getNodes(CORPUS_NODE_NAME);
    List<CorpusEntity> corpora = new LinkedList();

    while (iterator.hasNext()) {
      Node corpusNode = iterator.nextNode();
      corpora.add(MapperUtils.mapNode(corpusNode, CorpusEntity.class));
    }
    return corpora;
  }
}
