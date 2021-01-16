package org.speech.asr.common.specification;

import org.apache.commons.beanutils.PropertyUtils;
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
public class BeanRestrictions {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(BeanRestrictions.class.getName());

  public static CompositeSpecification eq(String propertyName, Object value) {
    return new EqPropertySpecification(propertyName, value);
  }

  public static CompositeSpecification refOf(String propertyName, Object value) {
    return new RefOfPropertySpecification(propertyName, value);
  }

  private static abstract class AbstractPropertySpecification extends AbstractCompositeSpecification {
    protected String propertyName;
    protected Object value;

    public AbstractPropertySpecification(String propertyName, Object propertyValue) {
      this.propertyName = propertyName;
      this.value = propertyValue;
    }
  }

  private static class RefOfPropertySpecification extends AbstractPropertySpecification {
    public RefOfPropertySpecification(String propertyName, Object propertyValue) {
      super(propertyName, propertyValue);
    }

    public boolean isSatisfiedBy(Object obj) {
      try {
        return PropertyUtils.getProperty(obj, propertyName) == value;
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }
  }

  private static class EqPropertySpecification extends AbstractPropertySpecification {

    public EqPropertySpecification(String propertyName, Object propertyValue) {
      super(propertyName, propertyValue);
    }

    public boolean isSatisfiedBy(Object obj) {
      try {
        return PropertyUtils.getProperty(obj, propertyName).equals(value);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }
  }
}
