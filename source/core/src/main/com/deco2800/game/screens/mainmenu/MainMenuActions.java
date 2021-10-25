package com.deco2800.game.screens.mainmenu;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.KeyboardMenuDisplay;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
  private MainMenuScreen screen;
  private KeyboardMenuDisplay activeDisplay;
  private MenuDisplay menuDisplay;
  private LeaderboardDisplay leaderboardDisplay;
  private SettingsDisplay settingsDisplay;

  public MainMenuActions(MainMenuScreen screen) {
    this.screen = screen;
  }

  @Override
  public void create() {
    menuDisplay = entity.getComponent(MenuDisplay.class);
    leaderboardDisplay = entity.getComponent(LeaderboardDisplay.class);
    settingsDisplay = entity.getComponent(SettingsDisplay.class);

    activeDisplay = entity.getComponent(TitleDisplay.class);
    menuDisplay.setEnabled(false);
    leaderboardDisplay.setEnabled(false);
    settingsDisplay.setEnabled(false);

    entity.getEvents().addListener("menu", this::onMenuDisplay);
    entity.getEvents().addListener("leaderboard", this::onLeaderboardDisplay);
    entity.getEvents().addListener("settings", this::onSettingsDisplay);
    entity.getEvents().addListener("start", this::onStart);
    entity.getEvents().addListener("exit", this::onExit);
  }

  private void onMenuDisplay() {
    activeDisplay.setEnabled(false);
    menuDisplay.setEnabled(true);
    activeDisplay = menuDisplay;

  }

  private void onSettingsDisplay() {
    activeDisplay.setEnabled(false);
    settingsDisplay.setEnabled(true);
    activeDisplay = settingsDisplay;
  }

  private void onLeaderboardDisplay() {
    activeDisplay.setEnabled(false);
    leaderboardDisplay.setEnabled(true);
    activeDisplay = leaderboardDisplay;
  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart() {
    logger.info("Read Context");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.CONTEXT);
  }

  /**
   * Exits the game.
   */
  private void onExit() {
    logger.info("Exit game");
    ServiceLocator.getGame().exit();
  }
}
