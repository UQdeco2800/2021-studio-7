package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.ui.components.UIComponent;

/**
 * Creates a toggle-able and variable text box display at the bottom of the screen.
 *
 * On creation, will not display anything, but can call MainGameTextDisplay.display to display a
 * box with text (and optional image). Can then call remove to remove it.
 * TODO Decide whether to dispose after set time, or need to call to remove it.
 */
public class MainGameTextDisplay extends UIComponent {
    private Table table;

    @Override
    public void create() {
        super.create();
        addActors();
        display("This is some text. How do you like them apples?");
    }

    private void addActors() {
        table = new Table();
        table.bottom().padBottom(100f);
        table.setFillParent(true);
        stage.addActor(table);
    }

    /**
     * Displays a text box at the bottom of the screen containing the given text.
     *
     * @param text The text to display
     */
    public void display(String text) {
        Texture texture = new Texture(Gdx.files.internal(
                "images/ui/elements/Textbox_1024.png"));
        Image image = new Image(texture);
        table.add(image);
        Label displayText = new Label(text, skin, "large");
        displayText.setText(text);
        table.add(displayText);
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}
