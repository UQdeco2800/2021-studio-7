package com.deco2800.game.screens.title;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;

/**
 * A ui component for displaying the Tittle screen.
 */
public class TitleScreenDisplay extends UIComponent {
    private static final float Z_INDEX = 2f;
    private Table table;

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

        Label startText = new Label("PRESS ANY KEY TO START", skin, "title");
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
}
