/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.view.editor.corpus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.dao.jcr.resource.JcrResourceImpl;
import org.speech.asr.common.entity.CorpusEntity;
import org.speech.asr.common.entity.TranscribedUtterance;
import org.speech.asr.gui.logic.CorpusBean;
import org.speech.asr.gui.widget.table.TableDataProvider;
import org.speech.asr.media.vo.AudioSource;
import org.speech.asr.media.vo.AudioSourceImpl;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 16, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class CorpusContentProvider implements TableDataProvider {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(CorpusContentProvider.class.getName());

  private CorpusBean corpusBean;

  private CorpusEntity corpus;

  private List<TranscribedUtterance> rows;

  public CorpusContentProvider(CorpusEntity corpus, CorpusBean corpusBean) {
    this.corpus = corpus;
    this.corpusBean = corpusBean;
    rows = corpusBean.getCorpusContent(corpus.getUuid());
  }

  public int getRowCount() {
    return rows.size();
  }

  public void insertRow(int rowIndex) {
    log.debug("Inserting row");
    AudioSource as = new AudioSourceImpl("new", corpus.getAudioFormat().toAudioFormat(), null);
    TranscribedUtterance tu = new TranscribedUtterance();
    tu.setTranscription(String.valueOf(rowIndex));
    tu.setAudioContent(new JcrResourceImpl(as));
    tu = corpusBean.addItem(corpus.getUuid(), tu);
    rows.add(rowIndex, tu);
  }

  public Object getValueAt(int rowIndex, String propertyName) {
    TranscribedUtterance tu = rows.get(rowIndex);
    if (propertyName == "transcription") {
      return tu.getTranscription();
    } else if (propertyName == "audioContent") {
      return new AudioSourceImpl("got", corpus.getAudioFormat().toAudioFormat(),
          tu.getAudioContent().getContentAsArray());
    }
    return null;
  }

  public void removeRow(int rowIndex) {
    log.debug("Removing row");
    TranscribedUtterance tu = rows.remove(rowIndex);
    corpusBean.deleteItem(tu.getUuid());
  }

  public void setValueAt(int rowIndex, String propertyName, Object value) {
    log.debug("Setting value row index {} property name {} value {}", new Object[]{rowIndex, propertyName, value});
    TranscribedUtterance tu = rows.get(rowIndex);
    if (propertyName == "transcription") {
      String transcription = (String) value;
      tu.setTranscription(transcription);
      corpusBean.updateItem(tu);
    } else if (propertyName == "audioContent") {
      AudioSource as = (AudioSource) value;
      tu.setAudioContent(new JcrResourceImpl(as));
      corpusBean.updateItem(tu);
    }
  }
}
