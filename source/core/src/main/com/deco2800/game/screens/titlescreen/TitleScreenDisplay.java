package com.deco2800.game.screens.titlescreen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.input.components.InputComponent;
import com.deco2800.game.screens.endgame.EndGameDisplay;
import com.deco2800.game.ui.components.UIComponent;

import com.deco2800.game.entities.components.player.KeyboardPlayerInputComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TitleScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(EndGameDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Label startText;

    public TitleScreenDisplay() {
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

        startText = new Label("PRESS ANY KEY TO START",skin, "title");
        startText.addAction(Actions.alpha(0));
        startText.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(1f), Actions.fadeOut(1f))));

        Image title =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/ui/title/RETROACTIVE-large.png", Texture.class));

        Image bed =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/ui/screens/inactiveStart.png", Texture.class));

        table.add(title);
        table.row();
        table.add(startText).padTop(50f);
        table.row();
        table.add(bed).padTop(50f).padBottom(20f);
        stage.addActor(table);
        // Add button container to the table.
        // Easily sorts buttons vertically and separates padding settings from table.
        // It is assumed that more buttons will eventually be added.
//        VerticalGroup buttonContainer = new VerticalGroup();
//        buttonContainer.fill();
//        buttonContainer.bottom().right();
//        buttonContainer.space(10f);
//        table.bottom();
//        table.padBottom(100f);
//        table.add(buttonContainer);
//
//        // Add button to container. Transitions to the next level (main game screen).
//        TextButton StartBtn = new TextButton("INSERT COIN", skin);
//        StartBtn.addListener(
//                new ChangeListener() {
//                    @Override
//                    public void changed(ChangeEvent changeEvent, Actor actor) {
//                        logger.debug("Start button clicked");
//                        entity.getEvents().trigger("go_menu");
//                    }
//                });
//        buttonContainer.addActor(StartBtn);
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
