package com.deco2800.game.screens.mainmenu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuInputProcessor implements InputProcessor {
    private final Logger logger = LoggerFactory.getLogger(MenuInputProcessor.class);
    private int menuIndex;
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.UP:
                MainMenuDisplay.moveUp();
                logger.info("Up Key Pressed");
                break;
            case Keys.DOWN:
                MainMenuDisplay.moveDown();
                logger.info("Down Key Pressed");
                break;
            case Keys.ENTER:
                logger.info("Enter Key Pressed");
                MainMenuDisplay.pressMenu();
                break;

        }
        return false;
    }


    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}