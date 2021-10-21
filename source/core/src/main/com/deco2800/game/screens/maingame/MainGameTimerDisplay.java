package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Displays a timer in 24-hour time. Acts like an alarm; it will end the game once the desired time is reached.
 */
public class MainGameTimerDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameTimerDisplay.class);
    private static final String TIMER_BACKGROUND = "images/ui/elements/Textbox_256.png";
    private static final int TIMER_START = 2300;
    private static final int TIMER_END = 200;
    private static final long TIMER_TICK_RATE = 750L;
    private long lastTime = 0L;
    private Table table;
    private Label timerLabel;
    private TimerStatus timerStatus;
    private int timerTime;

    @Override
    public void create() {
        super.create();
        timerTime = TIMER_START;
        timerStatus = TimerStatus.NORMAL;
        addActors();
    }

    public void addActors() {
        table = new Table();
        table.setFillParent(true);
        table.top().padTop(30f);

        ServiceLocator.getResourceService().loadTexture(TIMER_BACKGROUND);
        ServiceLocator.getResourceService().loadAll();
        Image timerBackground =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset(TIMER_BACKGROUND, Texture.class));

        timerLabel = new Label(getCurrentTime(), skin, "title");
        timerLabel.setAlignment(Align.center);

        table.stack(timerBackground, timerLabel);

        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    public CharSequence getCurrentTime() {
        return String.format("%02d:%02d", getHours(), getMinutes());
    }

    public int getHours() {
        return timerTime / 100;
    }

    public int getMinutes() {
        return timerTime % 100;
    }

    public void tick() {
        if (getMinutes() < 59) {
            timerTime += 1;
        } else {
            timerTime += 41;
            if (getHours() > 23) {
                timerTime = getMinutes();
            }
        }
    }

    /**
     * Updates the timer label to the new time.
     *
     * If the timer is within an hour of the final time,
     * then the label will change colour and blink until the alarm goes off.
     */
    public void updateLabel() {
        timerLabel.setText(getCurrentTime());

        int timeUntilEnd = TIMER_END - timerTime;
        if (timerStatus == TimerStatus.NORMAL && timeUntilEnd > 0 && timeUntilEnd <= 100) {
            timerLabel.setColor(255, 0,0, 1f);
            timerLabel.addAction(Actions.alpha(0));
            timerLabel.addAction(Actions.forever(Actions.sequence(
                    Actions.fadeIn(1f),
                    Actions.fadeOut(1f))));
            timerStatus = TimerStatus.FLASHING;
        }
    }

    public void checkTimerEnd() {
        if (timerTime == TIMER_END) {
            logger.debug("Timer has finished");
            entity.getEvents().trigger("timer_ended");
        }
    }
    
    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }

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