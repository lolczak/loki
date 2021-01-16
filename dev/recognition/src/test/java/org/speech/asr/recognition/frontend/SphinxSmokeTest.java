/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.Feature;
import org.testng.annotations.Test;

import java.util.Random;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 5, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SphinxSmokeTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SphinxSmokeTest.class.getName());

  private long offset = 0;

  private int samplingRate = 8000;

  private long timestamp = 0;

  private Object synchronizer;

  @Test
  public void fireUp() {
    synchronizer = new Object();
    final FeatureExtractorPipe pipe = SphinxFeatureExtractor.create8khzMffcExtractor();

    new Thread(new Runnable() {
      public void run() {
        log.debug("Starting producing...");
        for (int i = 0; i < 1000; i++) {
          AudioPayload payload = createNextPayload();
          pipe.write(payload);
        }
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          log.error("", e);
        }
        log.debug("Fireing finished");
        pipe.stop();

      }
    }).start();

    new Thread(new Runnable() {
      public void run() {
        log.debug("Starting consuming..");
        int received = 0;
        while (received < 1993) {
          Feature feature = pipe.read();
          log.debug("Received feature {}", feature);
          if (feature != null) {
            received++;
          } else {
            break;
          }
        }
        synchronized (synchronizer) {
          synchronizer.notify();
        }
      }
    }).start();

    synchronized (synchronizer) {
      try {
        synchronizer.wait();
      } catch (InterruptedException e) {
        log.error("", e);
      }
    }

  }

  private AudioPayload createNextPayload() {
    AudioPayload payload = new AudioPayload();
    payload.setOffset(offset);
    payload.setSamplingRate(samplingRate);
    payload.setTimestamp(timestamp);

    int[] samples = new int[samplingRate / 100];
    Random random = new Random();
    for (int i = 0; i < samplingRate / 100; i++) {
      samples[i] = (int) (300.0 * Math.sin((offset + i) * 2 * Math.PI / 20));//random.nextInt(300);
    }
    payload.setSamples(samples);

    offset += samplingRate / 100;
    timestamp += 10;

    return payload;
  }
}
