package com.deco2800.game.screens.contextscreen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.endgame.EndGameDisplay;
import com.deco2800.game.screens.endgame.EndGameScreen;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(EndGameDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Label storyText;

    public ContextScreenDisplay() {
        super();
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);
        // Add button container to the table.
        // Easily sorts buttons vertically and separates padding settings from table.
        // It is assumed that more buttons will eventually be added.
        VerticalGroup buttonContainer = new VerticalGroup();
        buttonContainer.fill();
        buttonContainer.bottom().right();
        buttonContainer.space(10f);

        stage.addActor(table);

        table = new Table();
        table.setFillParent(true);

        storyText = new Label("This is where the story goes\n" +
                "\n",skin, "title");

        table.add(storyText).padTop(50f);
        table.row();
        table.add(buttonContainer);

        // Add button to container. Transitions to the next level (main game screen).
        TextButton playGameBtn = new TextButton("Play Game", skin);

        playGameBtn.getLabel().setColor(0, 0,0, 1f);
        playGameBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Play game button clicked");
                        entity.getEvents().trigger("play_game");
                    }
                });
        buttonContainer.addActor(playGameBtn);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}
