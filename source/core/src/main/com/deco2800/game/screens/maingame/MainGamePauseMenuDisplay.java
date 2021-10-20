package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainGamePauseMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGamePauseMenuDisplay.class);
    private static final String PAUSE_BACKGROUND = "images/ui/screens/paused_screen.png";
    private Table table;
    private static final float Z_INDEX = 2f;
    private boolean isVisible = false;

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("toggle_pause_visibility", this::toggleVisibility);
        addActors();
    }

    public void toggleVisibility() {
        isVisible = !isVisible;
        table.setVisible(isVisible);
        if (isVisible) {
            ServiceLocator.getGame().getScreen().pause();
        } else {
            ServiceLocator.getGame().getScreen().resume();
        }
    }

    private void addActors() {
        table = new Table(skin);
        table.setFillParent(true);
        table.bottom().padBottom(20f);
        table.setVisible(isVisible);

        // Set background to the appropriate texture for the end game condition.
        ServiceLocator.getResourceService().loadTexture(PAUSE_BACKGROUND);
        ServiceLocator.getResourceService().loadAll();
        Image background =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset(PAUSE_BACKGROUND, Texture.class));
        table.setBackground(background.getDrawable());

        // Add button container to the table.
        // Easily sorts buttons vertically and separates padding settings from table.
        // It is assumed that more buttons will eventually be added.
        HorizontalGroup buttonContainer = new HorizontalGroup();
        buttonContainer.space(10f);
        table.add(buttonContainer);

        // Add button to container. Resumes the game.
        TextButton resumeBtn = new TextButton("Resume", skin);
        resumeBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Resume button clicked");
                    toggleVisibility();
                }
            });
        buttonContainer.addActor(resumeBtn);

        // Add button to container. Restarts the game.
        TextButton restartBtn = new TextButton("Restart", skin);
        restartBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Restart button clicked");
                        MainGameScreen.zerolevel();
                        ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_GAME);
                    }
                });
        buttonContainer.addActor(restartBtn);

        // Add button to container. Goes to the settings display.
        TextButton settingsBtn = new TextButton("Settings", skin);
        settingsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Settings button clicked");
                        ServiceLocator.getGame().setScreen(GdxGame.ScreenType.SETTINGS);
                    }
                });
        buttonContainer.addActor(settingsBtn);

        // Add button to container. Goes back to the main menu.
        TextButton mainMenuBtn = new TextButton("Main Menu", skin);
        mainMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Main menu button clicked");
                        ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_MENU);
                    }
                });
        buttonContainer.addActor(mainMenuBtn);

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
