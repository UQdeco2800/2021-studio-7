package com.deco2800.game.screens.endgame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.deco2800.game.screens.mainmenu.MenuInputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndGameInputProcessor implements InputProcessor {
    private final Logger logger = LoggerFactory.getLogger(MenuInputProcessor.class);
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                EndGameDisplay.buttonLogic("Escape");
                logger.info("Escape Key Pressed");
                break;
            case Input.Keys.ENTER:
                EndGameDisplay.buttonLogic("Enter");
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
