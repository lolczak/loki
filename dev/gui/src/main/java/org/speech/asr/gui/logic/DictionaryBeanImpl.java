package org.speech.asr.gui.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.dao.jcr.callback.dictionary.*;
import org.speech.asr.common.entity.DictionaryEntity;
import org.speech.asr.common.entity.Word;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 25, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class DictionaryBeanImpl implements DictionaryBean {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DictionaryBeanImpl.class.getName());

  private JcrTemplate jcrTemplate;

  public List<DictionaryEntity> getAllDictionaries() {
    log.debug("Getting all dictionaries");
    JcrCallback listClbck = new ListDictionaryCallback();

    return (List) jcrTemplate.execute(listClbck);
  }

  public DictionaryEntity getByUuid(String uuid) {
    log.debug("Getting dictionary with uuid {}", uuid);
    JcrCallback getClbck = new GetDictionaryCallback(uuid);

    return (DictionaryEntity) jcrTemplate.execute(getClbck);
  }

  public String create(DictionaryEntity dictionary) {
    log.debug("Creating dictionary {}", dictionary);
    JcrCallback createCallback = new CreateDictionaryCallback(dictionary);
    String uuid = (String) jcrTemplate.execute(createCallback);
    dictionary.setUuid(uuid);
    jcrTemplate.save();
    return uuid;
  }

  public void delete(String uuid) {
    log.debug("Deleting dictionary with uuid {}", uuid);
    JcrCallback deleteClbck = new DeleteDictionaryCallback(uuid);

    jcrTemplate.execute(deleteClbck);
  }

  public DictionaryEntity update(DictionaryEntity dictionary) {
    log.debug("Updating dictionary {}", dictionary);
    JcrCallback updateClbck = new UpdateDictionaryCallback(dictionary);
    return (DictionaryEntity) jcrTemplate.execute(updateClbck);
  }

  public List<Word> getDictionaryContent(String uuid) {
    log.debug("Getting dictionary content for uuid {}", uuid);
    JcrCallback getContentClbck = new GetDictionaryContentCallback(uuid);
    return (List) jcrTemplate.execute(getContentClbck);
  }

  public void saveDictionaryContent(String uuid, List<Word> words) {
    log.debug("Saving dictionary content for uuid {}", uuid);
    JcrCallback saveClbck = new SaveDictionaryContentCallback(uuid, words);
    jcrTemplate.execute(saveClbck);
  }

  /**
   * Setter dla pola 'jcrTemplate'.
   *
   * @param jcrTemplate wartosc ustawiana dla pola 'jcrTemplate'.
   */
  public void setJcrTemplate(JcrTemplate jcrTemplate) {
    this.jcrTemplate = jcrTemplate;
  }
}
