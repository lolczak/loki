package org.speech.asr.gui.view.editor;

import java.awt.*;
import java.util.List;


/**
 * //@todo interface description
 * <p/>
 * Creation date: May 1, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface ItemEditor<T> {

  /**
   * Zwraca tytul edytora wyswietlany w zakladce.
   *
   * @return
   */
  String getTitle();

  /**
   * Klucz sluzacy do grupowania edytorow w kontenerze.
   *
   * @return
   */
  String getKey();

  /**
   * Zwraca formatke renderujaca widok modelu edytora.
   *
   * @return
   */
  Component getFormControl();

  /**
   * Wstrzykuje obiekt kontekstu - najczesciej element edytowany.
   *
   * @param context
   */
  void setContext(T context);

  /**
   * Pobiera opis toolbarow znajdujacych sie nad formatka edytora.
   *
   * @return
   */
  List<ToolbarDescriptor> getTopToolbars();

  /**
   * Pobiera opis toolbarow znajdujacych sie po lewej stronie formatki edytora.
   *
   * @return
   */
  List<ToolbarDescriptor> getLeftToolbars();

  /**
   * Pobiera opis toolbarow znajdujacych sie pod formatka edytora.
   *
   * @return
   */
  List<ToolbarDescriptor> getBottomToolbars();

  /**
   * Pobiera opis toolbarow znajdujacych sie po prawej stronie formatki edytora.
   *
   * @return
   */
  List<ToolbarDescriptor> getRightToolbars();

  /**
   * Odpalany na zamkniecie edytora.
   */
  void onClose();

  /**
   * Odpalany w momencie focusu na edytorze.
   */
  void onSelect();

  /**
   * Odpalany w momencie utworzenia edytora.
   */
  void onCreate();
}
