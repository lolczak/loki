package org.speech.asr.gui.logic;

import org.speech.asr.common.entity.DictionaryEntity;
import org.speech.asr.common.entity.Word;

import java.util.List;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Apr 22, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface DictionaryBean {

  List<DictionaryEntity> getAllDictionaries();

  DictionaryEntity getByUuid(String uuid);

  String create(DictionaryEntity dictionary);

  void delete(String uuid);

  DictionaryEntity update(DictionaryEntity dictionary);

  void saveDictionaryContent(String uuid, List<Word> words);

  List<Word> getDictionaryContent(String uuid);

}