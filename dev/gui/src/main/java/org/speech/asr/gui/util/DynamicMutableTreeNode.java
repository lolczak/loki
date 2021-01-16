package org.speech.asr.gui.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 28, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public abstract class DynamicMutableTreeNode extends DefaultMutableTreeNode {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(DynamicMutableTreeNode.class.getName());

  public DynamicMutableTreeNode() {
    super();
  }

  public DynamicMutableTreeNode(Object userObject) {
    super(userObject);
  }

  public DynamicMutableTreeNode(Object userObject, boolean allowsChildren) {
    super(userObject,
        allowsChildren);  
  }

  /**
   * Returns the child <code>TreeNode</code> at index
   * <code>childIndex</code>.
   */
  public TreeNode getChildAt(int childIndex) {
    loadChildren();
    return super.getChildAt(childIndex);
  }

  /**
   * Returns the number of children <code>TreeNode</code>s the receiver
   * contains.
   */
  public int getChildCount() {
    loadChildren();
    return super.getChildCount();
  }

  /**
   * Returns the index of <code>node</code> in the receivers children.
   * If the receiver does not contain <code>node</code>, -1 will be
   * returned.
   */
  public int getIndex(TreeNode node) {
    loadChildren();
    return getIndex(node);
  }

  /**
   * Returns true if the receiver is a leaf.
   */
  public boolean isLeaf() {
    loadChildren();
    return super.isLeaf();
  }

  /**
   * Returns the children of the receiver as an <code>Enumeration</code>.
   */
  public Enumeration children() {
    loadChildren();
    return children();
  }

  protected abstract void loadChildren();

}
