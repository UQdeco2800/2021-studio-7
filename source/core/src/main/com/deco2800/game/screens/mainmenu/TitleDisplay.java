package com.deco2800.game.screens.mainmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.KeyboardMenuDisplay;

import java.util.ArrayList;
import java.util.List;

/**
 * A ui component for displaying the Tittle screen.
 */
public class TitleDisplay extends KeyboardMenuDisplay {
    private static final String[] TEXTURES = {
            "images/ui/title/RETROACTIVE-large.png",
            "images/ui/screens/inactiveStart.png"
    };

    private Table table;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    @Override
    protected void addActors() {
        table = new Table();
        table.setFillParent(true);

        Label startText = new Label("PRESS ANY KEY TO START", skin, "title");
        startText.addAction(Actions.alpha(0));
        startText.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(1f), Actions.fadeOut(1f))));

        Image title = new Image(ServiceLocator.getResourceService()
                .getAsset(TEXTURES[0], Texture.class));

        Image bed = new Image(ServiceLocator.getResourceService()
                .getAsset(TEXTURES[1], Texture.class));

        table.add(title);
        table.row();
        table.add(startText).padTop(50f);
        table.row();
        table.add(bed).padTop(50f).padBottom(20f);
        stage.addActor(table);
    }

    @Override
    public void onMenuKeyPressed(int keyCode) {
        entity.getEvents().trigger("title_screen_interacted");
    }

    @Override
    public void onNonMenuKeyPressed(int keyCode) {
        entity.getEvents().trigger("title_screen_interacted");
    }

    public static List<String> getAssets() {
        return getAssets(".png");
    }

    public static List<String> getAssets(String extension) {
        List<String> assetsWithExtension = new ArrayList<>();
        if (extension.equals(".png")) {
            assetsWithExtension.addAll(List.of(TEXTURES));
        }
        return assetsWithExtension;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}