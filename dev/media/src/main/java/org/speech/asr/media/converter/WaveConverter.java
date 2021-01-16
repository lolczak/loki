/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.media.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.media.microphone.ByteArrayTransferHandler;
import org.speech.asr.media.vo.AudioSource;
import org.speech.asr.media.vo.AudioSourceImpl;

import javax.media.*;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import java.io.File;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 12, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class WaveConverter {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(WaveConverter.class.getName());

  public static AudioSource syncConvert(File waveFile, AudioFormat format) {
    try {
      MediaLocator fileLocator = new MediaLocator(waveFile.toURI().toURL());
      ProcessorModel model =
          new ProcessorModel(fileLocator, new Format[]{format},
              new ContentDescriptor(ContentDescriptor.RAW));
      Processor processor = Manager.createRealizedProcessor(model);
      PushBufferDataSource pbds = (PushBufferDataSource) processor.getDataOutput();
      PushBufferStream[] pbs = pbds.getStreams();
      ByteArrayTransferHandler transferHandler = new ByteArrayTransferHandler();
      pbs[0].setTransferHandler(transferHandler);
      InnerListener asyncCompletition = new InnerListener();
      processor.addControllerListener(asyncCompletition);

      synchronized (asyncCompletition) {
        pbds.start();
        processor.start(); //async start
        log.trace("Waiting for conversion end...");
        asyncCompletition.wait();
        log.trace("Conversion finished");
      }
      processor.close();
      return new AudioSourceImpl("", format, transferHandler.getData());
    } catch (Exception e) {
      throw new AsrRuntimeException(e);
    }

  }

  private static class InnerListener implements ControllerListener {

    public synchronized void controllerUpdate(ControllerEvent event) {
      log.trace("Event received by converter {}", event);
      if (event instanceof EndOfMediaEvent) {
        notifyAll();
      } else if (event instanceof StopByRequestEvent) {
        notifyAll();
      } else if (event instanceof StopAtTimeEvent) {
        notifyAll();
      } else if (event instanceof ControllerClosedEvent) {
        notifyAll();
      }
    }
  }
}
