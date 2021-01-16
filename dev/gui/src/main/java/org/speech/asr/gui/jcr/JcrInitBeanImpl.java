package org.speech.asr.gui.jcr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.dao.jcr.callback.init.*;
import org.speech.asr.gui.vo.RepoInitStatus;
import org.springmodules.jcr.JcrTemplate;

import javax.annotation.PostConstruct;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 20, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0
 */
public class JcrInitBeanImpl implements JcrInitBean {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(JcrInitBeanImpl.class.getName());

  private JcrTemplate jcrTemplate;

  public RepoInitStatus getRepoStatus() {
    return null;  //TODO To change body of implemented methods use File | Settings | File Templates.
  }

  @PostConstruct
  public void initRepo() {
    log.debug("Initializing jcr repo");
    InitRepoCallback initClbck = new InitRepoCallback();

    RepoInitStatus repoStatus = (RepoInitStatus) jcrTemplate.execute(initClbck);
    log.debug("Repo status : {}", repoStatus);

    if (!repoStatus.isRepoInit()) {
      log.debug("Initializing jcr repo");
      if (!repoStatus.isAsrRoot()) {
        initAsrRoot();
      } else {
        if (!repoStatus.isAcousticModelsInit()) {
          initAcousticModelsNode();
        }
        if (!repoStatus.isCorporaInit()) {
          initCorporaNode();
        }
        if (!repoStatus.isDictionariesInit()) {
          initDictionariesNode();
        }
        if (!repoStatus.isRecognizersInit()) {
          initDictionariesNode();
        }
      }
    }
  }

  private void initAsrRoot() {
    log.debug("Initializing whole branch");
    CreateAsrRootCallback asrRootClbck = new CreateAsrRootCallback();

    jcrTemplate.execute(asrRootClbck);
    log.debug("Asr root created");

    initAcousticModelsNode();
    initDictionariesNode();
    initRecognizersNode();
    initCorporaNode();
  }

  private void initAcousticModelsNode() {
    CreateAcousticModelsCallback modelsClbck = new CreateAcousticModelsCallback();
    jcrTemplate.execute(modelsClbck);
    log.debug("Acoustic models node created");
  }

  private void initDictionariesNode() {
    CreateDictionariesCallback dictsClbck = new CreateDictionariesCallback();
    jcrTemplate.execute(dictsClbck);
    log.debug("Dictionaries node created");
  }

  private void initCorporaNode() {
    CreateCorporaCallback corporaClbck = new CreateCorporaCallback();
    jcrTemplate.execute(corporaClbck);
    log.debug("Corpora node created");
  }

  private void initRecognizersNode() {
    CreateRecognizersCallback recognizersClbck = new CreateRecognizersCallback();
    jcrTemplate.execute(recognizersClbck);
    log.debug("Recognizers node created");
  }

  /**
   * Setter dla pola 'jcrTemplate'.
   *
   * @param jcrTemplate wartosc ustawiana dla pola 'jcrTemplate'.
   */
  public void setJcrTemplate(JcrTemplate jcrTemplate) {
    this.jcrTemplate = jcrTemplate;
  }
}
