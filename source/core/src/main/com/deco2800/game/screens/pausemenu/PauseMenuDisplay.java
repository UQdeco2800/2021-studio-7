package com.deco2800.game.screens.pausemenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.mainmenu.MainMenuDisplay;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.SerializedLambda;
import java.security.Provider;

public class PauseMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PauseMenuDisplay.class);
    private Table table;
    private static final float Z_INDEX = 2f;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);
        Image bg = new Image(ServiceLocator.getResourceService()
                .getAsset("images/ui/screens/paused_screen.png", Texture.class));

        TextButton resumeBtn = new TextButton("Resume", skin);
        TextButton restartBtn = new TextButton("Restart from Start", skin);
        TextButton mainMenuBtn = new TextButton("Main Menu", skin);
        TextButton settingsBtn = new TextButton("Settings", skin);

        // Trigger resume game
        resumeBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Resume button clicked");
                    entity.getEvents().trigger("resume");
                }
            });

        // Trigger restart game
        restartBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Restart button clicked");
                        entity.getEvents().trigger("restart");
                    }
                });

        // Trigger to go to settings menu
        settingsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Settings button clicked");
                        entity.getEvents().trigger("settings");
                    }
                });

        // Trigger to go to main menu
        mainMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Main menu button clicked");
                        entity.getEvents().trigger("main_menu");
                    }
                });

        table.add(bg);
        table.row();
        table.add(resumeBtn).padTop(50f);
        table.row();
        table.add(restartBtn).padTop(15f);
        table.row();
        table.add(settingsBtn).padTop(15f);
        table.row();
        table.add(mainMenuBtn).padTop(15f);
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
