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
public abstract class AbstractCompositeSpecification implements CompositeSpecification {
  /**
   * slf4j Logger.
   */
  private static final Logger log =
      LoggerFactory.getLogger(org.speech.asr.common.specification.AbstractCompositeSpecification.class.getName());

  protected Specification leftOperand;
  protected Specification rightOperand;

  public AbstractCompositeSpecification() {
    leftOperand = null;
    rightOperand = null;
  }

  public AbstractCompositeSpecification(Specification leftOperand, Specification rightOperand) {
    this.leftOperand = leftOperand;
    this.rightOperand = rightOperand;
  }

  public CompositeSpecification and(Specification rightOperand) {
    return new AndSpecification(this, rightOperand);
  }

  public CompositeSpecification not() {
    return new NotSpecification(this);
  }

  public CompositeSpecification or(Specification rightOperand) {
    return new OrSpecification(this, rightOperand);
  }

  public CompositeSpecification xor(Specification rightOperand) {
    return new XorSpecification(this, rightOperand);
  }


  private static class AndSpecification extends AbstractCompositeSpecification {
    public AndSpecification(Specification leftOperand, Specification rightOperand) {
      super(leftOperand, rightOperand);
    }

    public boolean isSatisfiedBy(Object obj) {
      return leftOperand.isSatisfiedBy(obj) && rightOperand.isSatisfiedBy(obj);
    }
  }

  private static class OrSpecification extends AbstractCompositeSpecification {
    public OrSpecification(Specification leftOperand, Specification rightOperand) {
      super(leftOperand, rightOperand);
    }

    public boolean isSatisfiedBy(Object obj) {
      return leftOperand.isSatisfiedBy(obj) || rightOperand.isSatisfiedBy(obj);
    }
  }

  private static class XorSpecification extends AbstractCompositeSpecification {
    public XorSpecification(Specification leftOperand, Specification rightOperand) {
      super(leftOperand, rightOperand);
    }

    public boolean isSatisfiedBy(Object obj) {
      boolean l = leftOperand.isSatisfiedBy(obj);
      boolean r = rightOperand.isSatisfiedBy(obj);

      return (!l && r) || (l && !r);
    }
  }

  private static class NotSpecification extends AbstractCompositeSpecification implements Specification {
    protected Specification innerSpec;

    public NotSpecification(Specification operand) {
      innerSpec = operand;
    }

    public boolean isSatisfiedBy(Object obj) {
      return !innerSpec.isSatisfiedBy(obj);
    }
  }

}
