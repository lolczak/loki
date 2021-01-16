package org.speech.asr.common.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 23, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class OuterBean {
   private Long longValue;

    private InnerBean innerBean;

    public Long getLongValue() {
      return longValue;
    }

    public void setLongValue(Long longValue) {
      this.longValue = longValue;
    }

    public InnerBean getInnerBean() {
      return innerBean;
    }

    public void setInnerBean(InnerBean innerBean) {
      this.innerBean = innerBean;
    }
}