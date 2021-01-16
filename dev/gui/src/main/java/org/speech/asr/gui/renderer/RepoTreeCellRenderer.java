package org.speech.asr.gui.renderer;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.util.image.ScalingSupportingImageSource;
import org.speech.asr.gui.vo.GenericTreeNode;
import org.springframework.context.MessageSource;
import org.springframework.richclient.tree.FocusableTreeCellRenderer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.util.Locale;
import java.util.Map;

/**
 * Renderer wezlow drzewa reprezentujacego repozytorium jcr.
 * <p/>
 * Creation date: Apr 22, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class RepoTreeCellRenderer implements TreeCellRenderer {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(RepoTreeCellRenderer.class.getName());

  private Map<Class, TreeCellRenderer> renderers;

  private TreeCellRenderer defaultRenderer;

  private MessageSource messageSource;

  private ScalingSupportingImageSource scalingImageSource;

  public RepoTreeCellRenderer() {
    initRederers();
  }

  private void initRederers() {
    renderers = new HashedMap();
    renderers.put(GenericTreeNode.class, new GenericNodeCellRenderer());
    defaultRenderer = new DefaultCellRenderer();
  }


  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                boolean leaf, int row, boolean hasFocus) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
    Object userObject = node.getUserObject();
    TreeCellRenderer delegate = renderers.get(userObject.getClass());
    if (delegate == null) {
      delegate = defaultRenderer;
      userObject = value;
    }

    return delegate.getTreeCellRendererComponent(tree, userObject, selected, expanded, leaf, row, hasFocus);
  }

  private static class DefaultCellRenderer extends FocusableTreeCellRenderer {

  }


  private class GenericNodeCellRenderer extends FocusableTreeCellRenderer {
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
                                                  int row, boolean hasFocus) {
      super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
          hasFocus);
      GenericTreeNode node = (GenericTreeNode) value;
      this.setIcon(scalingImageSource.getIcon16x16(node.getIconKey()));
      this.setText(messageSource.getMessage(node.getNameKey(), null, node.getName(), Locale.getDefault()));

      return this;
    }
  }

  /**
   * Setter dla pola 'messageSource'.
   *
   * @param messageSource wartosc ustawiana dla pola 'messageSource'.
   */
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Setter dla pola 'scalingImageSource'.
   *
   * @param scalingImageSource wartosc ustawiana dla pola 'scalingImageSource'.
   */
  public void setScalingImageSource(ScalingSupportingImageSource scalingImageSource) {
    this.scalingImageSource = scalingImageSource;
  }
}
