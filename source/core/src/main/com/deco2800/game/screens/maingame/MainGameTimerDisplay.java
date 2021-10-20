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
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private static final Logger logger =
            LoggerFactory.getLogger(MainGameTimerDisplay.class);
    private Texture texture = new Texture(Gdx.files.internal("images/ui" +
            "/elements/Textbox_256.png"));
    private Image background = new Image(texture);

    public MainGameTimerDisplay() {
        logger.debug("Initialising main game screen timer service");

        //set up initial timer for the level 1
        setTimer(23, 0,  2, 0);
        CharSequence timeText = String.format("    %d:0%d",startHour,
                startMinute);
        currentTimeLabel = new Label(timeText, skin, "title");
        currentTimeLabel.setText(timeText);
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
        timeTable.bottom().right();
        timeTable.padRight(60f);
        timeTable.setFillParent(true);
        timeTable.add(background);
        timeTable.stack(background, currentTimeLabel);
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
        setStartHour(hr1);
        setStartMinute(min1);
        setEndHour(hr2);
        setEndMinute(min2);
        logger.info("Timer setup ready");
    }

    /**
     * Set up start hour
     */

    public void setStartHour(int hr) {
        this.startHour = hr;
    }

    /**
     * Get start hour now
     */
    public int getStartHour() {
        return this.startHour;
    }

    /**
     * Set up start minute
     */
    public void setStartMinute(int min) {
        this.startMinute = min;
    }
    /**
     * Get start minute now
     */

    public int getStartMinute() {
        return this.startMinute;
    }

    /**
     * Set up end hour
     */

    public void setEndHour(int hr) {
        this.endHour = hr;
    }

    /**
     * Get end hour
     */
    public int getEndHour() {
        return this.endHour;
    }

    /**
     * Set up end minute
     */
    public void setEndMinute(int min) {
        this.endMinute = min;
    }

    /**
     * Get end minute now
     */
    public int getEndMinute() {
        return this.endMinute;
    }
    /**
     * Updates the main game screen clock
     */
    public void updateTimeUI() {
        if (this.getStartMinute() < 59) {
            this.setStartMinute(this.getStartMinute() + 1);
        } else if (this.getStartMinute() == 59) {
            if (this.getStartHour() < 23) {
                this.setStartHour(this.getStartHour() + 1);
            } else if (this.getStartHour() == 23) {
                this.setStartHour(0);
            }
            this.setStartMinute(0);
        }
        CharSequence timeText;
        if (this.getStartHour() < 10) {
            if (this.getStartMinute() < 10) {
                timeText = String.format("    0%d:0%d",this.getStartHour(),
                        this.getStartMinute());
            }
            else {
                timeText = String.format("    0%d:%d",this.getStartHour(),
                        this.getStartMinute());
            }
        } else {
            if (this.getStartMinute() < 10) {
                timeText = String.format("    %d:0%d",this.getStartHour(),
                        this.getStartMinute());
            }
            else {
                timeText = String.format("    %d:%d",this.getStartHour(),
                        this.getStartMinute());
            }
        }
        currentTimeLabel.setText(timeText);
        if((Math.abs(getStartHour()-getEndHour()) <= 1 || Math.abs(getStartHour()-getEndHour()) >= 23) && Math.abs(getEndMinute() - getStartMinute()) > 30) {
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
            if (this.getStartHour() == this.getEndHour() && this.getStartMinute() > this.getEndMinute()) {
                logger.info("Time end");
                entity.getEvents().trigger("timer_ended");
            }
        }
    }
}