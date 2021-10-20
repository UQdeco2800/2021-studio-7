package com.deco2800.game.screens;

import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.maingame.MainGameActions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.security.Provider;

import static org.mockito.Mockito.*;
/*
@ExtendWith(GameExtension.class)
class MainGameActionsTest {

    @Test
    void onExitTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        MainGameActions mainGameActions = new MainGameActions();
        mainGameActions.onMainMenu();
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    @Test
    void onBedInteractedTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        MainGameActions mainGameActions = new MainGameActions();
        mainGameActions.onBedInteracted();
        verify(game).setScreen(GdxGame.ScreenType.WIN_DEFAULT);
    }

    @Test
    void onTimerEndedTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        MainGameActions mainGameActions = new MainGameActions();
        mainGameActions.onTimerEnded();
        verify(game).setScreen(GdxGame.ScreenType.LOSS_TIMED);
    }

    @Test
    void onPlayerCaughtTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        MainGameActions mainGameActions = new MainGameActions();
        mainGameActions.onPlayerCaught();
        verify(game).setScreen(GdxGame.ScreenType.LOSS_CAUGHT);
    }
}
*/