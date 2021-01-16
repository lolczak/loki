package org.speech.asr.gui.dao.jcr.mapping;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Apr 24, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface JcrMapper<T> {

  T mapNode(Node node) throws RepositoryException;

  void mapEntity(T fromEntity, Node toNode) throws RepositoryException;
}
