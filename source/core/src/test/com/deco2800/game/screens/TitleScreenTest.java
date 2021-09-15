package com.deco2800.game.screens;

import com.badlogic.gdx.Input;
import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.input.KeyboardInputFactory;
import com.deco2800.game.input.components.InputComponent;
import com.deco2800.game.screens.titlescreen.TitleScreenActions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
public class TitleScreenTest {
    @Test
    void titleScreenTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        game.setScreen(GdxGame.ScreenType.TITLE_SCREEN);
        verify(game).setScreen(GdxGame.ScreenType.TITLE_SCREEN);
    }

    @Test
    void moveToMainMenuTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        game.setScreen(GdxGame.ScreenType.TITLE_SCREEN);
        TitleScreenActions titleScreenActions = new TitleScreenActions();
        titleScreenActions.goMenu();
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    @Test
    void keyDownTest() {
        InputService inputService = new InputService();
        ServiceLocator.registerInputService(inputService);

        KeyboardInputFactory keyboardInputFactory = new KeyboardInputFactory();
        InputComponent keyboardInput = keyboardInputFactory.createForTitle();

        ServiceLocator.getInputService().register(keyboardInput);
        assertTrue(keyboardInput.keyDown(Input.Keys.A));
    }
}
