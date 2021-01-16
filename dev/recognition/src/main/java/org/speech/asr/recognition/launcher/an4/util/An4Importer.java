/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.launcher.an4.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.media.converter.WaveConverter;
import org.speech.asr.media.vo.AudioSource;

import javax.media.format.AudioFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 6, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class An4Importer {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(An4Importer.class.getName());

  private static final String separator = " ";

  private An4Importer() {

  }

  public static List<TranscribedUtterance> parseTranscriptionFile(File file) {
    log.debug("Parsing an4 transcription file...");
    FileReader fileReader;
    BufferedReader reader;
    String line = null;
    List<TranscribedUtterance> words = new LinkedList();
    try {
      log.debug("Parsing prompt file...");
      fileReader = new FileReader(file);
      reader = new BufferedReader(fileReader);
      List<UtteranceHolder> items = new LinkedList();
      while ((line = reader.readLine()) != null) {
        int separatorIndex = line.indexOf(separator);
        String wavePath = line.substring(0, separatorIndex);
        String transcription = line.substring(separatorIndex + 1, line.length());
        UtteranceHolder item = new UtteranceHolder(wavePath, transcription);
        items.add(item);
      }
      int size = items.size();
      log.debug("Converting audio files...");
      int i = 0;
      for (UtteranceHolder item : items) {
        log.debug("Converting file " + item.getWavePath());
        TranscribedUtterance tu = new TranscribedUtterance();
        tu.setRelativePath(item.getWavePath());
        tu.setTranscription(item.getTranscription().trim());
        words.add(tu);
      }
      log.debug("Import successfully finished");
    } catch (Exception e) {
      log.error("", e);
      throw new AsrRuntimeException(e);
    }

    return words;
  }

  public static List<TranscribedUtterance> importCorpus(File file, File rootDir, AudioFormat format) {
    log.debug("Starting import of corpus...");
    FileReader fileReader;
    BufferedReader reader;
    String line = null;
    List<TranscribedUtterance> words = new LinkedList();
    try {
      log.debug("Parsing prompt file...");
      fileReader = new FileReader(file);
      reader = new BufferedReader(fileReader);
      List<UtteranceHolder> items = new LinkedList();
      while ((line = reader.readLine()) != null) {
        int separatorIndex = line.indexOf(separator);
        String wavePath = line.substring(0, separatorIndex);
        String transcription = line.substring(separatorIndex + 1, line.length());
        UtteranceHolder item = new UtteranceHolder(wavePath, transcription);
        items.add(item);
      }
      int size = items.size();
      log.debug("Converting audio files...");
      int i = 0;
      for (UtteranceHolder item : items) {
        log.debug("Converting file " + item.getWavePath());
        File waveFile = new File(rootDir, item.getWavePath() + ".wav");
        AudioSource as = WaveConverter.syncConvert(waveFile, format);
        TranscribedUtterance tu = new TranscribedUtterance();
        tu.setRelativePath(item.getWavePath());
        tu.setTranscription(item.getTranscription().trim());
        tu.setUtterance(as.getAudioContent());
        words.add(tu);
      }
      log.debug("Import successfully finished");
    } catch (Exception e) {
      log.error("", e);
      throw new AsrRuntimeException(e);
    }

    return words;
  }

  public static Map<String, String> importDictionary(File file) {
    FileReader fileReader;
    BufferedReader reader;
    String line = null;
    Map<String, String> items;
    try {
      fileReader = new FileReader(file);
      reader = new BufferedReader(fileReader);
      items = new HashMap();
      while ((line = reader.readLine()) != null) {
        int separatorIndex = line.indexOf(separator);
        String grapheme = line.substring(0, separatorIndex).trim();
        String pronunciation = line.substring(separatorIndex + 1, line.length()).trim();
        log.debug("Adding word={}, pronunciation={}", grapheme, pronunciation);
        items.put(grapheme, pronunciation);
      }
    } catch (IOException e) {
      log.error("", e);
      throw new AsrRuntimeException(e);
    }

    return items;
  }

  public static List<String> importPhoneSet(File file) {
    FileReader fileReader;
    BufferedReader reader;
    String line = null;
    List<String> items;
    try {
      fileReader = new FileReader(file);
      reader = new BufferedReader(fileReader);
      items = new LinkedList();
      while ((line = reader.readLine()) != null) {
        String phone = line.trim();
        log.debug("Loading phone {}", phone);
        items.add(phone);
      }
    } catch (IOException e) {
      log.error("", e);
      throw new AsrRuntimeException(e);
    }
    return items;
  }

  private static class UtteranceHolder {
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
