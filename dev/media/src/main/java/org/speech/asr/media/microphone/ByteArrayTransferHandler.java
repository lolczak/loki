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

import javax.media.Buffer;
import javax.media.protocol.BufferTransferHandler;
import javax.media.protocol.PushBufferStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 3, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ByteArrayTransferHandler implements BufferTransferHandler {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ByteArrayTransferHandler.class.getName());

  private List<byte[]> audioData;

  private int length;

  public ByteArrayTransferHandler() {
    audioData = new LinkedList();
    length = 0;
  }

  public void transferData(PushBufferStream stream) {
    Buffer buffer = new Buffer();
    try {
      stream.read(buffer);
      if (buffer.getLength() > 0) {
        length += buffer.getLength();
        byte[] buf = new byte[buffer.getLength()];
        System.arraycopy((byte[]) buffer.getData(), 0, buf, 0, buffer.getLength());
        audioData.add(buf);
      }
    } catch (IOException e) {
      log.error("Error while transferring data", e);
    }
  }

  public byte[] getData() {
    byte[] content = new byte[length];
    int offset = 0;
    for (byte[] chunk : audioData) {
      System.arraycopy(chunk, 0, content, offset, chunk.length);
      offset += chunk.length;
    }
    return content;
  }
}