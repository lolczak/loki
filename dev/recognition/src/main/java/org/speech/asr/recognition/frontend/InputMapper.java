package org.speech.asr.recognition.frontend;

import org.speech.asr.recognition.ann.InputBlock;

import java.util.List;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Aug 27, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface InputMapper {

  List<InputBlock> mapInput(double[] input);

}
