package org.speech.asr.gui.dao.jcr.callback.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.speech.asr.gui.constant.JcrConstants.ASR_ROOT_NODE_NAME;
import static org.speech.asr.gui.constant.JcrConstants.DICTIONARIES_ROOT_NODE_NAME;
import static org.speech.asr.gui.constant.JcrDictionaryProperties.DICTIONARY_NODE_NAME;
import org.speech.asr.gui.dao.jcr.mapping.MapperUtils;
import org.speech.asr.common.entity.DictionaryEntity;
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
public class CreateDictionaryCallback implements JcrCallback {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(CreateDictionaryCallback.class.getName());

  private DictionaryEntity dictionary;

  public CreateDictionaryCallback(DictionaryEntity dictionary) {
    this.dictionary = dictionary;
  }

  public Object doInJcr(Session session) throws IOException, RepositoryException {
    Node rootNode = session.getRootNode();
    Node asrRootNode = rootNode.getNode(ASR_ROOT_NODE_NAME);
    Node dictsNode = asrRootNode.getNode(DICTIONARIES_ROOT_NODE_NAME);
    Node dictionaryNode = dictsNode.addNode(DICTIONARY_NODE_NAME, DICTIONARY_NODE_NAME);

    MapperUtils.mapEntity(dictionary, dictionaryNode);
    //todo
    Object result = dictionaryNode.getUUID();
    session.save();
    log.debug("Created node at path {}  ", dictionaryNode.getPath());

    return result;
  }
}
