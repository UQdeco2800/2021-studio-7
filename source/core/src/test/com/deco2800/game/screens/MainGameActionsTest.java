package com.deco2800.game.screens;

import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.maingame.MainGameActions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.security.Provider;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class MainGameActionsTest {

    @Test
    void onExitTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        MainGameActions mainGameActions = new MainGameActions();
        mainGameActions.onExit();
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    @Test
    void onWinDefaultTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        MainGameActions mainGameActions = new MainGameActions();
        mainGameActions.onWinDefault();
        verify(game).setScreen(GdxGame.ScreenType.WIN_DEFAULT);
    }

    @Test
    void onLossTimedTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        MainGameActions mainGameActions = new MainGameActions();
        mainGameActions.onLossTimed();
        verify(game).setScreen(GdxGame.ScreenType.LOSS_TIMED);
    }

    @Test
    void onLossCaughtTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        MainGameActions mainGameActions = new MainGameActions();
        mainGameActions.onLossCaught();
        verify(game).setScreen(GdxGame.ScreenType.LOSS_CAUGHT);
    }

    @Test
    void onRestartTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        MainGameActions mainGameActions = new MainGameActions();
        mainGameActions.onRestart();
        verify(game).setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    @Test
    void onSettingsTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        MainGameActions mainGameActions = new MainGameActions();
        mainGameActions.onSettings();
        verify(game).setScreen(GdxGame.ScreenType.SETTINGS);
    }

    @Test
    void onMainMenuTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        MainGameActions mainGameActions = new MainGameActions();
        mainGameActions.onMainMenu();
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
}
