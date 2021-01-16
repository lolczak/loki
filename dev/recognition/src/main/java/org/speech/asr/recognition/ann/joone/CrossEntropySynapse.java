/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.ann.joone;

import org.joone.engine.learning.TeacherSynapse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.math.MathUtils;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 27, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class CrossEntropySynapse extends TeacherSynapse {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(CrossEntropySynapse.class.getName());

  public CrossEntropySynapse() {
    super();
  }

  protected double calculateError(double aDesired, double anOutput, int anIndex) {
    double myError = aDesired - anOutput;

    assert MathUtils.isReal(myError) : "aDesired=" + aDesired + ", out=" + anOutput;
    // myError = Dn - Yn
    // myError^2 = (Dn - yn)^2
    // GlobalError += SUM[ SUM[ 1/2 (Dn - yn)^2]]
    // GlobalError += SUM[ 1/2 SUM[(Dn - yn)^2]]
    GlobalError += (myError * myError) / 2;
    assert MathUtils.isReal(GlobalError) : "globalErr=" + GlobalError + "aDesired=" + aDesired + ", out=" + anOutput;
    return myError;
  }

}
