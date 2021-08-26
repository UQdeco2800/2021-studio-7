package com.deco2800.game.components.wingame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class WinGameActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(WinGameActions.class);
  private GdxGame game;

  public WinGameActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("exit", this::onExit);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onExit() {
    logger.info("Exiting main game screen...");
    logger.info("Swapping to main menu screen...");
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

}
