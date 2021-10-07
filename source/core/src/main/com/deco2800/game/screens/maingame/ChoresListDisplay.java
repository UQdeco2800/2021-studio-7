package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import com.deco2800.game.events.listeners.EventListener1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a toggle-able and variable text box display at the bottom of the screen.
 *
 * By default, will not display anything, but can call MainGameTextDisplay.display to display a
 * box with text (and optional image). Can then call remove to remove it.
 */
public class ChoresListDisplay extends UIComponent {

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("create_chores_list", this::display);
    }

    /**
     * Displays the text box at the bottom of the screen containing the given text.
     *
     * @param text The text to display
     */
    public void display(String text) {
        // Divide screen into a more manageable grid
        int rowHeight = Gdx.graphics.getHeight() / 16;
        int colWidth = Gdx.graphics.getWidth() / 10;

        // Display Text
        Label displayText = new Label("", skin, "large");
        displayText.setSize(colWidth*6, rowHeight*3);
        displayText.setPosition((float) colWidth/12, (float) rowHeight*10);
        displayText.setFontScale((float) (colWidth*10)/1280); // Scale font to screen size
        displayText.setWrap(true);

        stage.addActor(displayText);
    }

    /**
     * Removes the textbox after a set amount of time (DURATION)
     */
    @Override
    public void update() {}

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}