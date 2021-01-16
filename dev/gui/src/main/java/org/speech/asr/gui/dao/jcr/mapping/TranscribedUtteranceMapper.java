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
import static org.speech.asr.gui.constant.JcrCorpusProperties.AUDIO_CONTENT_NODE;
import static org.speech.asr.gui.constant.JcrCorpusProperties.TRANSCRIPTION_PROPERTY;
import org.speech.asr.common.jcr.JcrResource;
import org.speech.asr.gui.dao.jcr.resource.LazyLoadedJcrResource;
import org.speech.asr.common.entity.TranscribedUtterance;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 17, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class TranscribedUtteranceMapper implements JcrMapper<TranscribedUtterance> {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(TranscribedUtteranceMapper.class.getName());

  public void mapEntity(TranscribedUtterance fromEntity, Node toNode) throws RepositoryException {
    toNode.setProperty(TRANSCRIPTION_PROPERTY, fromEntity.getTranscription());
    Node resourceNode;
    if (toNode.hasNode(AUDIO_CONTENT_NODE)) {
      resourceNode = toNode.getNode(AUDIO_CONTENT_NODE);
    } else {
      resourceNode = toNode.addNode(AUDIO_CONTENT_NODE, JcrConstants.ASR_RESOURCE_TYPE);
    }
    MapperUtils.mapEntity(fromEntity.getAudioContent(), resourceNode);
  }

  public TranscribedUtterance mapNode(Node node) throws RepositoryException {
    TranscribedUtterance tu = new TranscribedUtterance();
    tu.setUuid(node.getUUID());
    tu.setTranscription(node.getProperty(TRANSCRIPTION_PROPERTY).getString());
    Node resourceNode = node.getNode(AUDIO_CONTENT_NODE);
    JcrResource jcrResource = MapperUtils.mapNode(resourceNode, LazyLoadedJcrResource.class);
    tu.setAudioContent(jcrResource);
    return tu;
  }
}
