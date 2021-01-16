package org.speech.asr.gui.view.editor;

import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingDesktop;
import com.vlsolutions.swing.docking.DockingUtilities;
import com.vlsolutions.swing.docking.TabbedDockableContainer;
import com.vlsolutions.swing.docking.event.DockableStateChangeEvent;
import com.vlsolutions.swing.docking.event.DockableStateChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.event.Event;
import org.speech.asr.common.event.EventDispatcher;
import org.speech.asr.common.event.EventListener;
import org.speech.asr.common.specification.HierarchyRestrictions;
import org.speech.asr.common.specification.Specification;
import org.speech.asr.gui.constant.EditorsContainerConstants;
import org.speech.asr.gui.event.EditorsContainerEvent;
import org.speech.asr.gui.util.image.ScalingSupportingImageSource;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.support.AbstractView;

import javax.swing.*;
import java.awt.*;


/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 22, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class EditorsContainerView extends AbstractView {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(EditorsContainerView.class.getName());

  private DockingDesktop desktop;

  private EventDispatcher eventDispatcher;

  private MessageSource messageSource;

  private ScalingSupportingImageSource imageSource;

  private DockableRepository dockableRepository;

  public EditorsContainerView() {
    desktop = new DockingDesktop();
    dockableRepository = new DockableRepository();
  }

  private void registerEventListeners() {
    Specification specification = HierarchyRestrictions.instanceOf(EditorsContainerEvent.class);
    eventDispatcher.addEventListener(specification, new EventListener() {
      public void eventDispatched(Event event) {
        handleEvent(event);
      }
    });
  }

  protected JComponent createControl() {
    registerEventListeners();
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    desktop.addDockableStateChangeListener(new DockableStateChangeListener() {
      public void dockableStateChanged(DockableStateChangeEvent event) {
        EditorsContainerView.this.dockableStateChanged(event);
      }
    });

    panel.add(desktop, BorderLayout.CENTER);
    return panel;
  }

  private void handleEvent(Event e) {
    //todo obsluga pozostalych eventow
    EditorsContainerEvent event = (EditorsContainerEvent) e;
    createOrSelect(event);
  }

  private void createOrSelect(EditorsContainerEvent event) {
    if (dockableRepository.contains(event.getDocumentId())) {
      //todo select
      if (dockableRepository.getDockableCount() > 1) {
        ItemEditorAdapter dock = dockableRepository.getById(event.getDocumentId());
        TabbedDockableContainer tabbedDockableContainer = DockingUtilities.findTabbedDockableContainer(dock);
        if (tabbedDockableContainer != null) {
          tabbedDockableContainer.setSelectedDockable(dock);
        }
      }
    } else {
      ItemEditor editor = getNewEditor(event.getEditorName());
      editor.setContext(event.getPayload());
      ItemEditorAdapter dock = new ItemEditorAdapter(event.getDocumentId(), editor);
      dock.setImageSource(imageSource);
      dock.setMessageSource(messageSource);
      if (dockableRepository.isDesktopEmpty()) {
        desktop.addDockable(dock);
      } else {
        desktop.createTab(dockableRepository.getLast(), dock, 0, true);
      }
      dockableRepository.put(dock);
    }
  }

  private void dockableStateChanged(DockableStateChangeEvent event) {
    if (event.getNewState().getState() == DockableState.STATE_CLOSED) {
      ItemEditorAdapter adapter = (ItemEditorAdapter) event.getNewState().getDockable();
      //todo notyfikacja
      dockableRepository.remove(adapter);
    }
  }

//  private void setTabbedContainer() {
//    if (dockableRepository.getDockableCount() == 1) {
//      DockableContainer baseOldContainer = DockingUtilities.findSingleDockableContainer(dockableRepository.getLast());
//      if (baseOldContainer != null) {
//        TabbedDockableContainer baseTab = DockableContainerFactory.getFactory().createTabbedDockableContainer();
//        baseTab.installDocking(desktop);
//        baseTab.addDockable(dockableRepository.getLast(), 0);
//
//        DockingUtilities.replaceChild(((Component) baseOldContainer).getParent(),
//            (Component) baseOldContainer, (Component) baseTab);
//      }
//    }
//  }

  private ItemEditor getNewEditor(String editorName) {
    String beanName = editorName + EditorsContainerConstants.EDITORS_BEAN_SUFFIX;
    ItemEditor editorBean = (ItemEditor) getApplicationContext().getBean(beanName, ItemEditor.class);
    return editorBean;
  }

  /**
   * Setter dla pola 'eventDispatcher'.
   *
   * @param eventDispatcher wartosc ustawiana dla pola 'eventDispatcher'.
   */
  public void setEventDispatcher(EventDispatcher eventDispatcher) {
    this.eventDispatcher = eventDispatcher;
  }

  /**
   * Setter dla pola 'imageSource'.
   *
   * @param imageSource wartosc ustawiana dla pola 'imageSource'.
   */
  public void setImageSource(ScalingSupportingImageSource imageSource) {
    this.imageSource = imageSource;
  }

  /**
   * Setter dla pola 'messageSource'.
   *
   * @param messageSource wartosc ustawiana dla pola 'messageSource'.
   */
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }
}
