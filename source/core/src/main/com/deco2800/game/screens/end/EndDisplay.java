package com.deco2800.game.screens.end;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveDisplay;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class EndDisplay extends RetroactiveDisplay {
    private static final String[] BACKGROUNDS = {
        "images/ui/screens/win_screen.png",
        "images/ui/screens/lose_screen.png",
        "images/ui/screens/time_out.png"
    };

    @Override
    public void create() {
        super.create();
        table.bottom().right().padBottom(10f).padRight(10f);

        Image background = new Image(ServiceLocator.getResourceService()
            .getAsset(BACKGROUNDS[entity.getComponent(EndActions.class).getScreen().getResult()], Texture.class));
        table.setBackground(background.getDrawable());

        table.add(createButtons());
    }

    @Override
    protected Group createButtons() {
        buttonContainer = new VerticalGroup();
        traverseBackwards = new int[]{Keys.UP, Keys.W};
        traverseForwards = new int[]{Keys.DOWN, Keys.S};
        enter = new int[]{Keys.ENTER};

        ((VerticalGroup) buttonContainer).fill().bottom().right().space(10f);

        if (entity.getComponent(EndActions.class).getScreen().getResult() == 0) {
            TextButton nextLevelBtn = new TextButton("Next level", skin);
            nextLevelBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Next level button clicked");
                    entity.getEvents().trigger("queue_main_game");
                }
            });
            buttonContainer.addActor(nextLevelBtn);
        }

        TextButton mainMenuBtn = new TextButton("Back to main menu", skin);
        mainMenuBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Exit button clicked");
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
        if (keycode == Keys.ESCAPE) {
            entity.getEvents().trigger("play_sound", "confirm");
            entity.getEvents().trigger("queue_main_menu");
        }
    }

    @Override
    public void loadAssets() {
        logger.debug("    Loading end display assets");
        super.loadAssets();
        ServiceLocator.getResourceService().loadAssets(BACKGROUNDS, Texture.class);
    }

    @Override
    public void unloadAssets() {
        logger.debug("    Unloading end display assets");
        super.unloadAssets();
        ServiceLocator.getResourceService().unloadAssets(BACKGROUNDS);
    }
}
