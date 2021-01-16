/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.dao.jcr.callback.corpus.CreateCorpusCallback;
import org.speech.asr.gui.dao.jcr.callback.corpus.DeleteCorpusCallback;
import org.speech.asr.gui.dao.jcr.callback.corpus.ListCorpusCallback;
import org.speech.asr.gui.dao.jcr.callback.corpus.UpdateCorpusCallback;
import org.speech.asr.gui.dao.jcr.callback.corpus.content.*;
import org.speech.asr.common.entity.CorpusEntity;
import org.speech.asr.common.entity.TranscribedUtterance;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class CorpusBeanImpl implements CorpusBean {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(CorpusBeanImpl.class.getName());

  private JcrTemplate jcrTemplate;

  public String create(CorpusEntity corpus) {
    log.debug("Creating corpus {}", corpus);
    JcrCallback createCallback = new CreateCorpusCallback(corpus);
    String uuid = (String) jcrTemplate.execute(createCallback);
    corpus.setUuid(uuid);
    jcrTemplate.save();
    return uuid;
  }

  public void delete(String uuid) {
    log.debug("Deleting corpus with uuid {}", uuid);
    JcrCallback deleteClbck = new DeleteCorpusCallback(uuid);

    jcrTemplate.execute(deleteClbck);
  }

  public List<CorpusEntity> getAllCorpora() {
    log.debug("Getting all corpora");
    JcrCallback listClbck = new ListCorpusCallback();

    return (List) jcrTemplate.execute(listClbck);
  }

  public CorpusEntity getByUuid(String uuid) {
    return null;  //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public List<TranscribedUtterance> getCorpusContent(String uuid) {
    log.debug("Getting corpus content for corpus with uuid {}", uuid);
    JcrCallback contentClbck = new GetCorpusItemsCallback(uuid);
    return (List) jcrTemplate.execute(contentClbck);
  }

  public TranscribedUtterance addItem(String corpusUuid, TranscribedUtterance tu) {
    log.debug("Adding utterance {} to corpus uuid {}", tu, corpusUuid);
    JcrCallback createClbck = new CreateCorpusItemCallback(corpusUuid, tu);

    return (TranscribedUtterance) jcrTemplate.execute(createClbck);
  }

  public TranscribedUtterance updateItem(TranscribedUtterance tu) {
    log.debug("Updating utterance {}", tu);
    JcrCallback updateClbck = new UpdateCorpusItemCallback(tu);
    return (TranscribedUtterance) jcrTemplate.execute(updateClbck);
  }

  public void deleteItem(String uuid) {
    log.debug("Deleting corpus item with uuid {}", uuid);
    JcrCallback deleteClbck = new DeleteCorpusItemCallback(uuid);
    jcrTemplate.execute(deleteClbck);
  }

  public void saveCorpusContent(String uuid, List<TranscribedUtterance> utternaces) {
    //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public int getItemsCount(String uuid) {
    log.debug("Getting count for corpus with uuid {}", uuid);
    JcrCallback jcrCountClbck = new GetCorpusItemsCountCallback(uuid);
    Integer count = (Integer) jcrTemplate.execute(jcrCountClbck);

    return count;
  }

  public CorpusEntity update(CorpusEntity corpus) {
    log.debug("Updating corpus {}", corpus);
    JcrCallback updateClbck = new UpdateCorpusCallback(corpus);
    return (CorpusEntity) jcrTemplate.execute(updateClbck);
  }

  /**
   * Setter dla pola 'jcrTemplate'.
   *
   * @param jcrTemplate wartosc ustawiana dla pola 'jcrTemplate'.
   */
  public void setJcrTemplate(JcrTemplate jcrTemplate) {
    this.jcrTemplate = jcrTemplate;
  }
}
