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
import org.speech.asr.recognition.ann.InputBlock;

import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 27, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class BaseInputMapper implements InputMapper {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(BaseInputMapper.class.getName());

  protected List<Mapping> mappings;

  public BaseInputMapper() {
    mappings = new LinkedList();
  }

  public void addMapping(int fromIndex, int toIndex, String name) {
    Mapping mapping = new Mapping();
    mapping.setFromIndex(fromIndex);
    mapping.setToIndex(toIndex);
    mapping.setName(name);
    mappings.add(mapping);
  }

  public List<InputBlock> mapInput(double[] input) {
    List<InputBlock> inputs = new LinkedList();
    for (Mapping mapping : mappings) {
      InputBlock inputBlock = new InputBlock();
      int length = mapping.toIndex - mapping.fromIndex + 1;
      double[] arr = new double[length];
      System.arraycopy(input, mapping.getFromIndex(), arr, 0, length);
      inputBlock.setId(mapping.getName());
      inputBlock.setInput(arr);
      inputs.add(inputBlock);
    }
    return inputs;
  }

  protected class Mapping {
    private int fromIndex;

    private int toIndex;

    private String name;

    public int getFromIndex() {
      return fromIndex;
    }

    public void setFromIndex(int fromIndex) {
      this.fromIndex = fromIndex;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getToIndex() {
      return toIndex;
    }

    public void setToIndex(int toIndex) {
      this.toIndex = toIndex;
    }
  }
}
