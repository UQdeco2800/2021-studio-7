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
    entity.getEvents().addListener("resume", this::onResume);
    entity.getEvents().addListener("restart", this::onRestart);
    entity.getEvents().addListener("settings", this::onSettings);
    entity.getEvents().addListener("main_menu", this::onMainMenu);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  public void onExit() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to main menu screen...");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  /**
   * Swaps to the Main Menu screen for now.
   */
  public void onWinDefault() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to default win screen...");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.WIN_DEFAULT);
  }

  /**
   * Swaps to the Main Menu screen for now.
   */
  public void onLossTimed() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to default loss screen...");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.LOSS_TIMED);
  }

  /**
   * Swaps to the Main Menu screen for now.
   */
  public void onLossCaught() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to default loss screen...");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.LOSS_CAUGHT);
  }

  /**
   * Restarts game, by resetting game by running start.
   */
  public void onRestart() {
    logger.info("Restart game");
//        ServiceLocator.getGame().dispose();
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_GAME);
  }

  /**
   * Resumes game, by unpausing game state.
   */
  public void onResume() {
    logger.info("Resume game");
//        ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_GAME);
    ServiceLocator.getGame().getScreen().resume();

//    ServiceLocator.getGame().resume();
  }

  /**
   * Opens settings menu.
   */
  public void onSettings() {
    logger.info("Launch settings screen");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.SETTINGS);
  }

  /**
   * Opens main menu.
   */
  public void onMainMenu() {
    logger.info("Launch main menu screen");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_MENU);
  }
}
