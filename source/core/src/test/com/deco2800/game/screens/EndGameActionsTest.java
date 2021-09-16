package com.deco2800.game.screens;

import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.generic.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.security.Provider;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class EndGameActionsTest {
    @Test
    void onNextLevelTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        EndGameActions endGameActions = new EndGameActions();
        endGameActions.onNextLevel();
        verify(game).setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    @Test
    void onExitTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        EndGameActions endGameActions = new EndGameActions();
        endGameActions.onExit();
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
}
