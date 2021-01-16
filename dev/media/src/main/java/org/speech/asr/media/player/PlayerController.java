package org.speech.asr.media.player;

import org.speech.asr.media.vo.AudioSource;
import org.speech.asr.media.vo.Time;


/**
 * //@todo interface description
 * <p/>
 * Creation date: May 18, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface PlayerController {

  void setAudioSource(AudioSource audioStream);

  AudioSource getAudioSource();

  void start();

  void stop();

  void pause();

  void seek(Time time);

  void addListener(PlayerListener l);

  void removeListener(PlayerListener l);

}
