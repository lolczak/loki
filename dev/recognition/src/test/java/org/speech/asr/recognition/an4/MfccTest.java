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
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 8, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class MfccTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MfccTest.class.getName());

  @Test
  public void read() {
    List<Feature> features = null;
    try {
      byte[] samples = FileUtils.readFileToByteArray(new File("/mnt/work/test/cen1-fash-b.raw"));

      double[] values = DataUtil.littleEndianBytesToValues(samples, 0, samples.length, 2, true);
      features = MfccConverter.computeMfccLE(samples, 16000);

    } catch (IOException e) {
      log.error("", e);
    }
    log.debug("Features size {}", features.size());
    try {
      byte[] content = FileUtils.readFileToByteArray(new File("/mnt/work/test/cen1-fash-b.mfc"));
      int size = byteToFloat(content, 0);//5187;
      int offset = 4;
      double min = 0;
      double max =0;
      for (int i = 0; i < size; i++) {
        int intVal = byteToFloat(content, offset);
        float val = Float.intBitsToFloat(intVal);
        log.debug("Frame {} Cepstrum of {} val= {}, and my is {}",
            new Object[]{i / 13, i % 13, val, features.get(i / 13).getData()[i % 13]});
//        Assert.assertTrue(MathUtils.compare(val, features.get(i / 13).getData()[i % 13], 0.20));
        if (val < min) {
          min = val;
        }
        if (val>max) {
          max = val;
        }
        offset += 4;
      }
      log.debug("Min {} ; Max {}", min, max);
    } catch (IOException e) {
      log.error("", e);
    }
  }

  private int byteToInt(byte[] bytes, int offset) {
    int result = 0;
    result = result | (bytes[offset] << 24);
    result = result | (bytes[offset + 1] << 16);
    result = result | (bytes[offset + 2] << 8);
    result = result | (bytes[offset + 3]);
    return result;
  }

  private int byteToFloat(byte[] bytes, int offset) {
    int result = 0;
    int b1 = byteToInt(bytes[offset]);
    int b2 = byteToInt(bytes[offset + 1]);
    int b3 = byteToInt(bytes[offset + 2]);
    int b4 = byteToInt(bytes[offset + 3]);
    assert b1 >= 0 : "Value " + b1;
    assert b2 >= 0 : "Value " + b2;
    assert b3 >= 0 : "Value " + b3;
    assert b4 >= 0 : "Value " + b4;
    result = result | (b1 << 24);
    result = result | (b2 << 16);
    result = result | (b3 << 8);
    result = result | (b4);
    return result;
  }

  private int byteToInt(byte bVal) {
    int iVal = bVal;
//    int s = iVal < 0 ? -1 : 1;
//    int result = (iVal * s) | (s < 0 ? 0x80 : 0);
    int result = iVal & 0xFF;
//    String bits = Integer.toBinaryString(iVal);
//    bits = bits.substring(bits.length() - 8, bits.length());
//    String restStr = Integer.toBinaryString(iVal);
//    restStr = restStr.substring(restStr.length() - 8, restStr.length());
//    assert bits.equals(restStr);
    return result;
  }
}
