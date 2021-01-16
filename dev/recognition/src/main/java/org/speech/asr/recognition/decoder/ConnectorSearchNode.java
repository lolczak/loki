/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.Feature;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 27, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ConnectorSearchNode extends AbstractSearchNode {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ConnectorSearchNode.class.getName());

  protected String nodeId;

  public ConnectorSearchNode(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getNodeId() {
    return nodeId;
  }

  public String getWord() {
    return null;
  }

  public boolean isEmitting() {
    return false;
  }

  public boolean isHypothesis() {
    return false;
  }

  public double score(Feature feature) {
    throw new IllegalStateException();
  }


  public String toString() {
    return "ConnectorSearchNode{" +
        "nodeId='" + nodeId + '\'' +
        '}';
  }
}
