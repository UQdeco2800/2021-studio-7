package com.deco2800.game.screens.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveDisplay;

public class PauseDisplay extends RetroactiveDisplay {
    private static final String PAUSE_BACKGROUND = "images/ui/screens/paused_screen.png";
    private HorizontalGroup settingsButtonsContainer;
    private int settingsButtonIndex = 0;

    public PauseDisplay() {
        super();
        textures.add(PAUSE_BACKGROUND);
    }

    @Override
    protected void addActors() {
        table = new Table();
        table.bottom().padBottom(20f).setFillParent(true);
        table.setTouchable(Touchable.disabled);

        Image background = new Image(ServiceLocator.getResourceService().getAsset(PAUSE_BACKGROUND, Texture.class));
        table.setBackground(background.getDrawable());

        settingsButtonsContainer = createSettingsButtonsContainer();
        table.add(settingsButtonsContainer);
    }

    private HorizontalGroup createSettingsButtonsContainer() {
        HorizontalGroup container = new HorizontalGroup();
        container.space(10f);

        TextButton resumeBtn = new TextButton("Resume", skin);
        resumeBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Resume button clicked");
                    entity.getEvents().trigger("exit_pause");
                }
            });
        container.addActor(resumeBtn);

        TextButton restartBtn = new TextButton("Restart", skin);
        restartBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Restart button clicked");
                    entity.getEvents().trigger("main_game");
                }
            });
        container.addActor(restartBtn);

        TextButton settingsBtn = new TextButton("Settings", skin);
        settingsBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Settings button clicked");
                    entity.getEvents().trigger("enter_settings");
                }
            });
        container.addActor(settingsBtn);

        TextButton mainMenuBtn = new TextButton("Main Menu", skin);
        mainMenuBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Main menu button clicked");
                    entity.getEvents().trigger("main_menu");
                }
            });
        container.addActor(mainMenuBtn);

        return container;
    }

    @Override
    protected void keyDown(int keyCode) {
        switch (keyCode) {
            case Keys.LEFT:
            case Keys.A:
                entity.getEvents().trigger("play_sound", "browse");
                settingsButtonIndex = changeSelectedButton(settingsButtonsContainer, settingsButtonIndex, -1);
                break;
            case Keys.RIGHT:
            case Keys.D:
                entity.getEvents().trigger("play_sound", "browse");
                settingsButtonIndex = changeSelectedButton(settingsButtonsContainer, settingsButtonIndex, 1);
                break;
            case Keys.ENTER:
                ((TextButton) settingsButtonsContainer.getChild(settingsButtonIndex)).toggle();
                break;
            case Keys.P:
            case Keys.ESCAPE:
                entity.getEvents().trigger("exit_pause");
                break;
            default:
        }
    }

    @Override
    public void show() {
        super.show();
        settingsButtonIndex = changeSelectedButton(settingsButtonsContainer, settingsButtonIndex, -999);
    }
}
