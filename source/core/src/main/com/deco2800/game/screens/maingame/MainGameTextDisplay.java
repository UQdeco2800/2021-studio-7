package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.events.listeners.EventListener1;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;

/**
 * Creates a toggle-able and variable text box display at the bottom of the screen.
 *
 * On creation, will not display anything, but can call MainGameTextDisplay.display to display a
 * box with text (and optional image). Can then call remove to remove it.
 * TODO Decide whether to dispose after set time, or need to call to remove it.
 */
public class MainGameTextDisplay extends UIComponent {
    private Table table;
    private boolean visible;
    private long startTime;

    private long DURATION = 3000L; // The duration for which the textbox stays visible

    // TODO Put this stuff somewhere else probably


    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("create_textbox",
                (EventListener1<String>) this::display);
    }

    private void addActors() {
        table = new Table();
        table.bottom().padBottom(10f);
        table.setFillParent(true);
        stage.addActor(table);
    }

    /**
     * Displays a text box at the bottom of the screen containing the given text.
     *
     * @param text The text to display
     */
    public void display(String text) {
        // Background texture
        Texture texture = new Texture(Gdx.files.internal(
                "images/ui/elements/Textbox_1024.png"));
        Image image = new Image(texture);

        // Text
        Label displayText = new Label(text, skin, "large");
        displayText.setWrap(true);

        table.stack(image, displayText);

        visible = true;
        startTime = ServiceLocator.getTimeSource().getTime();
    }

    /**
     * Displays a text box at the bottom of the screen containing the given text and image.
     *
     * @param text The text to display
     * @param imagePath The path to the image to display
     */
    public void display(String text, String imagePath) {
        // Background texture
        Texture texture = new Texture(Gdx.files.internal(
                "images/ui/elements/Textbox_1024.png"));
        Image image = new Image(texture);

        // Text
        Label displayText = new Label(text, skin, "large");
        displayText.setWrap(true);

        // Image


        table.stack(image, displayText);

        visible = true;
        startTime = ServiceLocator.getTimeSource().getTime();
    }

    /**
     * Removes all current visual components from the screen (but doesn't do a full cleanup)
     */
    public void hide() {
        table.clear();
        visible = false;
    }

    /**
     * Removes the textbox after a set amount of time
     */
    @Override
    public void update() {
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (visible && currentTime - startTime >= DURATION) {
            hide();
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}
