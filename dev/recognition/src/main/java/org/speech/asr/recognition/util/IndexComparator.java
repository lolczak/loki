/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.util;

import org.dom4j.Element;
import static org.speech.asr.recognition.constant.GmmAcousticModelConstants.INDEX_ATTR;

import java.util.Comparator;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 28, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class IndexComparator implements Comparator<Element> {
  public int compare(Element o1, Element o2) {
    int index1 = Integer.valueOf(o1.attribute(INDEX_ATTR).getText());
    int index2 = Integer.valueOf(o2.attribute(INDEX_ATTR).getText());
    if (index1 == index2) {
      return 0;
    }
    if (index1 < index2) {
      return -1;
    } else {
      return 1;
    }
  }
}