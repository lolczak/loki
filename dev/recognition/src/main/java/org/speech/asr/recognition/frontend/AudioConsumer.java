package org.speech.asr.recognition.frontend;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Aug 5, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface AudioConsumer {

  /**
   * Puts samples to component.
   *
   * @param samples - audio samples
   */
  void put(AudioPayload samples);

  /**
   * Puts samples to queue and adds mark after it.
   * This mark is used by search manager to finish recognition.
   *
   * @param samples
   * @param addMarkAfter
   */
  void put(AudioPayload samples, boolean addMarkAfter);

  /**
   * Add mark (DataEndSignal+DataStartSignal) to queue.
   */
  void addSpeechSeparator();

  /**
   * Notify about end of stream.
   */
  void stop();

}
