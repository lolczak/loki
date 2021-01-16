package org.speech.asr.common.mock;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 24, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class BeanWithEnum {

  public enum LogicEnum {
    YES, NO
  }
  ;

  private LogicEnum answer;

  public LogicEnum getAnswer() {
    return answer;
  }

  public void setAnswer(LogicEnum answer) {
    this.answer = answer;
  }
}
