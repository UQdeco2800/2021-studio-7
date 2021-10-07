package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.contextscreen.ContextScreen;
import com.deco2800.game.screens.EndGameScreen;
import com.deco2800.game.screens.maingame.MainGameScreen;
import com.deco2800.game.screens.mainmenu.MainMenuScreen;
import com.deco2800.game.screens.settingsmenu.SettingsScreen;
import com.deco2800.game.screens.leaderboardscreen.LeaderBoardScreen;
import com.deco2800.game.screens.titlescreen.TitleScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.Gdx.app;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is currently running.
 * The current screen triggers transitions to other screens. This works similarly to a finite state
 * machine (See the State Pattern).
 */
public class GdxGame extends Game {
  private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);

  @Override
  public void create() {
    logger.info("Creating game");
    loadSettings();

    // Sets background to black
    Gdx.gl.glClearColor(0/255f, 0/255f, 0/255f, 1);

    setScreen(ScreenType.TITLE_SCREEN);
    ServiceLocator.registerGame(this);
  }

  /**
   * Loads the game's settings.
   */
  private void loadSettings() {
    logger.debug("Loading game settings");
    UserSettings.Settings settings = UserSettings.get();
    UserSettings.applySettings(settings);
  }

  /**
   * Sets the game's screen to a new screen of the provided type.
   * @param screenType screen type
   */
  public void setScreen(ScreenType screenType) {
    logger.info("Setting game screen to {}", screenType);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }
    setScreen(newScreen(screenType));
  }

  @Override
  public void dispose() {
    logger.debug("Disposing of current screen");
    getScreen().dispose();
  }

  /**
   * Create a new screen of the provided type.
   * @param screenType screen type
   * @return new screen
   */
  public Screen newScreen(ScreenType screenType) {
    switch (screenType) {
      case TITLE_SCREEN:
        return new TitleScreen();
      case MAIN_MENU:
        return new MainMenuScreen();
//      case PAUSE_MENU:
//        return new PauseMenuScreen();
      case MAIN_GAME:
        return new MainGameScreen();
      case SETTINGS:
        return new SettingsScreen();
      case CONTEXT:
        return new ContextScreen();
      case LEADERBOARD:
        return new LeaderBoardScreen(this);
      case WIN_DEFAULT:
        return new EndGameScreen(ScreenType.WIN_DEFAULT);
      case LOSS_TIMED:
        return new EndGameScreen(ScreenType.LOSS_TIMED);
      case LOSS_CAUGHT:
        return new EndGameScreen(ScreenType.LOSS_CAUGHT);
      default:
        return null;
    }
  }

  public enum ScreenType {
    MAIN_MENU, PAUSE_MENU, MAIN_GAME, SETTINGS, WIN_DEFAULT, LOSS_TIMED, LOSS_CAUGHT, CONTEXT, TITLE_SCREEN, LEADERBOARD
  }

  /**
   * Exit the game.
   */
  public void exit() {
    app.exit();
  }
}
