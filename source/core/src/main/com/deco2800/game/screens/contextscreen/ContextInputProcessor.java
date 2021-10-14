package com.deco2800.game.screens.contextscreen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextInputProcessor implements InputProcessor {
    private final Logger logger = LoggerFactory.getLogger(com.deco2800.game.screens.contextscreen.ContextInputProcessor.class);

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ENTER:
                ContextScreenDisplay.playButton();
                logger.info("Enter Key Pressed");
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
