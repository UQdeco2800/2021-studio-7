package com.deco2800.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.ui.UIComponent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class MainGameTimerTestingDisplay extends UIComponent {
    Table table;
    private Label timerLabel;
    private static Timer timer;
    private static int interval;

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
        table.bottom().left();
        table.setFillParent(true);
        table.padBottom(1f).padLeft(5f);


        // Timer text
        CharSequence Timer_text = String.format("Time left: %ds", interval);
        timerLabel = new Label(Timer_text, skin, "large");

        table.add(timerLabel);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    /**
     * Updates the player's time left on the ui.
     * @param time player time left
     */
    public void updatePlayerHealthUI(int time) {
        CharSequence text = String.format("Time left: %ds", time);
        timerLabel.setText(text);
    }

    @Override
    public void dispose() {
        super.dispose();
        timerLabel.remove();
    }

    /**
     * Main function for timer, it would update time left
     * and stop when time left equals to zero
     */
    public void countDown() {
        timer = new Timer();
        int delay = 1000;
        int period = 1000;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                updatePlayerHealthUI(setInterval());
            }
        }, delay, period);
    }
    private static int setInterval() {
        if (interval == 1)
            timer.cancel();
        return --interval;
    }

    public void setTimer(int time) {
        interval = time;
    }
}
