/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.media.microphone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.media.vo.AudioSource;
import org.speech.asr.media.vo.AudioSourceImpl;

import javax.media.*;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import java.io.IOException;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 31, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class JmfMicrophone implements Microphone {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(JmfMicrophone.class.getName());

  private static final MediaLocator MICROPHONE_LOCATOR = new MediaLocator("javasound://44100");

  private AudioFormat audioFormat;

  private Processor processor;

  private ByteArrayTransferHandler transferHandler;

  private PushBufferDataSource pbds;

  private void init() {
    try {
      ProcessorModel model =
          new ProcessorModel(MICROPHONE_LOCATOR, new Format[]{audioFormat},
              new ContentDescriptor(ContentDescriptor.RAW));
      processor = Manager.createRealizedProcessor(model);
      pbds = (PushBufferDataSource) processor.getDataOutput();
      PushBufferStream[] pbs = pbds.getStreams();
      transferHandler = new ByteArrayTransferHandler();
      pbs[0].setTransferHandler(transferHandler);

    } catch (Exception e) {
      throw new AsrRuntimeException(e);
    }
  }

  public AudioSource getAudioSource() {
    AudioSourceImpl as = new AudioSourceImpl("record", audioFormat, transferHandler.getData());
    return as;
  }

  public void setAudioFormat(AudioFormat audioFormat) {
    this.audioFormat = audioFormat;
    init();
  }

  public void start() {
    log.debug("Starting recording...");
    try {
      pbds.start();
      processor.start();
    } catch (IOException e) {
      throw new AsrRuntimeException(e);
    }
  }

  public void stop() {
    log.debug("Stopping recording");
    processor.stop();
    processor.close();
    processor = null;
  }
}
