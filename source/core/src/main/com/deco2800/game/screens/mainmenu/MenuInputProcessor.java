package com.deco2800.game.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuInputProcessor implements InputProcessor {
    private final Logger logger = LoggerFactory.getLogger(MenuInputProcessor.class);

    @Override
    public boolean keyDown(int keycode) {
        if((Gdx.input.isKeyJustPressed(Keys.UP)) ||
                (Gdx.input.isKeyJustPressed(Keys.W))){
            MainMenuDisplay.moveUp();
            logger.info("Up/W Key Pressed");
        }

        if((Gdx.input.isKeyJustPressed(Keys.DOWN)) ||
                (Gdx.input.isKeyJustPressed(Keys.S))){
            MainMenuDisplay.moveDown();
            logger.info("Down/S Key Pressed");
        }

        if(Gdx.input.isKeyJustPressed(Keys.ENTER)){
            MainMenuDisplay.pressMenu();
            logger.info("Enter Key Pressed");
        }

        if((Gdx.input.isKeyJustPressed(Keys.LEFT)) ||
                (Gdx.input.isKeyJustPressed(Keys.A))){
            MainMenuDisplay.toggleLeftBtn();
            logger.info("Left/A Key Pressed");
        }

        if((Gdx.input.isKeyJustPressed(Keys.RIGHT)) ||
                (Gdx.input.isKeyJustPressed(Keys.D))){
            MainMenuDisplay.toggleRightBtn();
            logger.info("Right/D Key Pressed");
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