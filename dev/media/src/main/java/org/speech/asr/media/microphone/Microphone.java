package org.speech.asr.media.microphone;

import org.speech.asr.media.vo.AudioSource;

import javax.media.format.AudioFormat;

/**
 * //@todo interface description
 * <p/>
 * Creation date: May 31, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface Microphone {

  AudioSource getAudioSource();

  void setAudioFormat(AudioFormat audioFormat);

  void start();

  void stop();
}
