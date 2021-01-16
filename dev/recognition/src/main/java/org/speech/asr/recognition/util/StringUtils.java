/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 7, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class StringUtils {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(StringUtils.class.getName());

  public static List<String> parse(String string) {
    StringTokenizer st = new StringTokenizer(string, " \t");
    List<String> tokens = new LinkedList();
    while (st.hasMoreTokens()) {
      tokens.add(st.nextToken());
    }
    return tokens;
  }
}
