package com.deco2800.game.screens.maingame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class MainGameActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainGameActions.class);

  @Override
  public void create() {
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("bed_interacted", this::onBedInteracted);
    entity.getEvents().addListener("player_caught", this::onPlayerCaught);
    entity.getEvents().addListener("timed_ended", this::onTimerEnded);
  }

  public void onExit() {
    logger.debug("Exiting main game screen...");
    logger.debug("Swapping to main menu screen...");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  public void onBedInteracted() {
    logger.debug("Exiting main game screen...");
    logger.debug("Swapping to default win screen...");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.WIN_DEFAULT);
  }

  public void onPlayerCaught() {
    logger.debug("Exiting main game screen...");
    logger.debug("Swapping to default loss screen...");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.LOSS_CAUGHT);
  }

  public void onTimerEnded() {
    logger.debug("Exiting main game screen...");
    logger.debug("Swapping to default loss screen...");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.LOSS_TIMED);
  }
}
