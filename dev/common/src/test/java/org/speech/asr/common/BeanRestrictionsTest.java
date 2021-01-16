package org.speech.asr.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.mock.BeanWithEnum;
import org.speech.asr.common.mock.InnerBean;
import org.speech.asr.common.mock.OuterBean;
import org.speech.asr.common.specification.BeanRestrictions;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 23, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class BeanRestrictionsTest {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(BeanRestrictionsTest.class.getName());

  @Test
  public void testPrimitiveType() {
    InnerBean inner = new InnerBean();
    inner.setIntValue(13);

    OuterBean bean = new OuterBean();
    bean.setLongValue(12l);
    bean.setInnerBean(inner);

    boolean result =
        BeanRestrictions.eq("innerBean.intValue", 13).and(BeanRestrictions.eq("longValue", 12l)).isSatisfiedBy(bean);

    Assert.assertTrue(result);

    result =
        BeanRestrictions.eq("innerBean.intValue", 133).and(BeanRestrictions.eq("longValue", 12l)).isSatisfiedBy(bean);

    Assert.assertFalse(result);

  }

  @Test
  public void testEnumProperty() {
    BeanWithEnum mock = new BeanWithEnum();
    mock.setAnswer(BeanWithEnum.LogicEnum.YES);

    boolean result = BeanRestrictions.refOf("answer", BeanWithEnum.LogicEnum.YES).isSatisfiedBy(mock);
    Assert.assertTrue(result);

    result = BeanRestrictions.refOf("answer", BeanWithEnum.LogicEnum.NO).isSatisfiedBy(mock);
    Assert.assertFalse(result);
  }

}
