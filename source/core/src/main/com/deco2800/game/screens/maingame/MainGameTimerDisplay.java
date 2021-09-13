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
    Table timeTable;
    private final Label timerLabel;
    private final Label currentTimeLabel;
    private static int timeLeft;
    private long lastTime = 0L;
    private int hour;
    private int minute;

    public MainGameTimerDisplay(int initialTime) {
        timeLeft = initialTime;
        hour = 20;
        minute = 0;
        CharSequence text = String.format("Time left: %ds", timeLeft);
        CharSequence timeText = String.format("%d : 0%d",hour, minute);
        timerLabel = new Label(text, skin, "large");
        currentTimeLabel = new Label(timeText, skin, "large");

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
        timeTable = new Table();
        timeTable.top().right().padTop(10f).padRight(120f);
        table.setFillParent(true);
        timeTable.setFillParent(true);

        table.add(timerLabel);
        timeTable.add(currentTimeLabel);
        stage.addActor(table);
        stage.addActor(timeTable);
    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    /**
     * Updates the player's time left on the ui.
     */
    public void updatePlayerTimerUI() {
            CharSequence text = String.format("Time left: %ds", timeLeft);
            timerLabel.setText(text);
    }

    public void updateTimeUI() {
        if (minute < 59) {
            minute += 1;
        } else if (minute == 59) {
            if (hour < 23) {
                hour ++;
            } else if (hour == 23) {
                hour = 0;
            }
            minute = 0;
        }
        CharSequence timeText;
        if (hour < 10) {
            if (minute < 10) {
                timeText = String.format("0%d : 0%d",hour, minute);
            }
            else {
                timeText = String.format("0%d : %d",hour, minute);
            }
        } else {
            if (minute < 10) {
                timeText = String.format("%d : 0%d",hour, minute);
            }
            else {
                timeText = String.format("%d : %d",hour, minute);
            }
        }

        currentTimeLabel.setText(timeText);
        System.out.println(timeText);
    }

    @Override
    public void dispose() {
        table.clear();
        timeTable.clear();
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
        if (currentTime - lastTime >= 600L) {
            lastTime = currentTime;
//            tick();
//            updatePlayerTimerUI();
            updateTimeUI();
            if (hour == 2 && minute > 0) {
                entity.getEvents().trigger("loss_timed");
            }
        }
        }
}