package com.deco2800.game.screens.context;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.context.ContextScreen;
import com.deco2800.game.screens.context.ContextScreenDisplay;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextInputProcessor extends UIComponent implements InputProcessor {
    private final Logger logger = LoggerFactory.getLogger(com.deco2800.game.screens.context.ContextInputProcessor.class);

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ENTER:
                logger.info("Enter Key Pressed");
                if (entity.getComponent(ContextScreenDisplay.class).userNameValid()) {
                    entity.getComponent(ContextScreenActions.class).writeUsername();
                    ContextScreenActions.playGame();
                } else {
                    entity.getComponent(ContextScreenDisplay.class).displayWarning();
                }
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

    @Override
    protected void draw(SpriteBatch batch) {
        //draw is handled by the stage
    }
}
