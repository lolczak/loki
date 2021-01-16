package org.speech.asr.recognition.linguist;

import org.speech.asr.recognition.decoder.SearchGraphSegment;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jul 27, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface SearchGraphBuilder {

  SearchGraphSegment createSearchGraph();
}
