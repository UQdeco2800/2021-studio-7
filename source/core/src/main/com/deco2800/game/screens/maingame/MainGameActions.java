package com.deco2800.game.screens.maingame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.context.ContextScreen;
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
    entity.getEvents().addListener("pause", this::onPause);
    entity.getEvents().addListener("resume", this::onResume);
    entity.getEvents().addListener("restart", this::onRestart);
    entity.getEvents().addListener("settings", this::onSettings);
    entity.getEvents().addListener("main_menu", this::onMainMenu);
    entity.getEvents().addListener("bed_interacted", this::onBedInteracted);
    entity.getEvents().addListener("player_caught", this::onPlayerCaught);
    entity.getEvents().addListener("timer_ended", this::onTimerEnded);
  }

  public void onPause() {
    logger.debug("Pausing the game...");
    ServiceLocator.getGame().getScreen().pause();
  }

  public void onResume() {
    logger.debug("Resuming the game...");
    ServiceLocator.getGame().getScreen().resume();
  }

  public void onMainMenu() {
    logger.debug("Swapping to main menu screen...");
    MainGameScreen.zeroLevel();
    ContextScreen.screenZero();
    ServiceLocator.getScreen(MainGameScreen.class).queueNewScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  public void onRestart() {
    logger.debug("Swapping to new main game screen...");
    MainGameScreen.zeroLevel();
    ContextScreen.screenZero();
    ServiceLocator.getScreen(MainGameScreen.class).queueNewScreen(GdxGame.ScreenType.MAIN_GAME);
  }

  public void onSettings() {
    logger.debug("Swapping to settings screen...");
    MainGameScreen.zeroLevel();
    ContextScreen.screenZero();
    ServiceLocator.getScreen(MainGameScreen.class).queueNewScreen(GdxGame.ScreenType.SETTINGS);
  }

  public void onBedInteracted() {
    logger.debug("Swapping to win screen...");
    ServiceLocator.getScreen(MainGameScreen.class).queueNewScreen(GdxGame.ScreenType.WIN_DEFAULT);
  }

  public void onPlayerCaught() {
    logger.debug("Swapping to caught lose screen...");
    ServiceLocator.getScreen(MainGameScreen.class).queueNewScreen(GdxGame.ScreenType.LOSS_CAUGHT);
  }

  public void onTimerEnded() {
    logger.debug("Swapping to timed lose screen...");
    ServiceLocator.getScreen(MainGameScreen.class).queueNewScreen(GdxGame.ScreenType.LOSS_TIMED);
  }
}
