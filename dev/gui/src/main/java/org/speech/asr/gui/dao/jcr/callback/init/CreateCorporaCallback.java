package org.speech.asr.gui.dao.jcr.callback.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.speech.asr.gui.constant.JcrConstants.*;
import org.springmodules.jcr.JcrCallback;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 20, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class CreateCorporaCallback implements JcrCallback {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(CreateCorporaCallback.class.getName());

  public Object doInJcr(Session session) throws IOException, RepositoryException {
    Node rootNode = session.getRootNode();
    Node asrRootNode = rootNode.getNode(ASR_ROOT_NODE_NAME);
    Node corporaNode = asrRootNode.addNode(CORPORA_ROOT_NODE_NAME);
    session.save();

    return corporaNode;
  }
}
