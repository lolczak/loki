package org.speech.asr.gui.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.context.AppContext;
import org.speech.asr.gui.util.StatusBarLogger;
import org.speech.asr.gui.util.dictionary.DictionariesSource;
import org.speech.asr.gui.util.image.ScalingSupportingImageSource;
import org.speech.asr.gui.widget.progress.ProgressDialog;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.config.DefaultApplicationLifecycleAdvisor;
import org.springframework.richclient.application.statusbar.StatusBar;
import org.springframework.richclient.application.statusbar.support.DefaultStatusBar;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 16, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0
 */
public class SimpleLifecycleAdvisor extends DefaultApplicationLifecycleAdvisor {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SimpleLifecycleAdvisor.class.getName());

  private DefaultStatusBar statusBar;

  private DictionariesSource dictionarySource;

  private ScalingSupportingImageSource imageSource;

  private MessageSource messageSource;

  public StatusBar getStatusBar() {
    if (statusBar == null) {
      statusBar = new DefaultStatusBar();
      StatusBarLogger.init(statusBar);
    }
    return statusBar;
  }

  public void onPreStartup() {
    super.onPreStartup();
    AppContext appContext = AppContext.getInstance();
    appContext.setDictionarySource(dictionarySource);
    appContext.setImageSource(imageSource);
    appContext.setMessageSource(messageSource);
    appContext.setApplication(getApplication());

//    appContext.setProgressWidget(new ProgressDialog("progress.title"));
    //getApplicationServices();
  }

  public void onShutdown() {
    log.info("Shutting down");
    // getApplication().getActiveWindow().getPage().close()
    super.onShutdown();
  }

  /**
   * Setter for property 'dictionarySource'.
   *
   * @param dictionarySource Value to set for property 'dictionarySource'.
   */
  public void setDictionarySource(DictionariesSource dictionarySource) {
    this.dictionarySource = dictionarySource;
  }

  /**
   * Setter for property 'imageSource'.
   *
   * @param imageSource Value to set for property 'imageSource'.
   */
  public void setImageSource(ScalingSupportingImageSource imageSource) {
    this.imageSource = imageSource;
  }

  /**
   * Setter for property 'messageSource'.
   *
   * @param messageSource Value to set for property 'messageSource'.
   */
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }
}
