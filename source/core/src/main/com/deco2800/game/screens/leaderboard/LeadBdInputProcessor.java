package com.deco2800.game.screens.leaderboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeadBdInputProcessor implements InputProcessor {
    private final Logger logger = LoggerFactory.getLogger(com.deco2800.game.screens.leaderboard.LeadBdInputProcessor.class);

    @Override
    public boolean keyDown(int keycode) {
        if((Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) ||
                (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) ){
            LeaderBoardDisplay.exitLB();
            logger.info("Enter Key Pressed");
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
