package com.deco2800.game.screens.maingame;

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
    private static final Logger logger = LoggerFactory.getLogger(MainGameTimerDisplay.class);
    private static final String TIMER_BACKGROUND = "images/ui/elements/Textbox_256.png";
    private static final float TIMER_START = 23.00f;
    private static final float TIMER_END = 3.00f;
    private static final long TIMER_TICK_RATE = 750L;
    private Table table;
    private Label timerLabel;
    private TimerStatus timerStatus;
    private float timerTime;
    private long lastTime = 0L;


    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        timerTime = TIMER_START;
        timerStatus = TimerStatus.NORMAL;
        addActors();
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * @see Table for positioning options
     */
    public void addActors() {
        table = new Table(skin);
        table.setFillParent(true);
        table.bottom().right().padRight(60f);

        // Set background to the appropriate texture for the timer display
        ServiceLocator.getResourceService().loadTexture(TIMER_BACKGROUND);
        ServiceLocator.getResourceService().loadAll();
        Image timerBackground =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset(TIMER_BACKGROUND, Texture.class));
        table.setBackground(timerBackground.getDrawable());

        // Add timer label to the table
        timerLabel = new Label(getCurrentTime(), skin);
        table.add(timerLabel);

        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    public CharSequence getCurrentTime() {
        return String.format("%02d%02d", getHours(), getMinutes());
    }

    public int getHours() {
        return (int) timerTime;
    }

    public int getMinutes() {
        return (int) (timerTime % 1);
    }

    public void tick() {
        if (getMinutes() < 59) {
            timerTime += 0.01f;
        } else {
            timerTime += 0.41f;
            if (getHours() > 23) {
                timerTime -= (int) timerTime;
            }
        }
    }

    public void updateLabel() {
        timerLabel.setText(getCurrentTime());

        if (timerStatus == TimerStatus.NORMAL && TIMER_END - timerTime <= 1) {
            timerLabel.setColor(255, 0,0, 1f);
            timerLabel.addAction(Actions.alpha(0));
            timerLabel.addAction(Actions.forever(Actions.sequence(
                    Actions.fadeIn(1f),
                    Actions.fadeOut(1f))));
            timerStatus = TimerStatus.FLASHING;
        }
    }

    public void checkTimerEnd() {
        if (timerTime >= TIMER_END) {
            logger.debug("Timer has finished");
            entity.getEvents().trigger("timer_ended");
        }
    }
    
    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }

    /**
     * Main function for timer, it would update time left
     * and stop when time reach 2 pm and trigger time loss event
     */
    @Override
    public void update() {
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (currentTime - lastTime >= TIMER_TICK_RATE) {
            lastTime = currentTime;
            tick();
            updateLabel();
            checkTimerEnd();
        }
    }
    
    enum TimerStatus {
        NORMAL, FLASHING
    }
}