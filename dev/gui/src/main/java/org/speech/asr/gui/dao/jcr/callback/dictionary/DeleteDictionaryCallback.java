package org.speech.asr.gui.dao.jcr.callback.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.dao.jcr.callback.BaseUuidJcrCallback;

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
public class DeleteDictionaryCallback extends BaseUuidJcrCallback {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DeleteDictionaryCallback.class.getName());

  public DeleteDictionaryCallback(String uuid) {
    super(uuid);
  }

  public Object doInJcr(Session session) throws IOException, RepositoryException {
    Node node = session.getNodeByUUID(getUuid());
    node.remove();
    session.save();
    return null;
  }
}
