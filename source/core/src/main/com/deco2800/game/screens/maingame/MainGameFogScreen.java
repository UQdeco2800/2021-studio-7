package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainGameFogScreen extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameFogScreen.class);
    private Texture texture = new Texture(Gdx.files.internal("images/ui/screens/fog_effect_2.png"));
    private Image background = new Image(texture);

    public MainGameFogScreen() {
        logger.debug("Initialising main game screen timer service");
    }

    /**
     * Draw the renderable. Should be called only by the renderer, not manually.
     *
     * @param batch Batch to render to.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Creates actors and positions them on the stage using a table.
     *
     * @see Table for positioning options
     */
    public void addActors() {
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setOrigin(Align.center);
        stage.addActor(background);
    }

    @Override
    public void dispose() {
        background.clear();
        super.dispose();
    }

    @Override
    public void update() {
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setOrigin(Align.center);
    }
}
