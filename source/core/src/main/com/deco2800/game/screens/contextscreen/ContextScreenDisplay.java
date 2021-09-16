package com.deco2800.game.screens.contextscreen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.EndGameDisplay;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the Context screen.
 */
public class ContextScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(EndGameDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;

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


        HorizontalGroup imageContainer = new HorizontalGroup();
        imageContainer.fill();
        stage.addActor(table);

        table = new Table();
        table.setFillParent(true);

        //Adds the story text, however the wrapping is not working properly so new lines are added
        Label storyText = new Label("It’s 1982, a school night and mum went out tonight, " +
                "saying she'll be back\nby midnight." +
                "It's now 11:00, way past my bed time, but I hear the car\npull into the driveway. She's" +
                "home early and coming after me. Can I make\nit to bed before she catches me?", skin);
        storyText.setFontScale((float) 1.2);

        table.add(storyText).padBottom(30f).padRight(10f);
        table.row();

        //This is the image of the mum and the bed, they were combined as a PNG as it is easier to
        //be added rather than build multiple assets for it. This is because there are no functional
        //Actions by them
        Image mumAndBed =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/ui/context/mum_and_bed.PNG", Texture.class));

        table.add(mumAndBed);
        table.row();
        table.add(buttonContainer).padTop(30f);

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
