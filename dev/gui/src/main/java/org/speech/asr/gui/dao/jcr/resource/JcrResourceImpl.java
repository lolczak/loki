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
import org.speech.asr.media.vo.AudioSource;
import org.speech.asr.common.jcr.JcrResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 12, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class JcrResourceImpl implements JcrResource {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(JcrResourceImpl.class.getName());

  protected String mimeType;

  protected String encoding;

  protected Calendar lastModified;

  private byte[] content;

  public JcrResourceImpl(AudioSource audioSource) {
    byte[] audioContent = audioSource.getAudioContent();
    content = audioContent != null ? audioContent : new byte[0];
    mimeType = "raw";//todo
    lastModified = Calendar.getInstance();
    encoding = audioSource.getAudioFormat().toString();
  }

  public byte[] getContentAsArray() {
    return content;
  }

  public InputStream getContentAsStream() {
    return new ByteArrayInputStream(content);
  }

  public String getUuid() {
    return null;  //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public void setContent(InputStream content) {
    //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public void setUuid(String uuid) {
    //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  /**
   * {@inheritDoc}
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * {@inheritDoc}
   */
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  /**
   * Getter for property 'content'.
   *
   * @return Value for property 'content'.
   */
  public byte[] getContent() {
    return content;
  }

  /**
   * {@inheritDoc}
   */
  public void setContent(byte[] content) {
    this.content = content;
  }

  /**
   * {@inheritDoc}
   */
  public Calendar getLastModified() {
    return lastModified;
  }

  /**
   * {@inheritDoc}
   */
  public void setLastModified(Calendar lastModified) {
    this.lastModified = lastModified;
  }

  /**
   * {@inheritDoc}
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * {@inheritDoc}
   */
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }
}
