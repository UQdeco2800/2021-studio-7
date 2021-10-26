package com.deco2800.game.screens.menu;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveActions;
import com.deco2800.game.screens.SettingsDisplay;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MenuActions extends RetroactiveActions {
  private static final String BACKGROUND_MUSIC = "sounds/backgroundMusic-EP.mp3";
  private final MenuScreen screen;
  private TitleDisplay titleDisplay;
  private MenuDisplay menuDisplay;
  private LeaderboardDisplay leaderboardDisplay;
  private SettingsDisplay settingsDisplay;

  public MenuActions(MenuScreen screen) {
    this.screen = screen;
    backgroundMusic = BACKGROUND_MUSIC;
  }

  @Override
  public void create() {
    titleDisplay = entity.getComponent(TitleDisplay.class);
    menuDisplay = entity.getComponent(MenuDisplay.class);
    leaderboardDisplay = entity.getComponent(LeaderboardDisplay.class);
    settingsDisplay = entity.getComponent(SettingsDisplay.class);

    titleDisplay.show();
    entity.getEvents().addListener("exit_title", this::onExitTitleDisplay);

    entity.getEvents().addListener("enter_leaderboard", this::onEnterLeaderboardDisplay);
    entity.getEvents().addListener("exit_leaderboard", this::onExitLeaderboardDisplay);

    entity.getEvents().addListener("enter_settings", this::onEnterSettingsDisplay);
    entity.getEvents().addListener("exit_settings", this::onExitSettingsDisplay);

    entity.getEvents().addListener("main_game", this::onQueueMainGame);
    entity.getEvents().addListener("exit", this::onExit);
  }

  private void onExitTitleDisplay() {
    playSound("confirm");
    titleDisplay.hide();
    menuDisplay.show();
  }

  private void onEnterLeaderboardDisplay() {
    playSound("confirm");
    menuDisplay.hide();
    leaderboardDisplay.show();
  }

  private void onExitLeaderboardDisplay() {
    playSound("confirm");
    leaderboardDisplay.hide();
    menuDisplay.show();
  }

  private void onEnterSettingsDisplay() {
    playSound("confirm");
    menuDisplay.hide();
    settingsDisplay.show();
  }

  private void onExitSettingsDisplay() {
    playSound("confirm");
    settingsDisplay.hide();
    menuDisplay.show();
  }

  private void onQueueMainGame() {
    playSound("confirm");
    logger.debug("Queueing main game screen transition");
    screen.queueNextScreen(GdxGame.ScreenType.MAIN_GAME);
  }

  private void onExit() {
    playSound("confirm");
    logger.debug("Exiting game");
    ServiceLocator.getGame().exit();
  }
}
