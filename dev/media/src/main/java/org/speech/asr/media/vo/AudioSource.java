package org.speech.asr.media.vo;

import javax.media.format.AudioFormat;

/**
 * //@todo interface description
 * <p/>
 * Creation date: May 19, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface AudioSource {

  String getName();

  AudioFormat getAudioFormat();

  byte[] getAudioContent();

  boolean isEmpty();

}
