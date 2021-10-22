package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.ui.components.UIComponent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A ui component for displaying player score.
 */
public class MainGameScoreDisplay extends UIComponent {
    Table table;
    private Label scoreLabel;
    private static Timer timer;
    private int timeLeft;
    private int score;
    private int timeSinceStart;

    public MainGameScoreDisplay(int initialTime, int initialscore) {
        this.timeLeft = initialTime;
        this.score = initialscore;
        timeSinceStart = 0;
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
        table.bottom().left().padBottom(60f).padLeft(5f);
        table.setFillParent(true);

        scoreLabel = new Label(
                String.format("Score: %d", score),
                skin, "large");

        table.add(scoreLabel);
        stage.addActor(table);

    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    /**
     * Updates the player's core on the ui.
     */
    public void updatePlayerHealthUI() {
        CharSequence text = String.format("Score: %d", getscore());
        scoreLabel.setText(text);
    }

    /**
     *
     * @return score as an int
     */
    public int getscore(){
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

}