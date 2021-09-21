package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A ui component for displaying player stats, e.g. health.
 */
public class MainGameTimerDisplay extends UIComponent {
    Table timeTable;
    private final Label currentTimeLabel;
    private long lastTime = 0L;
    private int hour;
    private int minute;
    private static final Logger logger =
            LoggerFactory.getLogger(MainGameTimerDisplay.class);

    public MainGameTimerDisplay() {
        logger.debug("Initialising main game screen timer service");
        hour = 20;
        minute = 0;
        CharSequence timeText = String.format("%d : 0%d",hour, minute);
        currentTimeLabel = new Label(timeText, skin, "large");
        logger.debug("Main game screen timer service started");

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
        timeTable = new Table();
        timeTable.top().right().padTop(10f).padRight(120f);
        timeTable.setFillParent(true);
        timeTable.add(currentTimeLabel);
        stage.addActor(timeTable);
    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    /**
     * Updates the main game screen clock
     */
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
    }

    @Override
    public void dispose() {
        logger.debug("Disposing timer");
        timeTable.clear();
        super.dispose();
    }

    /**
     * Main function for timer, it would update time left
     * and stop when time reach 2 pm and trigger time loss event
     */
    @Override
    public void update() {
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (currentTime - lastTime >= 600L) {
            lastTime = currentTime;
            updateTimeUI();
            if (hour == 2 && minute > 0) {
                logger.info("Time end");
                entity.getEvents().trigger("loss_timed");
            }
        }
        }
}