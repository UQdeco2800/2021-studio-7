package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    Label displayText;
    private long lastTime = 0L;
    private int start_hour;
    private int start_minute;
    private int end_hour;
    private int end_minute;
    private static final Logger logger =
            LoggerFactory.getLogger(MainGameTimerDisplay.class);
    private Texture texture = new Texture(Gdx.files.internal("images/ui" +
            "/elements/Textbox_256.png"));
    private Image background = new Image(texture);

    public MainGameTimerDisplay() {
        logger.debug("Initialising main game screen timer service");
        setStart_hour(23);
        setStart_minute(0);
        setEnd_minute(0);
        setEnd_hour(2);
CharSequence timeText = String.format("    %d:0%d",start_hour,
        start_minute);
        currentTimeLabel = new Label(timeText, skin, "title");
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
        timeTable.add(currentTimeLabel);
        timeTable.add(background);
        timeTable.stack(background, currentTimeLabel);
        stage.addActor(timeTable);
    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    public void setStart_hour(int hr) {
        this.start_hour = hr;
    }

    public int getStart_hour() {
        return this.start_hour;
    }

    public void setStart_minute(int min) {
        this.start_minute = min;
    }
    public int getStart_minute() {
        return this.start_minute;
    }
    public void setEnd_hour(int hr) {
        this.end_hour = hr;
    }

    public int getEnd_hour() {
        return this.end_hour;
    }

    public void setEnd_minute(int min) {
        this.end_minute = min;
    }

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
            if (this.getStart_hour() == this.getEnd_hour() && this.getStart_minute() > this.getEnd_minute()) {
                logger.info("Time end");
                entity.getEvents().trigger("loss_timed");
            }
        }
        }
}