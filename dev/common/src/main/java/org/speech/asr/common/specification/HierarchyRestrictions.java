package org.speech.asr.common.specification;

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
public class HierarchyRestrictions {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(HierarchyRestrictions.class.getName());

  public static CompositeSpecification instanceOf(Class type) {
    return new InstanceOfSpecification(type);
  }

  public static CompositeSpecification subclassOf() {
    return null;
  }

  public static CompositeSpecification baseclassOf() {
    return null;
  }

  private static class InstanceOfSpecification extends AbstractCompositeSpecification {
    private Class type;

    public InstanceOfSpecification(Class type) {
      this.type = type;
    }

    public boolean isSatisfiedBy(Object obj) {
      try {
        return type.isInstance(obj);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }
  }
}
