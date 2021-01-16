/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.dao.jcr.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.constant.JcrConstants;
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.common.jcr.JcrResource;
import org.springmodules.jcr.JcrCallback;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;
import java.io.InputStream;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 17, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SaveJcrResourceCallback implements JcrCallback {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SaveJcrResourceCallback.class.getName());

  private JcrResource jcrResource;

  private InputStream toPersist;

  public SaveJcrResourceCallback(JcrResource jcrResource, InputStream toPersist) {
    this.jcrResource = jcrResource;
    this.toPersist = toPersist;
    if (toPersist == null) {
      throw new AsrRuntimeException("There is no stream to persist");
    }
  }

  public Object doInJcr(Session session) throws IOException, RepositoryException {
    Node resourceNode = session.getNodeByUUID(jcrResource.getUuid());
    resourceNode.setProperty(JcrConstants.JCR_DATA_PROPERTY, toPersist);
    return null;  
  }
}
