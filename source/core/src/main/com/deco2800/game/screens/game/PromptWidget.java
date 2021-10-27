package com.deco2800.game.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.generic.ServiceLocator;
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

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("create_textbox", this::display);
        hide();
    }

    @Override
    protected void addActors() {
        table = new Table();
        table.center().bottom();

        int rowHeight = Gdx.graphics.getHeight() / 16;
        int colWidth = Gdx.graphics.getWidth() / 10;

        //table.setSize(Gdx.graphics.getWidth(), rowHeight * 4f);
        Image background = new Image(ServiceLocator.getResourceService().getAsset(PROMPT_TEXTURE, Texture.class));
        //background.setScaleX((colWidth * 8) / background.getWidth());
        //background.setOrigin(Align.center);
        table.setBackground(background.getDrawable());

        prompt = new Label("", skin, "large");
        prompt.setSize(colWidth * 6f, rowHeight * 3f);
        //prompt.setPosition(colWidth * 2f, rowHeight / 2f);
        prompt.setFontScale((colWidth * 10f) / 1280f); // Scale font to screen size
        prompt.setWrap(true);
        table.add(prompt);
    }

    public void display(String text) {
        this.text = text;
        if (enabled) {
            hide();
        }
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
        enabled = false;
        prompt.setText("");
        text = "";
        currentText = "";
        index = 0;
    }

    @Override
    public void show() {
        super.show();
        enabled = true;
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