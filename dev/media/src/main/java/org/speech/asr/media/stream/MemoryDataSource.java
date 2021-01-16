/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.media.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.media.util.TimeUtils;
import org.speech.asr.media.vo.AudioSource;

import javax.media.Time;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullBufferDataSource;
import javax.media.protocol.PullBufferStream;
import java.io.IOException;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 8, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class MemoryDataSource extends PullBufferDataSource {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MemoryDataSource.class.getName());

  private AudioSource audioSource;

  public MemoryDataSource(AudioSource audioSource) {
    this.audioSource = audioSource;
  }

  public PullBufferStream[] getStreams() {
    MemoryPullSourceStream stream = new MemoryPullSourceStream(audioSource);
    return new PullBufferStream[]{stream};
  }

  public void connect() throws IOException {
  }

  public void disconnect() {
  }

  public String getContentType() {
    return ContentDescriptor.RAW;
  }

  public Object getControl(String s) {
    return null;
  }

  public Object[] getControls() {
    return new Object[0];
  }

  public Time getDuration() {
    return TimeUtils.getTime(audioSource.getAudioContent().length, audioSource.getAudioFormat());
  }

  public void start() throws IOException {
  }

  public void stop() throws IOException {
  }
}
