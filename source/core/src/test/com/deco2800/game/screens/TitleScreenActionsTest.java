package com.deco2800.game.screens;

import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.contextscreen.ContextScreenActions;
import com.deco2800.game.screens.titlescreen.TitleScreenActions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class TitleScreenActionsTest {
    @Test
    void goMenuTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        TitleScreenActions contextScreenActions = new TitleScreenActions();
        contextScreenActions.goMenu();
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
}
