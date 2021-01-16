package org.speech.asr.gui.view.editor.dictionary.importer;

import org.speech.asr.common.entity.Word;

import java.util.List;
import java.io.InputStream;

/**
 * //@todo interface description
 * <p/>
 * Creation date: May 10, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface DictionaryImporter {

  List<Word> importDictionary(InputStream in);

}
