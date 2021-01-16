package org.speech.asr.media.player;

/**
 * //@todo interface description
 * <p/>
 * Creation date: May 19, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface PlayerListener {

  void positionChanged(PlayerEvent event);

  void playerInitialized(PlayerEvent event);

  void playerStopped(PlayerEvent event);

  void playerStarted(PlayerEvent event);

  void playerPaused(PlayerEvent event);

}
