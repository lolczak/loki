package org.speech.asr.recognition.acoustic;

import java.util.Collection;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jun 19, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface Hmm {

  HmmState getInitialState();

  HmmState getFinalState();

  Collection<HmmState> getAllStates();

}
