package com.deco2800.game.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.generic.Loadable;
import com.deco2800.game.input.components.KeyboardMenuInputComponent;
import com.deco2800.game.rendering.components.RenderPriority;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public abstract class RetroactiveDisplay extends UIComponent implements Loadable {
    protected static final Logger logger = LoggerFactory.getLogger(RetroactiveDisplay.class);
    protected KeyboardMenuInputComponent inputComponent;
    protected Table buttonTable;
    protected Button button;
    protected int buttonIndex = 0;
    protected int[] traverseBackwards = new int[0];
    protected int[] traverseForwards = new int[0];
    protected int[] enter = new int[0];

    public RetroactiveDisplay() {
        super();
        renderPriority = RenderPriority.DISPLAY.ordinal();
    }

    @Override
    public void create() {
        super.create();
        inputComponent = entity.getComponent(KeyboardMenuInputComponent.class);
        entity.getEvents().addListener("key_down", this::onPreKeyDown);
        entity.getEvents().addListener("key_up", this::onPreKeyUp);
        table.setTouchable(Touchable.disabled);
        hide();
    }

    protected abstract Table createButtons();

    private void onPreKeyDown(int keyCode) {
        if (table.isVisible()) {
            keyDown(keyCode);
        }
    }

    private void onPreKeyUp(int keyCode) {
        if (table.isVisible()) {
            keyUp(keyCode);
        }
    }

    protected void keyDown(int keycode) {
        if (button != null) {
            return;
        }

        if (Arrays.stream(traverseBackwards).anyMatch(i -> i == keycode)) {
            entity.getEvents().trigger("play_sound", "browse");
            traverseButtons(-1);
        } else if (Arrays.stream(traverseForwards).anyMatch(i -> i == keycode)) {
            entity.getEvents().trigger("play_sound", "browse");
            traverseButtons(1);
        } else if (Arrays.stream(enter).anyMatch(i -> i == keycode)) {
            entity.getEvents().trigger("play_sound", "confirm");
            triggerTouchDown();
        }
    }

    protected void keyUp(int keycode) {
        if (Arrays.stream(enter).anyMatch(i -> i == keycode)) {
            triggerTouchUp();
        }
    }

    protected void traverseButtons(int direction) {
        if (button != null) {
            triggerUnhighlight();
            button = null;
        }

        if ((direction < 0 && buttonIndex == 0) ||
            (direction > 0 && buttonIndex == buttonTable.getChildren().size - 1)) {
            direction = 0;
        }

        if (direction != 0) {
            triggerUnhighlight();
            buttonIndex = (buttonIndex + direction) % buttonTable.getChildren().size;
            triggerHighlight();
        }
    }

    protected void triggerHighlight() {
        InputEvent highlight = new InputEvent();
        highlight.setType(InputEvent.Type.enter);
        highlight.setPointer(-1);

        buttonTable.getChild(buttonIndex).fire(highlight);
    }

    protected void triggerUnhighlight() {
        InputEvent unhighlight = new InputEvent();
        unhighlight.setType(InputEvent.Type.exit);
        unhighlight.setPointer(-1);

        buttonTable.getChild(buttonIndex).fire(unhighlight);
    }

    protected void triggerTouchDown() {
        InputEvent touchDown = new InputEvent();
        touchDown.setType(InputEvent.Type.touchDown);
        touchDown.setButton(0);
        touchDown.setRelatedActor(button);

        button = (Button) buttonTable.getChild(buttonIndex);
        button.fire(touchDown);
    }

    protected void triggerTouchUp() {
        InputEvent touchUp = new InputEvent();
        touchUp.setType(InputEvent.Type.touchUp);
        touchUp.setButton(0);
        touchUp.setRelatedActor(button);

        if (button != null) {
            button.fire(touchUp);
            button = null;
        } else {
            buttonTable.getChild(buttonIndex).fire(touchUp);
        }
    }

    @Override
    public void hide() {
        super.hide();
        inputComponent.setMenuInUse(false);
    }

    @Override
    public void show() {
        super.show();
        traverseButtons(-buttonIndex);
        inputComponent.setMenuInUse(true);
    }

    @Override
    public void loadAssets() {
    }

    @Override
    public void unloadAssets() {
    }
}