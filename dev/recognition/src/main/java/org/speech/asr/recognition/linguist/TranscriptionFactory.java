package org.speech.asr.recognition.linguist;

import org.speech.asr.recognition.acoustic.PhoneticUnit;

import java.util.List;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Aug 30, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface TranscriptionFactory {
  List<PhoneticUnit> createTranscription(String txt);

  void setStartSilenceSymbol(String symbol);

  void setStopSilenceSymbol(String symbol);
}
