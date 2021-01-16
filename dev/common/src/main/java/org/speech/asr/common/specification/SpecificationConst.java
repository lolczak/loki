package org.speech.asr.common.specification;

/**
 * Created by IntelliJ IDEA.
 * User: luol
 * Date: Apr 23, 2009
 * Time: 8:18:07 PM
 * To change this template use File | Settings | File Templates.
 */
public enum SpecificationConst implements Specification {
  TRUE {
    public boolean isSatisfiedBy(Object obj) {
      return true;
    }},
  FALSE {
    public boolean isSatisfiedBy(Object obj) {
      return false;
    }};


  public abstract boolean isSatisfiedBy(Object obj);

}
