/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.view.editor.corpus.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.common.entity.TranscribedUtterance;
import org.speech.asr.gui.context.AppContext;
import org.speech.asr.gui.dao.jcr.resource.JcrResourceImpl;
import org.speech.asr.gui.logic.CorpusBean;
import org.speech.asr.gui.widget.progress.ProgressWidget;
import org.speech.asr.media.converter.WaveConverter;
import org.speech.asr.media.vo.AudioSource;

import javax.media.format.AudioFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 12, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class An4CorpusImporter {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(An4CorpusImporter.class.getName());

  private static final String separator = " ";

  private CorpusBean corpusBean;

  private String corpusUuid;

  private ProgressWidget progress;

  public An4CorpusImporter(String corpusUuid, CorpusBean corpusBean) {
    this.corpusBean = corpusBean;
    this.corpusUuid = corpusUuid;
  }

  public List<TranscribedUtterance> importCorpus(File file, File rootDir, AudioFormat format) {
    progress = AppContext.getInstance().getProgressWidget();
    progress.start();
    progress.setStatus("Starting import of corpus...");
    FileReader fileReader;
    BufferedReader reader;
    String line = null;
    List<TranscribedUtterance> words = new LinkedList();
    try {
      progress.setStatus("Parsing prompt file...");
      fileReader = new FileReader(file);
      reader = new BufferedReader(fileReader);
      List<UtteranceHolder> items = new LinkedList();
      while ((line = reader.readLine()) != null) {
        int separatorIndex = line.indexOf(separator);
        String wavePath = line.substring(0, separatorIndex);
        String transcription = line.substring(separatorIndex + 1, line.length());
        UtteranceHolder item = new UtteranceHolder(wavePath + ".wav", transcription);
        items.add(item);
      }
      int size = items.size();
      progress.start(size);
      progress.setStatus("Converting audio files...");
      int i = 0;
      for (UtteranceHolder item : items) {
        progress.log("Converting file " + item.getWavePath());
        File waveFile = new File(rootDir, item.getWavePath());
        AudioSource as = WaveConverter.syncConvert(waveFile, format);
        TranscribedUtterance tu = new TranscribedUtterance();
        tu.setTranscription(item.getTranscription());
        tu.setAudioContent(new JcrResourceImpl(as));
        corpusBean.addItem(corpusUuid, tu);
        progress.progress(++i);
      }
      progress.setStatus("Import successfully finished");
    } catch (Exception e) {
      log.error("", e);
      throw new AsrRuntimeException(e);
    } finally {
      progress.stop();
    }

    return words;
  }

  private class UtteranceHolder {
    private String wavePath;

    private String transcription;

    public UtteranceHolder(String path, String txt) {
      this.wavePath = path;
      this.transcription = txt;
    }

    public String getTranscription() {
      return transcription;
    }

    public void setTranscription(String transcription) {
      this.transcription = transcription;
    }

    public String getWavePath() {
      return wavePath;
    }

    public void setWavePath(String wavePath) {
      this.wavePath = wavePath;
    }
  }
}
