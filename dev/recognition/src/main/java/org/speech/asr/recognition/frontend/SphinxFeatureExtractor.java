/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.frontend;

import edu.cmu.sphinx.frontend.*;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.acoustic.FeatureImpl;

import java.net.URL;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 6, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SphinxFeatureExtractor implements FeatureExtractorPipe {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SphinxFeatureExtractor.class.getName());

  private static final String SPHINX_CONFIG = "feature-config.xml";

  private static final String CLEAN_MFCC_8KHZ = "clean8khzMFCC";

  private static final String CLEAN_MFCC_16KHZ = "clean16khzMFCC";

  private static final String AUDIO_CONSUMER = "audioConsumer";

  private AudioConsumer audioConsumer;

  private FrontEnd frontEnd;

  private ConfigurationManager manager;

  private boolean isStopped;

  private long sequenceNumber;

  public static FeatureExtractorPipe createMffcExtractor(int samplingRate) {
    if (samplingRate == 8000) {
      return new SphinxFeatureExtractor(CLEAN_MFCC_8KHZ);
    } else if (samplingRate == 16000) {
      return new SphinxFeatureExtractor(CLEAN_MFCC_16KHZ);
    } else {
      throw new IllegalArgumentException("Supported rates 8000hz and 16000hz");
    }
  }


  public static FeatureExtractorPipe create8khzMffcExtractor() {
    return new SphinxFeatureExtractor(CLEAN_MFCC_8KHZ);
  }


  public static FeatureExtractorPipe create16khzMffcExtractor() {
    return new SphinxFeatureExtractor(CLEAN_MFCC_16KHZ);
  }

  private SphinxFeatureExtractor(String frontEndName) {
    URL url;
    log.debug("Sphinx config resource {}", SPHINX_CONFIG);
    url = SphinxFeatureExtractor.class.getClassLoader().getResource(SPHINX_CONFIG);
    log.debug("Loading configuration from {}", url);
    manager = new ConfigurationManager(url);
    frontEnd = (FrontEnd) manager.lookup(frontEndName);
    frontEnd.initialize();
    audioConsumer = (AudioConsumer) frontEnd.getElements().get(0);   //manager.lookup(AUDIO_CONSUMER);  
    isStopped = false;
    sequenceNumber = 0;
  }

  public void stop() {
    audioConsumer.addSpeechSeparator();
  }

  public Feature read() {
    if (isStopped) {
      return null;
    }
    //todo refactor
    Data data;
    do {
      data = frontEnd.getData();
    } while (data instanceof DataStartSignal);
    if (data instanceof DataEndSignal) {
      isStopped = true;
      return null;
    }

    FloatData featureData = (FloatData) data;
    FeatureImpl feature = new FeatureImpl(floatToDouble(featureData.getValues()));
    feature.setSequenceNumber(sequenceNumber++);
    return feature;
  }

  public void write(AudioPayload payload) {
    audioConsumer.put(payload);
  }

  private double[] floatToDouble(float[] values) {
    double[] result = new double[values.length];
    for (int i = 0; i < values.length; i++) {
      result[i] = values[i];
    }
    return result;
  }
}
