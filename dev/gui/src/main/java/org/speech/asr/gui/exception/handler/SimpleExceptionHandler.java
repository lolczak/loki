package org.speech.asr.gui.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.exceptionhandling.RegisterableExceptionHandler;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 16, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0
 */
public class SimpleExceptionHandler implements RegisterableExceptionHandler, Thread.UncaughtExceptionHandler {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SimpleExceptionHandler.class.getName());

  public void registerExceptionHandler() {
    log.debug("Registering exception hadler");
    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  public void uncaughtException(Thread thread, Throwable throwable) {
    log.error("Uncaught exception occurred", throwable);

    new SimpleExceptionDialog(thread, throwable).showDialog();
  }
}
