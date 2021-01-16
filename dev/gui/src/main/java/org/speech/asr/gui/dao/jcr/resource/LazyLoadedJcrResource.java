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
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.common.jcr.JcrResource;
import org.speech.asr.common.entity.BaseEntity;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 16, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class LazyLoadedJcrResource extends BaseEntity implements JcrResource {

  private static final Logger log = LoggerFactory.getLogger(LazyLoadedJcrResource.class.getName());

  protected String mimeType;

  protected String encoding;

  protected Calendar lastModified;

  private boolean isContentLoaded;

  private byte[] content;

  private JcrTemplate jcrTemplate;

  public LazyLoadedJcrResource() {
    isContentLoaded = false;
    jcrTemplate = JcrTemplateExt.JCR_TEMPLATE.get();
    if (jcrTemplate == null) {
      log.error("JcrTemplate is not set, lazy load will be impossible");
    }
  }

  public InputStream getContentAsStream() {
    if (!isContentLoaded) {
      loadContent();
    }
    return new ByteArrayInputStream(content);
  }

  public byte[] getContentAsArray() {
    if (!isContentLoaded) {
      loadContent();
    }
    return content;
  }

  public void setContent(InputStream content) {
    persistContent(content);
  }

  public void setContent(byte[] content) {
    persistContent(new ByteArrayInputStream(content));
  }

  private void loadContent() {
    log.debug("Lazily loading jcr resource with uuid {}", uuid);
    if (jcrTemplate == null) {
      throw new AsrRuntimeException("JcrTemplate not set for lazy load");
    }
    JcrCallback loadClbck = new LoadJcrResourceCallback(this);
    content = (byte[]) jcrTemplate.execute(loadClbck);
    isContentLoaded = true;
  }

  private void persistContent(InputStream in) {
    log.debug("Persisting jcr resource {}", uuid);
    JcrCallback saveClbck = new SaveJcrResourceCallback(this, in);
    jcrTemplate.execute(saveClbck);
    isContentLoaded = false;
  }

  /**
   * Getter dla pola 'encoding'.
   *
   * @return wartosc pola 'encoding'.
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * Setter dla pola 'encoding'.
   *
   * @param encoding wartosc ustawiana dla pola 'encoding'.
   */
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  /**
   * Getter dla pola 'lastModified'.
   *
   * @return wartosc pola 'lastModified'.
   */
  public Calendar getLastModified() {
    return lastModified;
  }

  /**
   * Setter dla pola 'lastModified'.
   *
   * @param lastModified wartosc ustawiana dla pola 'lastModified'.
   */
  public void setLastModified(Calendar lastModified) {
    this.lastModified = lastModified;
  }

  /**
   * Getter dla pola 'mimeType'.
   *
   * @return wartosc pola 'mimeType'.
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * Setter dla pola 'mimeType'.
   *
   * @param mimeType wartosc ustawiana dla pola 'mimeType'.
   */
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }
}
