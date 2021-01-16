package org.speech.asr.gui.util.dictionary;

import org.speech.asr.gui.util.dictionary.DictionaryItem;

import java.util.List;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jun 8, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface DictionariesSource {

  List<DictionaryItem> getItems(String key);
}
