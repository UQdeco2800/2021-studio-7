package com.deco2800.game.screens.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveDisplay;

/**
 * UI component for displaying the Tittle screen.
 */
public class TitleDisplay extends RetroactiveDisplay {
    private static final String[] TEXTURES = {
        "images/ui/title/RETROACTIVE-large.png",
        "images/ui/screens/inactiveStart.png"
    };

    @Override
    public void create() {
        super.create();

        VerticalGroup container = new VerticalGroup();
        container.space(50f);

        Image title = new Image(ServiceLocator.getResourceService().getAsset(TEXTURES[0], Texture.class));
        container.addActor(title);

        Label startText = new Label("PRESS ANY KEY TO START", skin, "title");
        startText.addAction(Actions.alpha(0));
        startText.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(1f), Actions.fadeOut(1f))));
        container.addActor(startText);

        Image bed = new Image(ServiceLocator.getResourceService().getAsset(TEXTURES[1], Texture.class));
        container.addActor(bed);

        table.add(container);
    }

    @Override
    protected Group createButtons() {
        return null;
    }

    @Override
    protected void keyUp(int keyCode) {
        entity.getEvents().trigger("play_sound", "browse");
        entity.getEvents().trigger("exit_title");
    }

    @Override
    public void loadAssets() {
        logger.debug("   Loading title display assets");
        super.loadAssets();
        ServiceLocator.getResourceService().loadAssets(TEXTURES, Texture.class);
    }

    @Override
    public void unloadAssets() {
        logger.debug("   Unloading title display assets");
        super.unloadAssets();
        ServiceLocator.getResourceService().unloadAssets(TEXTURES);
    }
}