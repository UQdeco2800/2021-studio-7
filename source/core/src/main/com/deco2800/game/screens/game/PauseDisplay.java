package com.deco2800.game.screens.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.rendering.components.RenderPriority;
import com.deco2800.game.screens.RetroactiveDisplay;

public class PauseDisplay extends RetroactiveDisplay {
    private static final String PAUSE_BACKGROUND = "images/ui/screens/paused_screen.png";

    public PauseDisplay() {
        super();
        renderPriority = RenderPriority.WIDGET.ordinal() - 0.05f;
    }

    @Override
    public void create() {
        super.create();
        table.bottom().padBottom(20f).setTouchable(Touchable.disabled);

        Image background = new Image(ServiceLocator.getResourceService().getAsset(PAUSE_BACKGROUND, Texture.class));
        table.setBackground(background.getDrawable());

        table.add(createButtons());
    }

    @Override
    protected Group createButtons() {
        buttonContainer = new HorizontalGroup();
        traverseBackwards = new int[]{Keys.LEFT, Keys.A};
        traverseForwards = new int[]{Keys.RIGHT, Keys.D};
        enter = new int[]{Keys.ENTER};

        ((HorizontalGroup) buttonContainer).space(10f);

        TextButton resumeBtn = new TextButton("Resume", skin);
        resumeBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Resume button clicked");
                    entity.getEvents().trigger("exit_pause");
                }
            });
        buttonContainer.addActor(resumeBtn);

        TextButton restartBtn = new TextButton("Restart", skin);
        restartBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Restart button clicked");
                    entity.getEvents().trigger("queue_main_game");
                }
            });
        buttonContainer.addActor(restartBtn);

        TextButton settingsBtn = new TextButton("Settings", skin);
        settingsBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Settings button clicked");
                    entity.getEvents().trigger("enter_settings");
                }
            });
        buttonContainer.addActor(settingsBtn);

        TextButton mainMenuBtn = new TextButton("Main Menu", skin);
        mainMenuBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Main menu button clicked");
                    entity.getEvents().trigger("queue_main_menu");
                }
            });
        buttonContainer.addActor(mainMenuBtn);

        triggerHighlight();

        return buttonContainer;
    }

    @Override
    protected void keyUp(int keycode) {
        super.keyUp(keycode);
        if (keycode == Keys.P || keycode == Keys.ESCAPE) {
            entity.getEvents().trigger("play_sound", "confirm");
            entity.getEvents().trigger("exit_pause");
        }
    }

    @Override
    public void loadAssets() {
        logger.debug("    Loading pause display assets");
        super.loadAssets();
        ServiceLocator.getResourceService().loadAsset(PAUSE_BACKGROUND, Texture.class);
    }

    @Override
    public void unloadAssets() {
        logger.debug("    Unloading pause display assets");
        super.unloadAssets();
        ServiceLocator.getResourceService().unloadAsset(PAUSE_BACKGROUND);
    }
}
