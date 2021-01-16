/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.dao.jcr.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 17, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class JcrTemplateExt extends JcrTemplate {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(JcrTemplateExt.class.getName());

  public static final ThreadLocal<JcrTemplateExt> JCR_TEMPLATE = new ThreadLocal();

  public Object execute(JcrCallback action, boolean exposeNativeSession) throws DataAccessException {
    JCR_TEMPLATE.set(this);
    return super.execute(action, exposeNativeSession);
  }

  public Object execute(JcrCallback callback) throws DataAccessException {
    JCR_TEMPLATE.set(this);
    return super.execute(callback);
  }
}
