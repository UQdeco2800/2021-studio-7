package com.deco2800.game.screens;

import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.title.TitleScreenActions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class TitleScreenActionsTest {

    /**
     * This tests whether the goMenu function is working by changing the game screen
     */
    @Test
    void goMenuTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        TitleScreenActions contextScreenActions = new TitleScreenActions();
        contextScreenActions.goMenu();
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
}
