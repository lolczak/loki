package org.speech.asr.recognition.trainer.baumwelch;

import org.speech.asr.recognition.acoustic.GmmAcousticModel;
import org.speech.asr.recognition.trainer.TrainSentence;

import java.util.Collection;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jul 14, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface ModelInitializer {

  void initializeStates(Collection<TrainSentence> trainingSet, GmmAcousticModel model);
}
