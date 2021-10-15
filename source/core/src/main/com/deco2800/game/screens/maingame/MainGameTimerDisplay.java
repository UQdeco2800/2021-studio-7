package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    private int start_hour;
    private int start_minute;
    private int end_hour;
    private int end_minute;
    private static final Logger logger =
            LoggerFactory.getLogger(MainGameTimerDisplay.class);
//    private Texture texture = new Texture(Gdx.files.internal("images/ui" +
//            "/elements/Textbox_256.png"));
//    private Image background = new Image(texture);

    public MainGameTimerDisplay() {
        logger.debug("Initialising main game screen timer service");

        //set up initial timer for the level 1
        setTimer(23, 0,  1, 0);
        CharSequence timeText = String.format("    %d:0%d",start_hour,
        start_minute);
        currentTimeLabel = new Label(timeText, skin, "title");
        currentTimeLabel.setText(timeText);
        currentTimeLabel.setFontScale(2f);
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
        timeTable.top().right();
        timeTable.padRight(500f).padTop(10f);
        timeTable.setFillParent(true);
//        timeTable.add(background);
//        timeTable.stack(background, currentTimeLabel);
        timeTable.add(currentTimeLabel);
        stage.addActor(timeTable);
    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    /**
     * Provide an interface to set up main game screen clock time
     * @param hr1 Start hour time for this level's game
     * @param hr2 End time for this level's game
     * @param min1 Start min for this level's game
     * @param min2 End min for this level's game
     */
    public void setTimer(int hr1, int min1, int hr2, int min2) {
        setStart_hour(hr1);
        setStart_minute(min1);
        setEnd_hour(hr2);
        setEnd_minute(min2);
        logger.info("Timer setup ready");
    }

    /**
     * Set up start hour
     */

    public void setStart_hour(int hr) {
        this.start_hour = hr;
    }

    /**
     * Get start hour now
     */
    public int getStart_hour() {
        return this.start_hour;
    }

    /**
     * Set up start minute
     */
    public void setStart_minute(int min) {
        this.start_minute = min;
    }
    /**
     * Get start minute now
     */

    public int getStart_minute() {
        return this.start_minute;
    }

    /**
     * Set up end hour
     */

    public void setEnd_hour(int hr) {
        this.end_hour = hr;
    }

    /**
     * Get end hour
     */
    public int getEnd_hour() {
        return this.end_hour;
    }

    /**
     * Set up end minute
     */
    public void setEnd_minute(int min) {
        this.end_minute = min;
    }

    /**
     * Get end minute now
     */
    public int getEnd_minute() {
        return this.end_minute;
    }
    /**
     * Updates the main game screen clock
     */
    public void updateTimeUI() {
        if (this.getStart_minute() < 59) {
            this.setStart_minute(this.getStart_minute() + 1);
        } else if (this.getStart_minute() == 59) {
            if (this.getStart_hour() < 23) {
                this.setStart_hour(this.getStart_hour() + 1);
            } else if (this.getStart_hour() == 23) {
                this.setStart_hour(0);
            }
            this.setStart_minute(0);
        }
        CharSequence timeText;
        if (this.getStart_hour() < 10) {
            if (this.getStart_minute() < 10) {
                timeText = String.format("    0%d:0%d",this.getStart_hour(),
                        this.getStart_minute());
            }
            else {
                timeText = String.format("    0%d:%d",this.getStart_hour(),
                        this.getStart_minute());
            }
        } else {
            if (this.getStart_minute() < 10) {
                timeText = String.format("    %d:0%d",this.getStart_hour(),
                        this.getStart_minute());
            }
            else {
                timeText = String.format("    %d:%d",this.getStart_hour(),
                        this.getStart_minute());
            }
        }
        currentTimeLabel.setText(timeText);
        if((Math.abs(getStart_hour()-getEnd_hour()) <= 1 || Math.abs(getStart_hour()-getEnd_hour()) >= 23) && Math.abs(getEnd_minute() - getStart_minute()) > 30) {
            currentTimeLabel.setColor(255, 0,0, 1f);
            currentTimeLabel.addAction(Actions.alpha(0));
            currentTimeLabel.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(1f),
                    Actions.fadeOut(1f))));
        }
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
        if (currentTime - lastTime >= 750L) {
            lastTime = currentTime;
            updateTimeUI();
            if (this.getStart_hour() == this.getEnd_hour() && this.getStart_minute() > this.getEnd_minute()) {
                logger.info("Time end");
                entity.getEvents().trigger("timer_ended");
            }
        }
        }
}