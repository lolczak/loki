package org.speech.asr.gui.dao.jcr.mapping;

import static org.speech.asr.gui.constant.JcrDictionaryProperties.*;
import org.speech.asr.common.entity.DictionaryEntity;
import org.speech.asr.common.entity.Word;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 24, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class DictionaryMapper implements JcrMapper<DictionaryEntity> {

  public DictionaryEntity mapNode(Node node) throws RepositoryException {
    //todo fixme dodac walidacje
    DictionaryEntity dictionary = new DictionaryEntity();

    dictionary.setUuid(node.getUUID());
    dictionary.setName(node.getProperty(NAME_PROPERTY).getString());
    dictionary.setDescription(node.getProperty(DESCRIPTION_PROPERTY).getString());
    dictionary.setLanguage(node.getProperty(LANGUAGE_PROPERTY).getString());
    dictionary.setPhoneticAlphabet(node.getProperty(PHONETIC_ALPHABET_PROPERTY).getString());
    List<Word> words = new LinkedList();

    dictionary.setWords(words);

    return dictionary;
  }

  public void mapEntity(DictionaryEntity fromEntity, Node toNode) throws RepositoryException {
    toNode.setProperty(NAME_PROPERTY, fromEntity.getName());
    toNode.setProperty(DESCRIPTION_PROPERTY, fromEntity.getDescription());
    toNode.setProperty(LANGUAGE_PROPERTY, fromEntity.getLanguage());
    toNode.setProperty(PHONETIC_ALPHABET_PROPERTY, fromEntity.getPhoneticAlphabet());
  }
}
