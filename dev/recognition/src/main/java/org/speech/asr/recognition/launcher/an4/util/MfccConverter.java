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
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.frontend.AudioPayload;
import org.speech.asr.recognition.frontend.FeatureExtractorPipe;
import org.speech.asr.recognition.frontend.SphinxFeatureExtractor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 7, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class MfccConverter {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MfccConverter.class.getName());

  public static List<Feature> computeMfccLE(byte[] audio, final int samplingRate) {
    final int[] audioArray = bytesToInts(audio);
    final Object synchronizer = new Object();
    final FeatureExtractorPipe pipe = SphinxFeatureExtractor.createMffcExtractor(samplingRate);
    final List<Feature> features = new LinkedList();
    final int tenMillis = samplingRate / 100;
    synchronized (synchronizer) {
      new Thread(new Runnable() {
        public void run() {
          log.debug("Starting producing...");
          for (int i = 0; i < audioArray.length; i = i + tenMillis) {

            int length = i + tenMillis < audioArray.length ? tenMillis : audioArray.length - i;
            int[] chunk = new int[length];
            System.arraycopy(audioArray, i, chunk, 0, length);
            AudioPayload payload = new AudioPayload();
            payload.setOffset(i);
            payload.setSamplingRate(samplingRate);
            payload.setTimestamp(i / (samplingRate / 1000));
            payload.setSamples(chunk);
            pipe.write(payload);
          }
//        try {
//          Thread.sleep(5000);
//        } catch (InterruptedException e) {
//          log.error("", e);
//        }
          log.debug("Stopping producing");
          pipe.stop();
        }
      }).start();

      new Thread(new Runnable() {
        public void run() {
          log.debug("Starting consuming..");
          while (true) {
            Feature feature = pipe.read();
            if (feature != null) {
              //taka na szybko normalizacja
              for (int i = 0; i < feature.getData().length; i++) {
                feature.getData()[i] = (feature.getData()[i] + 20) / 40;
              }
              features.add(feature);
            } else {
              log.debug("Stopping consuming");
              break;
            }
          }
          synchronized (synchronizer) {
            synchronizer.notify();
          }
        }
      }).start();


      try {
        synchronizer.wait();
      } catch (InterruptedException e) {
        log.error("", e);
      }
    }
    return features;
  }

  private int[] convertLE(byte[] array) {
    int[] result = new int[array.length / 2];

    int offset = 0;
    for (int i = 0; i < array.length; i = i + 2) {
      result[offset++] = array[i] | (array[i + 1] << 8);
    }

    return result;
  }

  private static int[] bytesToInts(byte[] data) {
    ByteBuffer buffer = ByteBuffer.allocate(data.length);
    buffer.put(data);
    buffer.order(ByteOrder.LITTLE_ENDIAN);
    buffer.rewind();
    short[] tab = new short[data.length / 2];
    buffer.asShortBuffer().get(tab);
    int[] ret = new int[tab.length];
    for (int i = 0; i < tab.length; i++) {
      ret[i] = tab[i];
    }

    return ret;
  }
}
