/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.media;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.media.converter.WaveConverter;
import org.speech.asr.media.player.AudioPlayer;
import org.speech.asr.media.vo.AudioSource;
import org.testng.annotations.Test;

import javax.media.format.AudioFormat;
import java.io.File;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 31, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SmokeTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SmokeTest.class.getName());

  @Test
  public void test() {
    try {
      AudioSource as =
          WaveConverter.syncConvert(new File("/home/luol/0-9.wav"), new AudioFormat(AudioFormat.LINEAR, 8000, 8, 1));
      AudioPlayer player = new AudioPlayer();
      player.setAudioSource(as);
      player.start();
      Thread.sleep(5000);
    } catch (Exception e) {
      log.error("", e);
    }
  }
}
