package com.deco2800.game.screens;

import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.context.ContextScreenActions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ContextScreenActionsTest {

    /**
     * Tests whether the play Game function is working by changing the game screen.
     */
    @Test
    void playGameTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        ContextScreenActions contextScreenActions = new ContextScreenActions();
        contextScreenActions.playGame();
        verify(game).setScreen(GdxGame.ScreenType.MAIN_GAME);
    }
}
