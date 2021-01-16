/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.gui.jcr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.BaseSpringTest;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;
import org.testng.annotations.Test;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.io.IOException;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jun 11, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SmokeJcrTest extends BaseSpringTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SmokeJcrTest.class.getName());

  private JcrTemplate jcrTemplate;

  @Test
  public void testCount() {
    Integer count = (Integer) jcrTemplate.execute(new JcrCallback() {
      public Object doInJcr(Session session) throws IOException, RepositoryException {
        QueryManager qm = session.getWorkspace().getQueryManager();
        //Query q = qm.createQuery("/asr:root/asr:corpora/*", Query.XPATH);
        Query q = qm.createQuery("//asr:root/*", Query.XPATH);
        log.debug("Executing query {}", q.getStatement());
        QueryResult qr = q.execute();
        NodeIterator ri = qr.getNodes();
        log.debug("Executed query {} results size {} hasNext {}",
            new Object[]{q.getStatement(), ri.getSize(), ri.hasNext()});
        return null;  //TODO To change body of implemented methods use File | Settings | File Templates.
      }
    });

    log.debug("Count: {}", count);
  }

  public void setJcrTemplate(JcrTemplate jcrTemplate) {
    this.jcrTemplate = jcrTemplate;
  }
}
