package com.deco2800.game.screens.mainmenu;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
  private GdxGame game;

  @Override
  public void create() {
    entity.getEvents().addListener("start", this::onStart);
    entity.getEvents().addListener("leaderboard", this::onLeaderboard);
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("settings", this::onSettings);
    entity.getEvents().addListener("change_character", this::onChangeCharacter);
    entity.getEvents().addListener("upKey", this::onExit);
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

  /**
   * Swaps to the Settings screen.
   */
  private void onSettings() {
    logger.info("Launching settings screen");
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.SETTINGS);
  }


  private void onChangeCharacter(){
    logger.info("Changing character");
  }
}
