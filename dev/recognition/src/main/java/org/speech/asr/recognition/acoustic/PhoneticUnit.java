package org.speech.asr.recognition.acoustic;

import java.util.List;

/**
 * bedzie on dekomponowal kazdy fonem na na stany
 * stan to gmm plus prawd. pozostania w stanie
 * <p/>
 * Creation date: Jun 19, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface PhoneticUnit<S extends StateDescriptor> {

  String getName();

  List<S> getStatesSequence();
}
