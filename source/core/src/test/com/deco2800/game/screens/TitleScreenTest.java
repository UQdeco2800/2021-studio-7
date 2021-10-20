package com.deco2800.game.screens;

import com.badlogic.gdx.Input;
import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.input.KeyboardInputFactory;
import com.deco2800.game.input.components.InputComponent;
import com.deco2800.game.screens.title.TitleScreenActions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
class TitleScreenTest {

    /**
     * Tests whether the title screen is working in creation
     */
    @Test
    void titleScreenTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        game.setScreen(GdxGame.ScreenType.TITLE_SCREEN);
        verify(game).setScreen(GdxGame.ScreenType.TITLE_SCREEN);
    }

    /**
     * Tests whether the goMenu function is working
     */
    @Test
    void moveToMainMenuTest() {
        GdxGame game = mock(GdxGame.class);
        ServiceLocator.registerGame(game);
        game.setScreen(GdxGame.ScreenType.TITLE_SCREEN);
        TitleScreenActions titleScreenActions = new TitleScreenActions();
        titleScreenActions.goMenu();
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * Tests whether the key down function is working by reading a keyboard input
     */
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
