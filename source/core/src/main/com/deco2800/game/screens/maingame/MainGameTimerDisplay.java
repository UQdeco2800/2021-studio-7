package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.ui.components.UIComponent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class MainGameTimerDisplay extends UIComponent {
    Table table;
    private Label timerLabel;
    private static Timer timer;
    private static int timeLeft;
    private static int timeSinceStart;

    public MainGameTimerDisplay(int initialTime) {
        timeLeft = initialTime;
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

        timerLabel = new Label(
                String.format("Time left: %ds", timeLeft),
                skin, "large");

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
        if (timeLeft <= 0) {
            // Should trigger loss_timed event in MainGameActions
            // I think it causes a runtime error because this method is
            // called on the TimerTask thread, and not the main thread.
            // Perhaps @XUEHUANG521 should utilise the time source in
            // the engine instead of a Timer object (@Jantoom)
            //entity.getEvents().trigger("loss_timed");
            timer.cancel();
        }
    }

    @Override
    public void dispose() {
        table.clear();
        timer.cancel();
        super.dispose();
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
                tick();
                updatePlayerHealthUI();
            }
        }, delay, period);
    }

    private static void tick() {
        timeLeft--;
        timeSinceStart++;
    }
}