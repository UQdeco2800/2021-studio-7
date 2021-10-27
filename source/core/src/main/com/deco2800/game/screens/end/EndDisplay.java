package com.deco2800.game.screens.end;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveDisplay;

import java.util.List;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class EndDisplay extends RetroactiveDisplay {
    private static final String[] BACKGROUNDS = {
        "images/ui/screens/win_screen.png",
        "images/ui/screens/lose_screen.png",
        "images/ui/screens/time_out.png"
    };
    private VerticalGroup endButtonsContainer;
    private int endButtonsIndex = 0;

    @Override
    protected void addActors() {
        table = new Table();
        table.bottom().right().padBottom(10f).padRight(10f).setFillParent(true);

        Image background = new Image(ServiceLocator.getResourceService()
            .getAsset(BACKGROUNDS[ServiceLocator.getScreen(EndScreen.class).getResult()], Texture.class));
        table.setBackground(background.getDrawable());

        endButtonsContainer = createEndButtonsContainer();
        table.add(endButtonsContainer);
    }

    private VerticalGroup createEndButtonsContainer() {
        VerticalGroup container = new VerticalGroup();
        container.fill().bottom().right().space(10f);

        if (ServiceLocator.getScreen(EndScreen.class).getResult() == 0) {
            TextButton nextLevelBtn = new TextButton("Next level", skin);
            nextLevelBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Next level button clicked");
                        entity.getEvents().trigger("next_level");
                    }
                });
            container.addActor(nextLevelBtn);
        }

        TextButton mainMenuBtn = new TextButton("Back to main menu", skin);
        mainMenuBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Exit button clicked");
                    entity.getEvents().trigger("exit");
                }
            });
        container.addActor(mainMenuBtn);

        return container;
    }

    @Override
    protected void keyDown(int keycode) {
        switch (keycode) {
            case Keys.UP:
            case Keys.W:
                endButtonsIndex = changeSelectedButton(endButtonsContainer, endButtonsIndex, -1);
                break;
            case Keys.DOWN:
            case Keys.S:
                endButtonsIndex = changeSelectedButton(endButtonsContainer, endButtonsIndex, 1);
                break;
            case Keys.ENTER:
                ((TextButton) endButtonsContainer.getChild(endButtonsIndex)).toggle();
                break;
            case Keys.ESCAPE:
                entity.getEvents().trigger("main_game");
                break;
            default:
        }
    }

    @Override
    public void show() {
        super.show();
        endButtonsIndex = changeSelectedButton(endButtonsContainer, endButtonsIndex, -999);
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
