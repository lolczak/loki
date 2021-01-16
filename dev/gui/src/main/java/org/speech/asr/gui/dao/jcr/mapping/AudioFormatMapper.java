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
import static org.speech.asr.gui.constant.JcrAudioFormatProperties.*;
import org.speech.asr.common.entity.JcrAudioFormat;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 10, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class AudioFormatMapper implements JcrMapper<JcrAudioFormat> {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(AudioFormatMapper.class.getName());

  public void mapEntity(JcrAudioFormat fromEntity, Node toNode) throws RepositoryException {
    toNode.setProperty(ENCODING_PROPERTY, fromEntity.getEncoding());
    toNode.setProperty(ENDIAN_PROPERTY, fromEntity.getEndian());
    toNode.setProperty(SAMPLE_SIZE_PROPERTY, fromEntity.getSampleSize());
    toNode.setProperty(SAMPLING_RATE_PROPERTY, fromEntity.getSamplingRate());
    toNode.setProperty(SIGNED_PROPERTY, fromEntity.isSigned());
  }

  public JcrAudioFormat mapNode(Node node) throws RepositoryException {
    //todo fixme dodac walidacje
    JcrAudioFormat audioFormat = new JcrAudioFormat();
    audioFormat.setUuid(node.getUUID());
    audioFormat.setEncoding(node.getProperty(ENCODING_PROPERTY).getString());
    audioFormat.setEndian((int) node.getProperty(ENDIAN_PROPERTY).getLong());
    audioFormat.setSampleSize((int) node.getProperty(SAMPLE_SIZE_PROPERTY).getLong());
    audioFormat.setSamplingRate((int) node.getProperty(SAMPLING_RATE_PROPERTY).getLong());
    audioFormat.setSigned(node.getProperty(SIGNED_PROPERTY).getBoolean());
    return audioFormat;
  }
}
