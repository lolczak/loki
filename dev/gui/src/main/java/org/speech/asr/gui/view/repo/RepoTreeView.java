package org.speech.asr.gui.view.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.model.RepoTreeModel;
import org.springframework.richclient.application.support.AbstractView;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 22, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class RepoTreeView extends AbstractView {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(RepoTreeView.class.getName());

  private JTree jTree;

  private TreeCellRenderer treeCellRenderer;

  private RepoTreeModel repoTreeModel;

  public RepoTreeView() {
    jTree = null;
  }

  protected void createTree() {
    repoTreeModel.setCommandManager(getWindowCommandManager());
    jTree = new JTree(repoTreeModel);
    jTree.setCellRenderer(treeCellRenderer);
    jTree.setRootVisible(true);
    jTree.setShowsRootHandles(true);
    jTree.addTreeSelectionListener(repoTreeModel);
    jTree.addMouseListener(repoTreeModel);
  }

  protected JComponent createControl() {
    JPanel view = new JPanel(new BorderLayout());
    createTree();
    JScrollPane sp = getComponentFactory().createScrollPane(jTree);
    view.add(sp, BorderLayout.CENTER);
    return view;
  }

  /**
   * Setter dla pola 'treeCellRenderer'.
   *
   * @param treeCellRenderer wartosc ustawiana dla pola 'treeCellRenderer'.
   */
  public void setTreeCellRenderer(TreeCellRenderer treeCellRenderer) {
    this.treeCellRenderer = treeCellRenderer;
  }

  /**
   * Setter dla pola 'repoTreeModel'.
   *
   * @param repoTreeModel wartosc ustawiana dla pola 'repoTreeModel'.
   */
  public void setRepoTreeModel(RepoTreeModel repoTreeModel) {
    this.repoTreeModel = repoTreeModel;
  }
}
