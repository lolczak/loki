/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.cvt;

import org.speech.asr.recognition.acoustic.StateDescriptor;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 22, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class StateSegment<T extends StateDescriptor> {

  private T state;

  private int firstFrame;

  private int lastFrame;

  public int getFirstFrame() {
    return firstFrame;
  }

  public void setFirstFrame(int firstFrame) {
    this.firstFrame = firstFrame;
  }

  public int getLastFrame() {
    return lastFrame;
  }

  public void setLastFrame(int lastFrame) {
    this.lastFrame = lastFrame;
  }

  public T getState() {
    return state;
  }

  public void setState(T state) {
    this.state = state;
  }
}
