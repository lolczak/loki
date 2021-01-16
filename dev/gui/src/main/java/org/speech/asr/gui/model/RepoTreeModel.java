package org.speech.asr.gui.model;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.event.Event;
import org.speech.asr.common.event.EventDispatcher;
import org.speech.asr.common.event.EventListener;
import org.speech.asr.common.specification.HierarchyRestrictions;
import org.speech.asr.common.specification.Specification;
import org.speech.asr.common.entity.CorpusEntity;
import org.speech.asr.gui.constant.EditorsContainerConstants;
import org.speech.asr.common.entity.DictionaryEntity;
import org.speech.asr.gui.event.CorpusEvent;
import org.speech.asr.gui.event.DictionaryEvent;
import org.speech.asr.gui.event.EditorsContainerEvent;
import org.speech.asr.gui.logic.AcousticModelBean;
import org.speech.asr.gui.logic.CorpusBean;
import org.speech.asr.gui.logic.DictionaryBean;
import org.speech.asr.gui.logic.RecognizerBean;
import org.speech.asr.gui.util.command.ContextProvider;
import org.speech.asr.gui.util.DynamicMutableTreeNode;
import org.speech.asr.gui.vo.GenericTreeNode;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.CommandManager;
import org.springframework.richclient.util.PopupMenuMouseListener;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 22, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class RepoTreeModel extends PopupMenuMouseListener implements TreeModel, TreeSelectionListener, ContextProvider {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(RepoTreeModel.class.getName());

  private enum NodeType {
    ROOT, DICTIONARIES, DICTIONARY, ACOUSTIC_MODELS, ACOUSTIC_MODEL, CORPORA, CORPUS, RECOGNIZERS, RECOGNIZER, PROPERTY
  }

  protected DefaultMutableTreeNode rootNode;

  protected DictionariesTreeNode dictionariesNode;

  protected DefaultMutableTreeNode amNode;

  protected CorporaTreeNode corporaNode;

  protected DefaultMutableTreeNode recognizersNode;

  protected Map<Object, MutableTreeNode> nodeMap;

  private AcousticModelBean acousticModelBean;

  private CorpusBean corpusBean;

  private DictionaryBean dictionaryBean;

  private RecognizerBean recognizerBean;

  private EventDispatcher eventDispatcher;

  private CommandManager commandManager;

  private Map<String, List<String>> commandGroupMap;

  private DefaultMutableTreeNode selectedNode;

  private List<TreeModelListener> listeners;

  public RepoTreeModel() {
    //createInitStructure();
    listeners = new LinkedList();
  }

  @PostConstruct
  public void init() {
    createInitStructure();
    registerEventListeners();
  }

  private void registerEventListeners() {
    Specification dictionariesSelector = HierarchyRestrictions.instanceOf(DictionaryEvent.class);
    eventDispatcher.addEventListener(dictionariesSelector, new EventListener() {
      public void eventDispatched(Event event) {
        refreshDictionaries();
      }
    });

    Specification corporaSelector = HierarchyRestrictions.instanceOf(CorpusEvent.class);
    eventDispatcher.addEventListener(corporaSelector, new EventListener() {
      public void eventDispatched(Event event) {
        refreshCorpora();
      }
    });
  }


  private void createInitStructure() {
    selectedNode = null;
    nodeMap = new HashedMap();
    rootNode =
        new DefaultMutableTreeNode(
            new GenericTreeNode(NodeType.ROOT, "asr.repo.node.label", "Asr repository", "rootNode.icon"));

    dictionariesNode =
        new DictionariesTreeNode(
            new GenericTreeNode(NodeType.DICTIONARIES, "dictionaries.repo.node.label", "Dictionaries",
                "dictionariesNode.icon"));
    amNode = new DefaultMutableTreeNode(
        new GenericTreeNode(NodeType.ACOUSTIC_MODELS, "am.repo.node.label", "Acoustic models", "amNode.icon"));
    corporaNode = new CorporaTreeNode(
        new GenericTreeNode(NodeType.CORPORA, "corpora.repo.node.label", "Corpora",
            "corporaNode.icon"));
    recognizersNode = new DefaultMutableTreeNode(
        new GenericTreeNode(NodeType.RECOGNIZERS, "recognizers.repo.node.label", "Recognizers",
            "recognizersNode.icon"));

    rootNode.add(dictionariesNode);
    rootNode.add(corporaNode);
    rootNode.add(amNode);
    rootNode.add(recognizersNode);
  }

  public void addTreeModelListener(TreeModelListener l) {
    listeners.add(l);
  }

  public Object getChild(Object parent, int index) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent;
    return node.getChildAt(index);
  }

  public int getChildCount(Object parent) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent;

    return node.getChildCount();
  }

  public int getIndexOfChild(Object parent, Object child) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent;

    return node.getIndex((DefaultMutableTreeNode) child);
  }

  public Object getRoot() {
    return rootNode;
  }

  public void removeTreeModelListener(TreeModelListener l) {
    removeTreeModelListener(l);
  }

  private void refreshDictionaries() {
    dictionariesNode.invalidate();
    for (TreeModelListener l : listeners) {
      TreeModelEvent e = new TreeModelEvent(this, new Object[]{rootNode, dictionariesNode});
      l.treeStructureChanged(e);
    }
  }

  private void refreshCorpora() {
    corporaNode.invalidate();
    for (TreeModelListener l : listeners) {
      TreeModelEvent e = new TreeModelEvent(this, new Object[]{rootNode, corporaNode});
      l.treeStructureChanged(e);
    }
  }

  public boolean isLeaf(Object node) {
    DefaultMutableTreeNode nnode = (DefaultMutableTreeNode) node;
    return nnode.isLeaf();
  }

  public void valueForPathChanged(TreePath path, Object newValue) {
//TODO To change body of implemented methods use File | Settings | File Templates.
  }

  public void valueChanged(TreeSelectionEvent e) {
    if (e != null && e.getNewLeadSelectionPath() != null)
      selectedNode = (DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent();
  }

  public void mouseClicked(MouseEvent e) {
    //super.mouseClicked(e);    
    if (e.getClickCount() == 2) {
      if (selectedNode == null) {
        return;
      }
      GenericTreeNode node = (GenericTreeNode) selectedNode.getUserObject();
      if (node.getNodeType() == NodeType.DICTIONARY) {
        //todo
        //fixme
        EditorsContainerEvent event = new EditorsContainerEvent();
        event.setEditorName(EditorsContainerConstants.DICTIONARY_EDITOR_NAME);
        event.setPayload(node.getUserObject());
        event.setDocumentId(((DictionaryEntity) node.getUserObject()).getUuid());
        eventDispatcher.dispatchEvent(event);
      } else if (node.getNodeType() == NodeType.CORPUS) {
        //todo
        //fixme
        EditorsContainerEvent event = new EditorsContainerEvent();
        event.setEditorName(EditorsContainerConstants.CORPUS_EDITOR_NAME);
        event.setPayload(node.getUserObject());
        event.setDocumentId(((CorpusEntity) node.getUserObject()).getUuid());
        eventDispatcher.dispatchEvent(event);
      }
    }
  }

  protected JPopupMenu getPopupMenu() {
    //todo
    if (selectedNode == null) {
      return null;
    }
    GenericTreeNode node = (GenericTreeNode) selectedNode.getUserObject();
    List<String> cmds = commandGroupMap.get(node.getNodeType().toString());
    if (cmds != null) {
      CommandGroup group = commandManager.createCommandGroup("repoGroup", cmds.toArray());
      return group.createPopupMenu();
    } else {
      return null;
    }

  }

  public Object getContext() {
    if (selectedNode == null) {
      return null;
    }
    GenericTreeNode node = (GenericTreeNode) selectedNode.getUserObject();
    return node.getUserObject();
  }

  /**
   * Setter dla pola 'acousticModelBean'.
   *
   * @param acousticModelBean wartosc ustawiana dla pola 'acousticModelBean'.
   */
  public void setAcousticModelBean(AcousticModelBean acousticModelBean) {
    this.acousticModelBean = acousticModelBean;
  }

  /**
   * Setter dla pola 'corpusBean'.
   *
   * @param corpusBean wartosc ustawiana dla pola 'corpusBean'.
   */
  public void setCorpusBean(CorpusBean corpusBean) {
    this.corpusBean = corpusBean;
  }

  /**
   * Setter dla pola 'dictionaryBean'.
   *
   * @param dictionaryBean wartosc ustawiana dla pola 'dictionaryBean'.
   */
  public void setDictionaryBean(DictionaryBean dictionaryBean) {
    this.dictionaryBean = dictionaryBean;
  }

  /**
   * Setter dla pola 'recognizerBean'.
   *
   * @param recognizerBean wartosc ustawiana dla pola 'recognizerBean'.
   */
  public void setRecognizerBean(RecognizerBean recognizerBean) {
    this.recognizerBean = recognizerBean;
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
   * Setter dla pola 'commandManager'.
   *
   * @param commandManager wartosc ustawiana dla pola 'commandManager'.
   */
  public void setCommandManager(CommandManager commandManager) {
    this.commandManager = commandManager;
  }

  /**
   * Setter dla pola 'commandGroupMap'.
   *
   * @param commandGroupMap wartosc ustawiana dla pola 'commandGroupMap'.
   */
  public void setCommandGroupMap(Map<String, List<String>> commandGroupMap) {
    this.commandGroupMap = commandGroupMap;
  }

  private class DictionariesTreeNode extends DynamicMutableTreeNode {

    private boolean invalidated;

    public DictionariesTreeNode(Object userObject) {
      super(userObject);
      invalidated = true;
    }

    protected void loadChildren() {
      if (invalidated) {
        invalidated = false;
        removeAllChildren();
        List<DictionaryEntity> dicts = dictionaryBean.getAllDictionaries();
        for (DictionaryEntity dict : dicts) {
          add(new DefaultMutableTreeNode(new GenericTreeNode(NodeType.DICTIONARY, "", dict.getName(),
              "dictionaryNode.icon", dict)));
        }
      }
    }

    public void invalidate() {
      invalidated = true;
    }
  }

  private class CorporaTreeNode extends DynamicMutableTreeNode {

    private boolean invalidated;

    public CorporaTreeNode(Object userObject) {
      super(userObject);
      invalidated = true;
    }

    protected void loadChildren() {
      if (invalidated) {
        invalidated = false;
        removeAllChildren();
        List<CorpusEntity> corpora = corpusBean.getAllCorpora();
        for (CorpusEntity corpus : corpora) {
          add(new DefaultMutableTreeNode(new GenericTreeNode(NodeType.CORPUS, "", corpus.getName(),
              "corpusNode.icon", corpus)));
        }
      }
    }

    public void invalidate() {
      invalidated = true;
    }
  }
}