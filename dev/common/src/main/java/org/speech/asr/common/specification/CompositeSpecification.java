package org.speech.asr.common.specification;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Apr 23, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface CompositeSpecification extends Specification {
  
  CompositeSpecification and(Specification rightOperand);

  CompositeSpecification or(Specification rightOperand);

  CompositeSpecification xor(Specification rightOperand);

  CompositeSpecification not();

}
