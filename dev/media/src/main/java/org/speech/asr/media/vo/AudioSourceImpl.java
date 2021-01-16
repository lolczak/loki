/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.media.vo;

import org.apache.commons.io.FileUtils;
import org.speech.asr.common.exception.AsrRuntimeException;

import javax.media.format.AudioFormat;
import java.io.File;
import java.io.IOException;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 31, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class AudioSourceImpl implements AudioSource {

  private byte[] audioContent;

  private AudioFormat audioFormat;

  private String name;

  public AudioSourceImpl(String name, AudioFormat audioFormat, byte[] audioContent) {
    this.name = name;
    this.audioFormat = audioFormat;
    this.audioContent = audioContent;
  }

  public AudioSourceImpl(String path, AudioFormat audioFormat) {
    try {
      audioContent = FileUtils.readFileToByteArray(new File(path));
      this.name = path;
      this.audioFormat = audioFormat;
    } catch (IOException e) {
      throw new AsrRuntimeException(e);
    }
  }

  public byte[] getAudioContent() {
    return audioContent;
  }

  public AudioFormat getAudioFormat() {
    return audioFormat;
  }

  public String getName() {
    return name;
  }

  public boolean isEmpty() {
    if (audioContent == null || audioContent.length == 0) {
      return true;
    }
    return false;
  }

  public String toString() {
    return "AudioSourceImpl{" +
//        "audioContentLength=" + audioContent != null ? String.valueOf(audioContent.length) : "null" +
        ", audioFormat=" + audioFormat +
        ", name='" + name + '\'' +
        '}';
  }
}
