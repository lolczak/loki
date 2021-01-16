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
import org.speech.asr.gui.constant.JcrConstants;
import org.speech.asr.common.jcr.JcrResource;
import org.speech.asr.gui.dao.jcr.resource.LazyLoadedJcrResource;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Calendar;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 17, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class JcrResourceMapper implements JcrMapper<JcrResource> {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(JcrResourceMapper.class.getName());

  public void mapEntity(JcrResource fromEntity, Node toNode) throws RepositoryException {
    toNode.setProperty(JcrConstants.JCR_MIME_TYPE_PROPERTY, fromEntity.getMimeType());
    toNode.setProperty(JcrConstants.JCR_ENCODING_PROPERTY, fromEntity.getEncoding());
    if (fromEntity.getLastModified() == null) {
      fromEntity.setLastModified(Calendar.getInstance());
    }
    toNode.setProperty(JcrConstants.JCR_LAST_MODIFIED_PROPERTY, fromEntity.getLastModified());
    toNode.setProperty(JcrConstants.JCR_DATA_PROPERTY, fromEntity.getContentAsStream());
  }

  public JcrResource mapNode(Node node) throws RepositoryException {
    LazyLoadedJcrResource jcrResource = new LazyLoadedJcrResource();
    jcrResource.setUuid(node.getUUID());
    jcrResource.setMimeType(node.getProperty(JcrConstants.JCR_MIME_TYPE_PROPERTY).getString());
    jcrResource.setEncoding(node.getProperty(JcrConstants.JCR_ENCODING_PROPERTY).getString());
    jcrResource.setLastModified(node.getProperty(JcrConstants.JCR_LAST_MODIFIED_PROPERTY).getDate());
    return jcrResource;
  }
}
