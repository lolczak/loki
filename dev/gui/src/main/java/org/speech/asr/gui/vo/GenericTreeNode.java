package org.speech.asr.gui.vo;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 22, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class GenericTreeNode {

  private Object nodeType;

  private String nameKey;

  private String name;

  private String iconKey;

  private Object userObject;

  public GenericTreeNode(Object nodeType, String nameKey, String name, String iconKey) {
    this.nodeType = nodeType;
    this.nameKey = nameKey;
    this.name = name;
    this.iconKey = iconKey;
  }

  public GenericTreeNode(Object nodeType, String nameKey, String name, String iconKey, Object userObject) {
    this.nodeType = nodeType;
    this.nameKey = nameKey;
    this.name = name;
    this.iconKey = iconKey;
    this.userObject = userObject;
  }


  /**
   * Getter dla pola 'iconKey'.
   *
   * @return wartosc pola 'iconKey'.
   */
  public String getIconKey() {
    return iconKey;
  }

  /**
   * Setter dla pola 'iconKey'.
   *
   * @param iconKey wartosc ustawiana dla pola 'iconKey'.
   */
  public void setIconKey(String iconKey) {
    this.iconKey = iconKey;
  }

  /**
   * Getter dla pola 'name'.
   *
   * @return wartosc pola 'name'.
   */
  public String getName() {
    return name;
  }

  /**
   * Setter dla pola 'name'.
   *
   * @param name wartosc ustawiana dla pola 'name'.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getter dla pola 'nameKey'.
   *
   * @return wartosc pola 'nameKey'.
   */
  public String getNameKey() {
    return nameKey;
  }

  /**
   * Setter dla pola 'nameKey'.
   *
   * @param nameKey wartosc ustawiana dla pola 'nameKey'.
   */
  public void setNameKey(String nameKey) {
    this.nameKey = nameKey;
  }

  /**
   * Getter dla pola 'nodeType'.
   *
   * @return wartosc pola 'nodeType'.
   */
  public Object getNodeType() {
    return nodeType;
  }

  /**
   * Setter dla pola 'nodeType'.
   *
   * @param nodeType wartosc ustawiana dla pola 'nodeType'.
   */
  public void setNodeType(Object nodeType) {
    this.nodeType = nodeType;
  }

  /**
   * Getter dla pola 'userObject'.
   *
   * @return wartosc pola 'userObject'.
   */
  public Object getUserObject() {
    return userObject;
  }

  /**
   * Setter dla pola 'userObject'.
   *
   * @param userObject wartosc ustawiana dla pola 'userObject'.
   */
  public void setUserObject(Object userObject) {
    this.userObject = userObject;
  }

  public String toString() {
    return "GenericTreeNode{" +
        "iconKey='" + iconKey + '\'' +
        ", nodeType=" + nodeType +
        ", nameKey='" + nameKey + '\'' +
        ", name='" + name + '\'' +
        ", userObject=" + userObject +
        '}';
  }
}
