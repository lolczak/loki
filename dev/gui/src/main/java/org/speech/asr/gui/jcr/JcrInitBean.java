package org.speech.asr.gui.jcr;

import org.speech.asr.gui.vo.RepoInitStatus;

/**
 * Bean odpowiedzialny za inicjalizacje repozytorium jcr.
 * <p/>
 * Creation date: Apr 20, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface JcrInitBean {

  RepoInitStatus getRepoStatus();

  void initRepo();

}
