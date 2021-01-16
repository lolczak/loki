package org.speech.asr.gui.util.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.speech.asr.gui.constant.EditorsContainerConstants.*;
import org.speech.asr.gui.util.command.CommandWrapper;
import org.speech.asr.gui.util.image.ScalingSupportingImageSource;
import org.speech.asr.gui.view.editor.ButtonDescriptor;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.CommandServices;
import org.springframework.richclient.command.config.CommandButtonIconInfo;
import org.springframework.richclient.command.config.CommandButtonLabelInfo;
import org.springframework.richclient.command.config.CommandFaceDescriptor;

import javax.swing.*;
import java.util.Locale;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 3, 2009 <br/>
 *
 * @author Lukasz Olczak
 */
public class CommandButtonFactory {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(CommandButtonFactory.class.getName());

  public static AbstractButton createButton(ButtonDescriptor descriptor, ScalingSupportingImageSource imgSource,
                                            MessageSource msgSource) {

    CommandWrapper cmd = new CommandWrapper(descriptor.getClosure());
    cmd.setFaceDescriptor(createToolbarDescriptor(descriptor, imgSource, msgSource));

    CommandServices serv = (CommandServices) ApplicationServicesLocator.services().getService(CommandServices.class);

    AbstractButton button =
        cmd.createButton(serv.getToolBarButtonFactory(), serv.getToolBarButtonConfigurer());
    return button;
  }

  private static CommandFaceDescriptor createToolbarDescriptor(ButtonDescriptor descriptor,
                                                               ScalingSupportingImageSource imgSource,
                                                               MessageSource msgSource) {
    String key = descriptor.getKey();
    String labelKey = key + TITLE_MSG_KEY_SUFFIX;
    String tooltipKey = key + TOOLTIP_MSG_KEY_SUFFIX;
    String iconKey = key + ICON_MSG_KEY_SUFFIX;

    String label = msgSource.getMessage(labelKey, new Object[]{}, labelKey, Locale.getDefault());
    String tooltip = msgSource.getMessage(tooltipKey, new Object[]{}, tooltipKey, Locale.getDefault());
    Icon icon = imgSource.getIcon16x16(iconKey);
    CommandFaceDescriptor face = new CommandFaceDescriptor();
    CommandButtonLabelInfo labelInfo = CommandButtonLabelInfo.valueOf(label);
    CommandButtonIconInfo iconInfo = new CommandButtonIconInfo(icon);
    face.setCaption(tooltip);
//    face.setDescription("long" + tooltip);
    face.setIconInfo(iconInfo);
    face.setLabelInfo(labelInfo);

    return face;
  }
}
