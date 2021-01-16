package org.speech.asr.common.event;

import org.speech.asr.common.specification.Specification;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Apr 23, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface EventDispatcher {

  void addEventListener(Specification selector, EventListener listener);

  void removeEventListener(EventListener listener);

  void dispatchEvent(Event event);

}
