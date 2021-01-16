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
import static org.speech.asr.gui.constant.JcrCorpusProperties.*;
import org.speech.asr.common.entity.CorpusEntity;
import org.speech.asr.common.entity.JcrAudioFormat;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class CorpusMapper implements JcrMapper<CorpusEntity> {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(CorpusMapper.class.getName());

  public CorpusEntity mapNode(Node node) throws RepositoryException {
    //todo fixme dodac walidacje
    CorpusEntity corpus = new CorpusEntity();

    corpus.setUuid(node.getUUID());
    corpus.setName(node.getProperty(NAME_PROPERTY).getString());
    corpus.setDescription(node.getProperty(DESCRIPTION_PROPERTY).getString());
    corpus.setLanguage(node.getProperty(LANGUAGE_PROPERTY).getString());
    corpus.setAudioFormat(MapperUtils.mapNode(node.getNode(AUDIO_FORMAT_PROPERTY), JcrAudioFormat.class));
    return corpus;
  }

  public void mapEntity(CorpusEntity fromEntity, Node toNode) throws RepositoryException {
    toNode.setProperty(NAME_PROPERTY, fromEntity.getName());
    toNode.setProperty(DESCRIPTION_PROPERTY, fromEntity.getDescription());
    toNode.setProperty(LANGUAGE_PROPERTY, fromEntity.getLanguage());
    Node audioFormatNode;
    if (toNode.hasNode(AUDIO_FORMAT_PROPERTY)) {
      audioFormatNode = toNode.getNode(AUDIO_FORMAT_PROPERTY);
    } else {
      audioFormatNode = toNode.addNode(AUDIO_FORMAT_PROPERTY, AUDIO_FORMAT_PROPERTY);
    }
    MapperUtils.mapEntity(fromEntity.getAudioFormat(), audioFormatNode);
  }
}
