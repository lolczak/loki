package org.speech.asr.common.jcr;

import java.io.InputStream;
import java.util.Calendar;

/**
 * //@todo interface description
 * <p/>
 * Creation date: May 17, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface JcrResource {

  String getUuid();

  void setUuid(String uuid);

  InputStream getContentAsStream();

  byte[] getContentAsArray();

  void setContent(InputStream content);

  void setContent(byte[] content);

  String getEncoding();

  void setEncoding(String encoding);

  Calendar getLastModified();

  void setLastModified(Calendar lastModified);

  String getMimeType();

  void setMimeType(String mimeType);
}
