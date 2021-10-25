package com.deco2800.game.screens.mainmenu;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.KeyboardMenuDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
  private GdxGame game;
  private KeyboardMenuDisplay activeDisplay;
  private TitleDisplay titleDisplay;
  private MenuDisplay menuDisplay;
  private SettingsDisplay settingsDisplay;

  @Override
  public void create() {
    titleDisplay = entity.getComponent(TitleDisplay.class);
    menuDisplay = entity.getComponent(MenuDisplay.class);
    settingsDisplay = entity.getComponent(SettingsDisplay.class);

    activeDisplay = titleDisplay;
    menuDisplay.setEnabled(false);
    settingsDisplay.setEnabled(false);

    entity.getEvents().addListener("menu", this::onMenuDisplay);
    entity.getEvents().addListener("settings", this::onSettingsDisplay);
    entity.getEvents().addListener("start", this::onStart);
    entity.getEvents().addListener("leaderboard", this::onLeaderboard);
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

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart() {
    logger.info("Read Context");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.CONTEXT);
  }

  /**
   * Swaps to the leaderboard screen.
   *
   */
  private void onLeaderboard() {
    logger.info("Launching leaderboard screen");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.LEADERBOARD);
  }

  /**
   * Exits the game.
   */
  private void onExit() {
    logger.info("Exit game");
    ServiceLocator.getGame().exit();
  }
}
