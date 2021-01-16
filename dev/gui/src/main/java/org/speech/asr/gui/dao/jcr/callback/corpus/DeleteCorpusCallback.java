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
import org.speech.asr.gui.dao.jcr.callback.BaseUuidJcrCallback;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class DeleteCorpusCallback extends BaseUuidJcrCallback {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DeleteCorpusCallback.class.getName());

  public DeleteCorpusCallback(String uuid) {
    super(uuid);
  }

  public Object doInJcr(Session session) throws IOException, RepositoryException {
    Node node = session.getNodeByUUID(getUuid());
    node.remove();
    session.save();
    return null;
  }
}
