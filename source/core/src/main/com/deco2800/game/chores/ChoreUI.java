package com.deco2800.game.chores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.rendering.components.RenderPriority;
import com.deco2800.game.screens.RetroactiveWidget;

import java.util.List;

/**
 * Creates a list of chores that updates when chores are completed, and can be toggled
 * on/ off with the 'o' key.
 */
public class ChoreUI extends RetroactiveWidget {
    private boolean displaying;
    private Label displayText;
    private int entityCount;

    public ChoreUI() {
        super();
        renderPriority = RenderPriority.WIDGET.ordinal() - 0.06f;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("toggle_chores", this::toggleDisplay);
        displaying = ServiceLocator.getChoreController().getLevel() != 1;
        table.top().right().pad(30f);

        int rowHeight = Gdx.graphics.getHeight() / 16;
        int colWidth = Gdx.graphics.getWidth() / 10;

        displayText = new Label("", skin, "large");
        displayText.setSize(colWidth * 4f, rowHeight * 6f);
        displayText.setFontScale((colWidth * 10f) / 1280f); // Scale font to screen size
        displayText.setAlignment(Align.topRight);
        displayText.setWrap(true);
        table.add(displayText);
    }

    private void toggleDisplay() {
        if (displaying) {
            displaying = false;
            hide();
        } else {
            displaying = true;
            show();
        }
    }

    @Override
    public void update() {
        if (ServiceLocator.getChoreController().getEntityCount() != entityCount) {
            updateText();
        }
    }

    private void updateText() {
        displayText.setText("");

        // Get the list of chores from the ChoreController
        List<Chore> chores = ServiceLocator.getChoreController().getChores();
        entityCount = ServiceLocator.getChoreController().getEntityCount();

        // Format the output text
        StringBuilder choreText = new StringBuilder();
        if (chores.size() != 0) {
            choreText.append("Things I need to do:\n");
            for (Chore chore : chores) {
                choreText.append(chore.getDescription()).append("\n");
            }
        } else {
            choreText.append("Chores complete, get to bed!");
        }

        displayText.setText(choreText);
    }

    @Override
    public void loadAssets() {
        logger.debug("    Loading chore widget assets");
        super.loadAssets();
    }

    @Override
    public void unloadAssets() {
        logger.debug("    Unloading chore widget assets");
        super.unloadAssets();
    }
}