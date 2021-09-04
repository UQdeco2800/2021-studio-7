package com.deco2800.game.screens.endgame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class EndGameActionsTest {
    @Test
    void onNextLevelTest() {
        GdxGame game = mock(GdxGame.class);
        EndGameActions endGameActions = new EndGameActions(game);
        endGameActions.onNextLevel();
        verify(game).setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    @Test
    void onExitTest() {
        GdxGame game = mock(GdxGame.class);
        EndGameActions endGameActions = new EndGameActions(game);
        endGameActions.onExit();
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
}
