package org.speech.asr.gui.jcr;

import org.apache.jackrabbit.api.JackrabbitNodeTypeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springmodules.jcr.jackrabbit.JackrabbitSessionFactory;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 20, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class JackrabbitSessionFactoryExt extends JackrabbitSessionFactory {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(JackrabbitSessionFactoryExt.class.getName());

  /**
   * Node definitions in CND format.
   */
  private Resource[] nodeDefinitions;

  private String contentType = JackrabbitNodeTypeManager.TEXT_XML;

  /*
	 * (non-Javadoc)
	 * @see org.springmodules.jcr.JcrSessionFactory#registerNodeTypes()
	 */
  protected void registerNodeTypes() throws Exception {

//    if (!ObjectUtils.isEmpty(nodeDefinitions)) {
//      Workspace ws = getSession().getWorkspace();
//
//      // Get the NodeTypeManager from the Workspace.
//      // Note that it must be cast from the generic JCR NodeTypeManager to
//      // the
//      // Jackrabbit-specific implementation.
//      NodeTypeManagerImpl nodeTypeManager = (NodeTypeManagerImpl) ws.getNodeTypeManager();
//
//      boolean debug = log.isDebugEnabled();
//      for (int i = 0; i < nodeDefinitions.length; i++) {
//        Resource resource = nodeDefinitions[i];
//        if (debug)
//          log.debug("adding node type definitions from " + resource.getDescription());
//
//        nodeTypeManager.registerNodeTypes(resource.getInputStream(), contentType, true);
//      }
//    }
    try {
      super.registerNodeTypes();
    } catch (Exception e) {
      log.error("Error occured registering node types: " + e.getMessage());
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setContentType(String contentType) {
    super.setContentType(contentType);
    this.contentType = contentType;
  }

  /**
   * {@inheritDoc}
   */
  public void setNodeDefinitions(Resource[] nodeDefinitions) {
    super.setNodeDefinitions(nodeDefinitions);
    this.nodeDefinitions = nodeDefinitions;
  }
}
