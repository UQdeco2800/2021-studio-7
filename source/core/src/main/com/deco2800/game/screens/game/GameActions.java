package com.deco2800.game.screens.game;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveActions;
import com.deco2800.game.screens.SettingsDisplay;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class GameActions extends RetroactiveActions {
  private final GameScreen screen;
  private ContextDisplay contextDisplay;
  private PauseDisplay pauseDisplay;
  private SettingsDisplay settingsDisplay;

  public GameActions(GameScreen screen) {
    this.screen = screen;
  }

  @Override
  public void create() {
    contextDisplay = entity.getComponent(ContextDisplay.class);
    pauseDisplay = entity.getComponent(PauseDisplay.class);
    settingsDisplay = entity.getComponent(SettingsDisplay.class);
    
    contextDisplay.show();
    entity.getEvents().addListener("exit_context", this::onExitContextDisplay);

    entity.getEvents().addListener("enter_pause", this::onEnterPauseDisplay);
    entity.getEvents().addListener("exit_pause", this::onExitPauseDisplay);

    entity.getEvents().addListener("enter_settings", this::onEnterSettingsDisplay);
    entity.getEvents().addListener("exit_settings", this::onExitSettingsDisplay);

    entity.getEvents().addListener("queue_main_game", this::onQueueMainGame);
    entity.getEvents().addListener("queue_main_menu", this::onQueueMainMenu);
    entity.getEvents().addListener("bed_interacted", this::onBedInteracted);
    entity.getEvents().addListener("player_caught", this::onPlayerCaught);
    entity.getEvents().addListener("timer_ended", this::onTimerEnded);
  }

  protected void onExitContextDisplay() {
    playSound("confirm");
    contextDisplay.hide();
  }

  protected void onEnterPauseDisplay() {
    playSound("confirm");
    screen.pause();
    pauseDisplay.show();
  }

  protected void onExitPauseDisplay() {
    playSound("confirm");
    pauseDisplay.hide();
    screen.resume();
  }

  protected void onEnterSettingsDisplay() {
    playSound("confirm");
    pauseDisplay.hide();
    settingsDisplay.show();
  }

  protected void onExitSettingsDisplay() {
    playSound("confirm");
    settingsDisplay.hide();
    pauseDisplay.show();
  }

  protected void onQueueMainGame() {
    playSound("confirm");
    logger.debug("Queueing main game screen transition");
    ServiceLocator.getGame().setLevel(1);
    screen.queueNextScreen(GdxGame.ScreenType.MAIN_GAME);
  }

  public void onQueueMainMenu() {
    playSound("confirm");
    logger.debug("Queueing main menu screen transition");
    ServiceLocator.getGame().setLevel(1);
    screen.queueNextScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  public void onBedInteracted() {
    logger.debug("Queueing win screen transition");
    screen.queueNextScreen(GdxGame.ScreenType.WIN_DEFAULT);
  }

  public void onPlayerCaught() {
    logger.debug("Queuing player caught lose screen transition");
    screen.queueNextScreen(GdxGame.ScreenType.LOSS_CAUGHT);
  }

  public void onTimerEnded() {
    logger.debug("Queueing timer ended lose screen transition");
    screen.queueNextScreen(GdxGame.ScreenType.LOSS_TIMED);
  }

  @Override
  public void loadAssets() {
    logger.debug("    Loading game actions assets");
    super.loadAssets();
  }

  @Override
  public void unloadAssets() {
    logger.debug("    Unloading game actions assets");
    super.unloadAssets();
  }
}
