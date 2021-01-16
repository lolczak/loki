package org.speech.asr.gui.widget.player;

import org.speech.asr.media.vo.Time;

import javax.media.format.AudioFormat;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jun 11, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface PlayerChangeListener {

  void updateDuration(final Time duration);

  void updateTime(Time time, Time duration);

  void updateFormatLabels(AudioFormat format);

  void updateStateLabel(String stateText);
}
