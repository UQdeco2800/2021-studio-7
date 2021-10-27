package com.deco2800.game.screens.game;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.screens.RetroactiveWidget;

import java.util.Timer;
import java.util.TimerTask;

/**
 * UI component for displaying player score.
 */
public class ScoreWidget extends RetroactiveWidget {
    Table table;
    private Label scoreLabel;
    private static Timer timer;
    private int timeLeft;
    private int score;
    private int timeSinceStart;

    public ScoreWidget(int initialTime, int initialScore) {
        super();
        this.timeLeft = initialTime;
        this.score = initialScore;
        timeSinceStart = 0;
    }

    @Override
    protected void addActors() {
        table = new Table();
        table.bottom().left().padBottom(60f).padLeft(5f);
        table.setFillParent(true);

        scoreLabel = new Label(
            String.format("Score: %d", score),
            skin, "large");

        table.add(scoreLabel);
        stage.addActor(table);

    }

    /**
     * Updates the player's core on the ui.
     */
    public void updatePlayerHealthUI() {
        CharSequence text = String.format("Score: %d", getscore());
        scoreLabel.setText(text);
    }

    /**
     * @return score as an int
     */
    public int getscore() {
        this.score -= 1;
        return this.score;
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
        makeTimer();
        int delay = 100;
        int period = 30;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                tick();
                updatePlayerHealthUI();
            }
        }, delay, period);
    }

    private static void makeTimer() {
        timer = new Timer();
    }

    private void tick() {
        this.timeLeft--;
        this.timeSinceStart++;
    }

    @Override
    public void loadAssets() {
        logger.debug("    Loading score widget assets");
        super.loadAssets();
    }

    @Override
    public void unloadAssets() {
        logger.debug("    Unloading score widget assets");
        super.unloadAssets();
    }
}