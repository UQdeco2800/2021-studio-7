package com.deco2800.game.chores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Creates a toggle-able and variable text box display at the bottom of the screen.
 *
 * By default, will not display anything, but can call MainGameTextDisplay.display to display a
 * box with text (and optional image). Can then call remove to remove it.
 */
public class ChoreUI extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ChoreUI.class);
    private boolean displaying = false;
    private Label displayText;

    @Override
    public void create() {
        super.create();

        // Divide screen into a more manageable grid
        int rowHeight = Gdx.graphics.getHeight() / 16;
        int colWidth = Gdx.graphics.getWidth() / 10;

        // Display Text
        displayText = new Label("", skin, "large");
        displayText.setSize(colWidth*6f, rowHeight*6f);
        displayText.setPosition(colWidth/12f, rowHeight*6f);
        displayText.setFontScale((colWidth*10f)/1280f); // Scale font to screen size
        displayText.setAlignment(Align.topLeft);
        displayText.setWrap(true);

        stage.addActor(displayText);

        entity.getEvents().addListener("toggle_chores", this::toggleDisplay);
    }

    /**
     * Toggle whether the chore list is being displayed or not
     */
    private void toggleDisplay() {
        if (displaying) {
            hide();
        } else {
            display();
        }
    }

    /**
     * Displays the list of chores to the screen.
     */
    public void display() {
        // Get the list of chores from the ChoreController
        ArrayList<Chore> chores = ServiceLocator.getChoreController().getChores();
        String[] choreDescriptions = new String[chores.size()];
        for (int i = 0; i < chores.size(); i++) {
            choreDescriptions[i] = chores.get(i).getDescription();
        }

        // Format the output text
        StringBuilder choreText = new StringBuilder();
        choreText.append("Things I need to do:\n");
        for (String choreDescription : choreDescriptions) {
            choreText.append(choreDescription).append("\n");
        }

        displayText.setText(choreText);
        displaying = true;
    }

    /**
     * Removes all current visual components from the screen (but doesn't do a full cleanup)
     */
    private void hide() {
        displayText.setText("");
        displaying = false;
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}