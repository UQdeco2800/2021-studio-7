package com.deco2800.game.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.rendering.components.RenderPriority;
import com.deco2800.game.screens.RetroactiveWidget;

/**
 * Creates a toggle-able and variable text box display at the bottom of the screen.
 */
public class PromptWidget extends RetroactiveWidget {
    private static final String PROMPT_TEXTURE = "images/ui/elements/Textbox_1024.png";
    private static final long PROMPT_DURATION = 3000L;
    private static final long NORMAL_TICK_RATE = 10L;
    private long lastTime = 0L;
    private Label prompt;
    private long startTime;
    private String text;
    private String currentText = "";
    private int index = 0;

    public PromptWidget() {
        super();
        renderPriority = RenderPriority.WIDGET.ordinal() - 0.10f;
    }

    @Override
    public void create() {
        super.create();
        table.setSize(stage.getWidth() * 0.90f, stage.getHeight() * 0.25f);
        table.setPosition(stage.getWidth() * 0.05f, stage.getHeight() * 0.05f);
        table.setFillParent(false);

        entity.getEvents().addListener("create_textbox", this:: display);

        Image background = new Image(ServiceLocator.getResourceService().getAsset(PROMPT_TEXTURE, Texture.class));
        table.setBackground(background.getDrawable());

        prompt = new Label("", skin, "large");
        prompt.setFontScale(stage.getWidth() * 0.001f);
        prompt.setAlignment(Align.topLeft);
        prompt.setWrap(true);
        table.add(prompt).top().left().grow()
            .pad(table.getHeight() * 0.24f, table.getWidth() * 0.08f, table.getHeight() * 0.24f, table.getWidth() * 0.08f);

        hide();
    }

    public void display(String text) {
        if (enabled) {
            hide();
        }
        this.text = text;
        entity.getEvents().trigger("play_sound", "confirm");
        show();
    }

    @Override
    public void update() {
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (currentTime - lastTime >= NORMAL_TICK_RATE && index < text.length()) {
            lastTime = currentTime;
            currentText += text.charAt(index);
            prompt.setText(currentText);
            index += 1;
        }

        if (currentTime - startTime >= PROMPT_DURATION) {
            hide();
        }
    }

    @Override
    public void hide() {
        super.hide();
        prompt.setText("");
        text = "";
        currentText = "";
        index = 0;
    }

    @Override
    public void show() {
        super.show();
        startTime = ServiceLocator.getTimeSource().getTime();
    }

    @Override
    public void loadAssets() {
        logger.debug("    Loading prompt widget assets");
        super.loadAssets();
        ServiceLocator.getResourceService().loadAsset(PROMPT_TEXTURE, Texture.class);
    }

    @Override
    public void unloadAssets() {
        logger.debug("    Unloading prompt widget assets");
        super.unloadAssets();
        ServiceLocator.getResourceService().unloadAsset(PROMPT_TEXTURE);
    }
}