package org.speech.asr.gui.vo;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 20, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class RepoInitStatus {
  private boolean dictionariesInit;

  private boolean corporaInit;

  private boolean acousticModelsInit;

  private boolean recognizersInit;

  private boolean isAsrRoot;

  public boolean isRepoInit() {
    return dictionariesInit && corporaInit && acousticModelsInit && recognizersInit && isAsrRoot;
  }

  /**
   * Getter for property 'acousticModelsInit'.
   *
   * @return Value for property 'acousticModelsInit'.
   */
  public boolean isAcousticModelsInit() {
    return acousticModelsInit;
  }

  /**
   * Setter for property 'acousticModelsInit'.
   *
   * @param acousticModelsInit Value to set for property 'acousticModelsInit'.
   */
  public void setAcousticModelsInit(boolean acousticModelsInit) {
    this.acousticModelsInit = acousticModelsInit;
  }

  /**
   * Getter for property 'corporaInit'.
   *
   * @return Value for property 'corporaInit'.
   */
  public boolean isCorporaInit() {
    return corporaInit;
  }

  /**
   * Setter for property 'corporaInit'.
   *
   * @param corporaInit Value to set for property 'corporaInit'.
   */
  public void setCorporaInit(boolean corporaInit) {
    this.corporaInit = corporaInit;
  }

  /**
   * Getter for property 'dictionariesInit'.
   *
   * @return Value for property 'dictionariesInit'.
   */
  public boolean isDictionariesInit() {
    return dictionariesInit;
  }

  /**
   * Setter for property 'dictionariesInit'.
   *
   * @param dictionariesInit Value to set for property 'dictionariesInit'.
   */
  public void setDictionariesInit(boolean dictionariesInit) {
    this.dictionariesInit = dictionariesInit;
  }

  /**
   * Getter for property 'recognizersInit'.
   *
   * @return Value for property 'recognizersInit'.
   */
  public boolean isRecognizersInit() {
    return recognizersInit;
  }

  /**
   * Setter for property 'recognizersInit'.
   *
   * @param recognizersInit Value to set for property 'recognizersInit'.
   */
  public void setRecognizersInit(boolean recognizersInit) {
    this.recognizersInit = recognizersInit;
  }

  /**
   * Getter for property 'asrRoot'.
   *
   * @return Value for property 'asrRoot'.
   */
  public boolean isAsrRoot() {
    return isAsrRoot;
  }

  /**
   * Setter for property 'asrRoot'.
   *
   * @param asrRoot Value to set for property 'asrRoot'.
   */
  public void setAsrRoot(boolean asrRoot) {
    isAsrRoot = asrRoot;
  }

  public String toString() {
    return "RepoInitStatus{" +
        "acousticModelsInit=" + acousticModelsInit +
        ", dictionariesInit=" + dictionariesInit +
        ", corporaInit=" + corporaInit +
        ", recognizersInit=" + recognizersInit +
        ", isAsrRoot=" + isAsrRoot +
        '}';
  }
}
