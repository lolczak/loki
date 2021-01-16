/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.an4;

import edu.cmu.sphinx.frontend.util.DataUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.launcher.an4.util.MfccConverter;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 8, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class MfccReaderTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MfccReaderTest.class.getName());

//  @Test
//  public void test8khzWave() {
//    AudioFormat format =
//        new AudioFormat(AudioFormat.LINEAR, 8000, 16, 1, AudioFormat.LITTLE_ENDIAN, AudioFormat.SIGNED);
//    AudioSource as = WaveConverter.syncConvert(new File("/mnt/work/test/test.wav"), format);
//    double[] values = DataUtil.littleEndianBytesToValues(as.getAudioContent(), 0, as.getAudioContent().length, 2, true);
//    List<Feature> features = MfccConverter.computeMfccLE(as.getAudioContent(), 8000);
//    log.debug("Features");
//  }

  @Test
  public void test16khz() {
    try {
      byte[] samples = FileUtils.readFileToByteArray(new File("/mnt/work/test/test16.raw"));

      double[] values = DataUtil.littleEndianBytesToValues(samples, 0, samples.length, 2, true);
      List<Feature> features = MfccConverter.computeMfccLE(samples, 16000);
      log.debug("Features");
    } catch (IOException e) {
      log.error("", e);
    }
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
