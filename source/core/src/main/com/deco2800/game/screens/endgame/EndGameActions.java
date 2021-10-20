package com.deco2800.game.screens.endgame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.context.ContextScreen;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Provider;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class EndGameActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(EndGameActions.class);

  @Override
  public void create() {
    entity.getEvents().addListener("next_level", this::onNextLevel);
    entity.getEvents().addListener("exit", this::onExit);
  }

  /**
   * Swaps to the next level on the Main Game Screen.
   */
  public void onNextLevel() {
    logger.info("Exiting end game screen...");
    if (ContextScreen.getScreen() == 2) {
      logger.info("Swapping to second context screen");
      ServiceLocator.getGame().setScreen(GdxGame.ScreenType.CONTEXT);
    } else {
      logger.info("Swapping to next level on main game screen...");
      ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_GAME);
    }
  }

  /**
   * Swaps to the Main Menu screen.
   */
  public void onExit() {
    logger.info("Exiting end game screen...");
    logger.info("Swapping to main menu screen...");
    MainGameScreen.zeroLevel();
    ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_MENU);
  }
}