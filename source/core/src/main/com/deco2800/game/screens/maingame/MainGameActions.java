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
    entity.getEvents().addListener("win_default", this::onWinDefault);
    entity.getEvents().addListener("loss_timed", this::onLossTimed);
    entity.getEvents().addListener("loss_caught", this::onLossCaught);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onExit() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to main menu screen...");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  /**
   * Swaps to the Main Menu screen for now.
   */
  private void onWinDefault() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to default win screen...");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.WIN_DEFAULT);
  }

  /**
   * Swaps to the Main Menu screen for now.
   */
  private void onLossTimed() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to default loss screen...");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.LOSS_TIMED);
  }

  /**
   * Swaps to the Main Menu screen for now.
   */
  private void onLossCaught() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to default loss screen...");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.LOSS_CAUGHT);
  }
}
