package com.deco2800.game.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.UserSettings.DisplaySettings;
import com.deco2800.game.files.UserSettings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class UserSettingsTest {
  @Test
  void shouldApplySettings() {
    Gdx.graphics = mock(Graphics.class);
    DisplayMode displayMode = mock(DisplayMode.class);
    when(Gdx.graphics.getDisplayMode()).thenReturn(displayMode);

    Settings settings = new Settings();
    settings.setVsync(true);
    settings.setDisplayMode(null);
    settings.setFullscreen(true);
    settings.setFps(40);
    UserSettings.applySettings(settings);

    verify(Gdx.graphics).setForegroundFPS(settings.getFps());
    verify(Gdx.graphics).setFullscreenMode(displayMode);
    verify(Gdx.graphics).setVSync(settings.isVsync());
  }

  @Test
  void shouldFindMatchingDisplay() {
    Gdx.graphics = mock(Graphics.class);
    DisplayMode correctMode = new CustomDisplayMode(200, 100, 60, 0);
    DisplayMode[] displayModes = {
        new CustomDisplayMode(100, 200, 30, 0),
        new CustomDisplayMode(100, 200, 60, 0),
        correctMode
    };
    when(Gdx.graphics.getDisplayModes()).thenReturn(displayModes);

    Settings settings = new Settings();
    settings.setDisplayMode(new DisplaySettings());
    settings.getDisplayMode().height = 100;
    settings.getDisplayMode().width = 200;
    settings.getDisplayMode().refreshRate = 60;
    settings.setFullscreen(true);
    UserSettings.applySettings(settings);

    verify(Gdx.graphics).setFullscreenMode(correctMode);
  }

  /**
   * This exists to make the constructor public
   */
  static class CustomDisplayMode extends DisplayMode {
    public CustomDisplayMode(int width, int height, int refreshRate, int bitsPerPixel) {
      super(width, height, refreshRate, bitsPerPixel);
    }
  }
}