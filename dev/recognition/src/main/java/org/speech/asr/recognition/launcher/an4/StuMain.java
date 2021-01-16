/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.launcher.an4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.GmmAcousticModel;
import org.speech.asr.recognition.acoustic.PhoneticUnit;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.SimpleLogScale;
import org.speech.asr.recognition.util.GmmAmSerializer;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * //@todo class description
 * <p/>
 * Creation date: Sep 1, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class StuMain {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(StuMain.class.getName());

  public static void main(String[] args) {
    AsrContext ctx = new AsrContext();
    ctx.setTransitionScored(false);
    LogScale logScale = new SimpleLogScale(1.0001);
    ctx.setLogScale(logScale);
    AsrContext.setContext(ctx);
    try {
      GmmAcousticModel model = GmmAmSerializer.load(new FileInputStream("/mnt/work/an4/8khz-stu.xml"));
      FileWriter writer = new FileWriter("/mnt/work/an4/stu.phone");
      for (PhoneticUnit unit : model.getPhoneSet()) {
        writer.write(unit.getName() + "\n");
      }
      writer.close();
    } catch (IOException e) {
      log.error("", e);
      System.exit(1);
    }
  }
}
