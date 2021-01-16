package org.speech.asr.gui.dao.jcr.callback.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.entity.DictionaryEntity;
import org.speech.asr.gui.dao.jcr.mapping.MapperUtils;
import org.springmodules.jcr.JcrCallback;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 24, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class UpdateDictionaryCallback implements JcrCallback {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(UpdateDictionaryCallback.class.getName());

  private DictionaryEntity dictionary;

  public UpdateDictionaryCallback(DictionaryEntity dictionary) {
    this.dictionary = dictionary;
  }

  public Object doInJcr(Session session) throws IOException, RepositoryException {
    Node node = session.getNodeByUUID(dictionary.getUuid());
    if (node == null) {
      throw new RepositoryException("There is no node with uuid " + dictionary.getUuid());
    }
    MapperUtils.mapEntity(dictionary, node);
    session.save();
    log.debug("Dictionary node {} updated.", node.getPath());
    return dictionary;
  }
}
