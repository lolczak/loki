package org.speech.asr.gui.util.image;

import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.image.ImageSource;

import javax.swing.*;
import java.awt.*;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Apr 25, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface ScalingSupportingImageSource extends IconSource, ImageSource {

  Icon getIcon(String key);

  Image getImage(String key);

  Icon getIcon16x16(String key);

  Icon getIcon22x22(String key);

  Icon getIcon24x24(String key);

  Icon getIcon32x32(String key);

  Icon getIcon48x48(String key);

  Icon getIcon128x128(String key);

  Icon getIcon(String key, int width, int height);

  Icon getIcon(String key, int width, int height, int hint);

  Image getImage16x16(String key);

  Image getImage22x22(String key);

  Image getImage24x24(String key);

  Image getImage32x32(String key);

  Image getImage48x48(String key);

  Image getImage128x128(String key);

  Image getImage(String key, int width, int height);

  Image getImage(String key, int width, int height, int hint);
}
