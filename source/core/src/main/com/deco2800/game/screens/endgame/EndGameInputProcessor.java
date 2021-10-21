package com.deco2800.game.screens.endgame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndGameInputProcessor implements InputProcessor {
    private final Logger logger = LoggerFactory.getLogger(EndGameInputProcessor.class);
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                EndGameDisplay.buttonLogic("Escape");
                logger.info("Escape Key Pressed");
                break;
            case Input.Keys.ENTER:
                EndGameDisplay.buttonLogic("Enter");
                EndGameDisplay.resetHover();
                logger.info("Enter Key Pressed");
                break;
            case Input.Keys.UP:
            case Input.Keys.W:
                EndGameDisplay.moveUp();
                logger.info("Up or W key pressed");
                break;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                EndGameDisplay.moveDown();
                logger.info("Down or S key pressed");
                break;
            default:
                logger.debug("Default case error in keyDown processing");
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
