package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.end.EndScreen;
import com.deco2800.game.screens.game.GameScreen;
import com.deco2800.game.screens.menu.MenuScreen;
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
  private String username;
  private int level = 0;

  @Override
  public void create() {
    logger.info("Creating game");
    loadSettings();

    // Sets background to black
    Gdx.gl.glClearColor(0/255f, 0/255f, 0/255f, 1);

    setScreen(ScreenType.MAIN_MENU);
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

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    if (this.username.isEmpty()) {
      String placeholder = "";
      return placeholder;
    } else {
      return this.username + "! ";
    }
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public int getLevel() {
    return level;
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
      case MAIN_MENU:
        return new MenuScreen(this);
      case MAIN_GAME:
        return new GameScreen(this);
      case WIN_DEFAULT:
        return new EndScreen(this, ScreenType.WIN_DEFAULT);
      case LOSS_TIMED:
        return new EndScreen(this, ScreenType.LOSS_TIMED);
      case LOSS_CAUGHT:
        return new EndScreen(this, ScreenType.LOSS_CAUGHT);
      default:
        return null;
    }
  }

  public enum ScreenType {
    MAIN_MENU, MAIN_GAME, WIN_DEFAULT, LOSS_TIMED, LOSS_CAUGHT
  }

  /**
   * Exit the game.
   */
  public void exit() {
    app.exit();
  }
}
