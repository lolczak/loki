package org.speech.asr.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.specification.Specification;

import java.util.LinkedList;
import java.util.List;

/**
 * Prosty synchroniczny dispatcher zdarzen.
 * <p/>
 * Creation date: Apr 23, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class SimpleSynchronousEventDispatcher implements EventDispatcher {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SimpleSynchronousEventDispatcher.class.getName());

  private List<ListenerHolder> listeners;

  public SimpleSynchronousEventDispatcher() {
    listeners = new LinkedList();
  }

  public synchronized void addEventListener(Specification selector, EventListener listener) {
    listeners.add(new ListenerHolder(selector, listener));
  }

  public synchronized void dispatchEvent(Event event) {
    for (ListenerHolder lh : listeners) {
      Specification spec = lh.getSelector();
      EventListener listener = lh.getListener();
      if (spec.isSatisfiedBy(event)) {
        listener.eventDispatched(event);
      }
    }
  }

  public synchronized void removeEventListener(EventListener listener) {
    List<ListenerHolder> toRemove = new LinkedList();
    for (ListenerHolder lh : listeners) {
      if (lh.getListener() == listener) {
        toRemove.add(lh);
      }
    }

    listeners.removeAll(toRemove);
  }

  private class ListenerHolder {
    private Specification selector;

    private EventListener listener;

    public ListenerHolder(Specification spec, EventListener listener) {
      this.selector = spec;
      this.listener = listener;
    }

    public Specification getSelector() {
      return selector;
    }

    public void setSelector(Specification selector) {
      this.selector = selector;
    }

    public EventListener getListener() {
      return listener;
    }

    public void setListener(EventListener listener) {
      this.listener = listener;
    }
  }
}
