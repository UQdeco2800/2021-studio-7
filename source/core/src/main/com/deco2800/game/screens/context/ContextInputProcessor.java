package com.deco2800.game.screens.context;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextInputProcessor extends UIComponent implements InputProcessor {
    private final Logger logger = LoggerFactory.getLogger(com.deco2800.game.screens.context.ContextInputProcessor.class);

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ENTER:
                ContextScreenDisplay display = entity.getComponent(ContextScreenDisplay.class);
                logger.info("Enter Key Pressed");
                if (display.getStoryStatus() && display.getPrintStatus()) {
                    ContextScreenActions.playGame();
                }
                if (ContextScreen.getScreen() == 1) {
                    if (display.userNameValid()) {
                        entity.getComponent(ContextScreenActions.class).writeUsername();
                        display.clearTable();
                        display.tellStory();
                    } else {
                            display.displayWarning();
                    }
                }
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
