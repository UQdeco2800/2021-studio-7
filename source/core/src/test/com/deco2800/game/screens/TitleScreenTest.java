package com.deco2800.game.screens;

import com.badlogic.gdx.Input;
import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.titlescreen.KeyboardTitleInputComponent;
import com.deco2800.game.screens.titlescreen.TitleScreenActions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class TitleScreenTest {
    @Test
    void pressAnyKeyTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);

        game.setScreen(GdxGame.ScreenType.TITLE_SCREEN);

        KeyboardTitleInputComponent keyboardTitleInputComponent = new KeyboardTitleInputComponent();
        keyboardTitleInputComponent.keyDown(Input.Keys.A);
        verify(game).setScreen(GdxGame.ScreenType.TITLE_SCREEN);
    }
}
