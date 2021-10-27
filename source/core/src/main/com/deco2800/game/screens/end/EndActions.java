package com.deco2800.game.screens.end;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveActions;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class EndActions extends RetroactiveActions {
  private final EndScreen screen;

  public EndActions(EndScreen screen) {
    this.screen = screen;
  }

  @Override
  public void create() {
    EndDisplay endDisplay = entity.getComponent(EndDisplay.class);

    endDisplay.show();

    entity.getEvents().addListener("main_game", this::onQueueMainGame);
    entity.getEvents().addListener("main_menu", this::onQueueMainMenu);
  }

  protected void onQueueMainGame() {
    logger.debug("Queueing main game screen transition");
    screen.queueNextScreen(GdxGame.ScreenType.MAIN_GAME);
  }

  protected void onQueueMainMenu() {
    logger.debug("Queueing main menu screen transition");
    ServiceLocator.getGame().setLevel(1);
    screen.queueNextScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  @Override
  public void loadAssets() {
    logger.debug("    Loading end actions assets");
    super.loadAssets();
  }

  @Override
  public void unloadAssets() {
    logger.debug("    Unloading end actions assets");
    super.unloadAssets();
  }
}