package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class MainGameTimerDisplay extends UIComponent {
    Table table;
    private Label timerLabel;
    private static int timeLeft;
    private long lastTime = 0L;

    public MainGameTimerDisplay(int initialTime) {
        timeLeft = initialTime;
        CharSequence text = String.format("Time left: %ds", timeLeft);
        timerLabel = new Label(text, skin, "large");
    }

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * @see Table for positioning options
     */
    public void addActors() {
        table = new Table();
        table.bottom().left().padBottom(10f).padLeft(5f);
        table.setFillParent(true);

        table.add(timerLabel);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    /**
     * Updates the player's time left on the ui.
     */
    public void updatePlayerHealthUI() {
            CharSequence text = String.format("Time left: %ds", timeLeft);
            timerLabel.setText(text);
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }

    public static void tick() {
        timeLeft = timeLeft - 1;
    }

    /**
     * Main function for timer, it would update time left
     * and stop when time left equals to zero and trigger time loss event
     */
    @Override
    public void update() {
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (currentTime - lastTime >= 1000L) {
            lastTime = currentTime;
            tick();
            updatePlayerHealthUI();
            if (timeLeft < 0) {
                entity.getEvents().trigger("loss_timed");
            }
            }
        }
}