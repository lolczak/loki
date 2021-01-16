package org.speech.asr.gui.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.application.ApplicationLauncher;

import javax.swing.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 16, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0
 */
public class AppLauncher {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(AppLauncher.class.getName());

  private static final String CONTEXT_FILE = "context/richclient-application-context.xml";

  private static final String STARTUP_CONTEXT_FILE = "context/richclient-startup-context.xml";

  public static void main(String[] args) {
//    String contextPath = AppLauncher.class.getClassLoader().getResource(CONTEXT_FILE).toString();
//    String startupPath = AppLauncher.class.getClassLoader().getResource(STARTUP_CONTEXT_FILE).toString();
//    JFrame.setDefaultLookAndFeelDecorated(true);
//    try {
//      UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceOfficeBlue2007LookAndFeel");
//    } catch (Exception e) {
//      log.error("Substance Raven Graphite failed to initialize");
//    }


    try {
      new ApplicationLauncher(STARTUP_CONTEXT_FILE,
          new String[]{CONTEXT_FILE, "context/jcr-context.xml", "context/application-context.xml"});
    } catch (Exception ex) {
      log.error("Exception occurred", ex);
      System.exit(1);
    }
  }
}
