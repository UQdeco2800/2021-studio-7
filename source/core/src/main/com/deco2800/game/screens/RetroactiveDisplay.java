package com.deco2800.game.screens;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.deco2800.game.generic.Loadable;
import com.deco2800.game.rendering.components.RenderPriority;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RetroactiveDisplay extends UIComponent implements Loadable {
    protected static final Logger logger = LoggerFactory.getLogger(RetroactiveDisplay.class);

    public RetroactiveDisplay() {
        super();
        renderPriority = RenderPriority.DISPLAY.ordinal();
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("key_down", this::onPreKeyDown);
        hide();
    }

    private void onPreKeyDown(int keyCode) {
        if (table.isVisible()) {
            keyDown(keyCode);
        }
    }

    protected abstract void keyDown(int keycode);

    protected int changeSelectedButton(Group container, int index, int direction) {
        if ((direction < 0 && index == 0) ||
            (direction > 0 && index == container.getChildren().size - 1)) {
            direction = 0;
        }

        if (direction != 0) {
            container.getChild(index).fire(getUnhighlightEvent());
            index = (index + direction) % container.getChildren().size;
            container.getChild(index).fire(getHighlightEvent());
        }

        return index;
    }

    protected InputEvent getHighlightEvent() {
        InputEvent highlight = new InputEvent();
        highlight.setType(InputEvent.Type.exit);
        highlight.setPointer(-1);
        return highlight;
    }

    protected InputEvent getUnhighlightEvent() {
        InputEvent unhighlight = new InputEvent();
        unhighlight.setType(InputEvent.Type.enter);
        unhighlight.setPointer(-1);
        return unhighlight;
    }

    @Override
    public void loadAssets() {
    }

    @Override
    public void unloadAssets() {
    }
}