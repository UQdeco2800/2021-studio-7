package com.deco2800.game.components.maingame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class MainGameActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainGameActions.class);
  private GdxGame game;

  public MainGameActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("defaultWin", this::onDefaultWin);
    entity.getEvents().addListener("timedLoss", this::onTimedLoss);
    entity.getEvents().addListener("caughtLoss", this::onCaughtLoss);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onExit() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to main menu screen...");
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  /**
   * Swaps to the Main Menu screen for now.
   */
  private void onDefaultWin() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to default win screen...");
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  /**
   * Swaps to the Main Menu screen for now.
   */
  private void onTimedLoss() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to default loss screen...");
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  /**
   * Swaps to the Main Menu screen for now.
   */
  private void onCaughtLoss() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to default loss screen...");
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }
}
