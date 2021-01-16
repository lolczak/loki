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
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.launcher.an4.util.An4Importer;
import org.speech.asr.recognition.launcher.an4.util.MfccConverter;
import org.speech.asr.recognition.launcher.an4.util.TranscribedUtterance;
import org.speech.asr.recognition.util.SerializerUtils;

import javax.media.format.AudioFormat;
import java.io.File;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class WaveConverter {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(WaveConverter.class.getName());

  private int samplingRate;

  private File transcriptionFile;

  private File corpusDir;

  private File featureDir;

  public void start() {
    AudioFormat format =
        new AudioFormat(AudioFormat.LINEAR, samplingRate, 16, 1, AudioFormat.LITTLE_ENDIAN, AudioFormat.SIGNED);

    List<TranscribedUtterance> trainSet =
        An4Importer.importCorpus(transcriptionFile, corpusDir, format);
    for (TranscribedUtterance tu : trainSet) {
      log.debug("Converting {}", tu.getTranscription());
      List<Feature> features = MfccConverter.computeMfccLE(tu.getUtterance(), samplingRate);
      File featFile = new File(featureDir, tu.getRelativePath() + ".feat");
      featFile.getParentFile().mkdirs();
      SerializerUtils.save(features, featFile);
    }
  }

  public void setCorpusDir(File corpusDir) {
    this.corpusDir = corpusDir;
  }

  public void setFeatureDir(File featureDir) {
    this.featureDir = featureDir;
  }

  public void setSamplingRate(int samplingRate) {
    this.samplingRate = samplingRate;
  }

  public void setTranscriptionFile(File transcriptionFile) {
    this.transcriptionFile = transcriptionFile;
  }
}
