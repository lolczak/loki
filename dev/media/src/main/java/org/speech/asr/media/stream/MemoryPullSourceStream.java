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

import javax.media.Buffer;
import javax.media.Format;
import javax.media.Time;
import javax.media.protocol.ContentDescriptor;
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
public class MemoryPullSourceStream implements PullBufferStream {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MemoryPullSourceStream.class.getName());

  private AudioSource audioSource;

  public MemoryPullSourceStream(AudioSource audioSource) {
    this.audioSource = audioSource;
  }

  public Format getFormat() {
    return audioSource.getAudioFormat();
  }

  public void read(Buffer buffer) throws IOException {
    byte[] content = audioSource.getAudioContent();
    long duration = TimeUtils.getTimeInNano(content.length, audioSource.getAudioFormat());
    buffer.setData(content);
    buffer.setFormat(audioSource.getAudioFormat());
    buffer.setOffset(0);
    buffer.setLength(content.length);
    buffer.setDuration(duration);
    buffer.setTimeStamp(0);
    buffer.setEOM(true);
  }

  public boolean willReadBlock() {
    return true;
  }

  public boolean endOfStream() {
    log.debug("testing if end of media...");
    return true;
  }

  public ContentDescriptor getContentDescriptor() {
    return new ContentDescriptor(audioSource.getAudioFormat().getEncoding());
  }

  public long getContentLength() {
    return audioSource.getAudioContent().length;
  }

  public Object getControl(String controlType) {
    return null;
  }

  public Object[] getControls() {
    return new Object[0];
  }
}
