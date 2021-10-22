package com.deco2800.game.chores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;


import java.util.List;

/**
 * Creates a list of chores that updates when chores are completed, and can be toggled
 * on/ off with the 'o' key.
 */
public class ChoreUI extends UIComponent {
    private boolean displaying;
    private Label displayText;
    private int entityCount;

    @Override
    public void create() {
        super.create();
        displaying = ServiceLocator.getChoreController().getLevel() != 1;

        // Divide screen into a more manageable grid
        int rowHeight = Gdx.graphics.getHeight() / 16;
        int colWidth = Gdx.graphics.getWidth() / 10;

        // Display Text
        displayText = new Label("", skin, "large");
        displayText.setSize(colWidth*4f, rowHeight*6f);
        displayText.setFontScale((colWidth*10f)/1280f); // Scale font to screen size
        displayText.setAlignment(Align.topRight);
        displayText.setWrap(true);

        Table table = new Table();
        table.top().right();
        table.pad(30f);
        table.setFillParent(true);
        table.add(displayText);

        stage.addActor(table);

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
        // Clear the current display (if displaying)
        displayText.setText("");

        // Get the list of chores from the ChoreController
        List<Chore> chores = ServiceLocator.getChoreController().getChores();
        entityCount = ServiceLocator.getChoreController().getEntityCount();
        String[] choreDescriptions = new String[chores.size()];
        for (int i = 0; i < chores.size(); i++) {
            choreDescriptions[i] = chores.get(i).getDescription();
        }

        // Format the output text
        StringBuilder choreText = new StringBuilder();
        if (choreDescriptions.length != 0) {
            choreText.append("Things I need to do:\n");
            for (String choreDescription : choreDescriptions) {
                choreText.append(choreDescription).append("\n");
            }
        } else {
            choreText.append("Chores complete, get to bed!");
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
    public void update() {
        // Update the display when the number of chore entities completed changes
        if (displaying && ServiceLocator.getChoreController().getEntityCount() != entityCount) {
            this.display();
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        super.dispose();
        hide();
    }
}