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
import org.speech.asr.gui.dao.jcr.mapping.MapperUtils;
import org.speech.asr.common.entity.CorpusEntity;
import org.springmodules.jcr.JcrCallback;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 14, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class UpdateCorpusCallback implements JcrCallback {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(UpdateCorpusCallback.class.getName());

  private CorpusEntity corpus;

  public UpdateCorpusCallback(CorpusEntity corpus) {
    this.corpus = corpus;
  }

  public Object doInJcr(Session session) throws IOException, RepositoryException {
    Node node = session.getNodeByUUID(corpus.getUuid());
    if (node == null) {
      throw new RepositoryException("There is no node with uuid " + corpus.getUuid());
    }
    MapperUtils.mapEntity(corpus, node);
    session.save();
    log.debug("Corpus node {} updated.", node.getPath());
    return corpus;
  }
}
