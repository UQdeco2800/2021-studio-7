package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.events.listeners.EventListener1;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a toggle-able and variable text box display at the bottom of the screen.
 *
 * By default, will not display anything, but can call MainGameTextDisplay.display to display a
 * box with text (and optional image). Can then call remove to remove it.
 */
public class MainGameTextDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameTextDisplay.class);

    private Table table;
    // load the background texture for the text box
    private Texture texture = new Texture(Gdx.files.internal(
            "images/ui/elements/Textbox_1024.png"));
    private Image background = new Image(texture);
    private Label displayText;
    private boolean visible;
    private long startTime;

    // Divide screen into a more manageable grid
    int row_height = Gdx.graphics.getHeight() / 12;
    int col_width = Gdx.graphics.getWidth() / 10;

    private final long DURATION = 3000L; // The duration for which the textbox stays visible (ms)

    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("create_textbox",
                (EventListener1<String>) this::display);
    }

    /**
     * Create an empty table for storing the background image
     */
    private void addActors() {
        table = new Table();
        table.bottom();
        table.setSize(Gdx.graphics.getWidth(), row_height*4);
        stage.addActor(table);
    }

    /**
     * Displays the text box at the bottom of the screen containing the given text.
     *
     * @param text The text to display
     */
    private void display(String text) {
        // Background texture
        table.add(background);

        // Text
        displayText = new Label(text, skin, "large");
        displayText.setSize(col_width*6, row_height*3);
        displayText.setPosition(col_width*2, row_height);
        //displayText.setPosition(background.getImageX() + 30f, background.getImageY() + 10f);
        displayText.setWrap(true);

        stage.addActor(displayText);

        //table.stack(background, displayText);

        visible = true;
        startTime = ServiceLocator.getTimeSource().getTime();
    }

    /**
     * TODO Displays the text box at the bottom of the screen containing the given text and image.
     *
     * @param text The text to display
     * @param imagePath The path to the image to display
     */
    private void display(String text, String imagePath) {}

    /**
     * Removes all current visual components from the screen (but doesn't do a full cleanup)
     */
    private void hide() {
        table.clear();
        displayText.setText("");
        visible = false;
    }

    /**
     * Removes the textbox after a set amount of time (DURATION)
     */
    @Override
    public void update() {
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (visible && currentTime - startTime >= DURATION) {
            hide();
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        table.clear();
        displayText.setText("");
        displayText.clear();
        super.dispose();
    }
}
