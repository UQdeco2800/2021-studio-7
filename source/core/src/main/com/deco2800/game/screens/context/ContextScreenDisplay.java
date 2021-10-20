package com.deco2800.game.screens.context;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the Context screen.
 */
public class ContextScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ContextScreenDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private static TextButton button;

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

        //This is the image of the mum and the bed, they were combined as a PNG as it is easier to
        //be added rather than build multiple assets for it. This is because there are no functional
        //Actions by them
        Image context =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/context_screen/context_screen.PNG", Texture.class));

        table.add(context);
        table.row();
        table.add(buttonContainer).padTop(30f);

        // Add button to container. Transitions to the next level (main game screen).
        TextButton playGameBtn = new TextButton("PRESS ENTER TO PLAY!", skin);
        button = playGameBtn;
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

    public static void playButton(){
        button.toggle();
    }
}
