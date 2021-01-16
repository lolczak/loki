/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.dao.jcr.callback.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.constant.JcrConstants;
import static org.speech.asr.gui.constant.JcrDictionaryProperties.CONTENT_CHILD;
import org.speech.asr.gui.dao.jcr.callback.BaseUuidJcrCallback;
import org.speech.asr.common.entity.Word;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Calendar;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 10, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SaveDictionaryContentCallback extends BaseUuidJcrCallback {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SaveDictionaryContentCallback.class.getName());

  private List<Word> words;

  public SaveDictionaryContentCallback(String uuid, List<Word> words) {
    super(uuid);
    this.words = words;
  }

  public Object doInJcr(Session session) throws IOException, RepositoryException {
    Node node = session.getNodeByUUID(getUuid());
    if (node.hasNode(CONTENT_CHILD)) {
      Node contentNode = node.getNode(CONTENT_CHILD);
      contentNode.remove();
    }

    PipedOutputStream out = new PipedOutputStream();
    PipedInputStream in = new PipedInputStream(out);
    final ObjectOutputStream oos = new ObjectOutputStream(out);
    //todo synchronization
    new Thread() {
      public void run() {
        try {
          log.debug("Starting saving dictionary content");
          oos.writeObject(words);
        } catch (Exception e) {
        } finally {
          log.debug("Dictionary content finished");
          try {
            oos.close();
          } catch (IOException e) {
          }
        }
      }
    }.start();

    Node resNode = node.addNode(CONTENT_CHILD, JcrConstants.ASR_RESOURCE_TYPE);
    resNode.setProperty(JcrConstants.JCR_MIME_TYPE_PROPERTY, "application/octet-stream");
    resNode.setProperty(JcrConstants.JCR_ENCODING_PROPERTY, "UTF-8");
    resNode.setProperty(JcrConstants.JCR_DATA_PROPERTY, in);
    resNode.setProperty(JcrConstants.JCR_LAST_MODIFIED_PROPERTY, Calendar.getInstance());
    session.save();
    return node;
  }
}
