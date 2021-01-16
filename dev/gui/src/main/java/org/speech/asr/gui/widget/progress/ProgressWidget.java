package org.speech.asr.gui.widget.progress;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jun 14, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface ProgressWidget {

  void start();

  void start(int allWork);

  void setStatus(String txt);

  void log(String msg);

  void progress(int p);

  void stop();
}
