package org.speech.asr.gui.util.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.image.AwtImageResource;
import org.springframework.richclient.image.ImageSource;
import org.speech.asr.gui.util.image.ScalingSupportingImageSource;

import javax.swing.*;
import java.awt.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 25, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public class ScalingSupportingImageSourceImpl implements ScalingSupportingImageSource {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ScalingSupportingImageSourceImpl.class.getName());

  private ImageSource decorated;

  public ScalingSupportingImageSourceImpl(ImageSource decorated) {
    this.decorated = decorated;
  }

  public Icon getIcon(String key) {
    return new ImageIcon(getImage(key));
  }

  public Image getImage(String key) {
    return decorated.getImage(key);
  }

  public AwtImageResource getImageResource(String key) {
    return decorated.getImageResource(key);
  }

  public Icon getIcon(String key, int width, int height) {
    return getIcon(key, width, height, Image.SCALE_SMOOTH);
  }

  public Icon getIcon(String key, int width, int height, int hint) {
    return new ImageIcon(getImage(key).getScaledInstance(width, height, hint));
  }

  public Icon getIcon128x128(String key) {
    return getIcon(key, 128, 128);
  }

  public Icon getIcon16x16(String key) {
    return getIcon(key, 16, 16);
  }

  public Icon getIcon22x22(String key) {
    return getIcon(key, 22, 22);
  }

  public Icon getIcon24x24(String key) {
    return getIcon(key, 24, 24);
  }

  public Icon getIcon32x32(String key) {
    return getIcon(key, 32, 32);
  }

  public Icon getIcon48x48(String key) {
    return getIcon(key, 48, 48);
  }

  public Image getImage(String key, int width, int height) {
    return getImage(key, width, height, Image.SCALE_SMOOTH);
  }

  public Image getImage(String key, int width, int height, int hint) {
    return getImage(key).getScaledInstance(width, height, hint);
  }

  public Image getImage128x128(String key) {
    return getImage(key, 128, 128);
  }

  public Image getImage16x16(String key) {
    return getImage(key, 16, 16);
  }

  public Image getImage22x22(String key) {
    return getImage(key, 22, 22);
  }

  public Image getImage24x24(String key) {
    return getImage(key, 24, 24);
  }

  public Image getImage32x32(String key) {
    return getImage(key, 32, 32);
  }

  public Image getImage48x48(String key) {
    return getImage(key, 48, 48);
  }
}
