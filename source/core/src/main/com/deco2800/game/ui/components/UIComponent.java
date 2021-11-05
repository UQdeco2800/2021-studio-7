package com.deco2800.game.ui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.rendering.components.RenderPriority;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.components.RenderComponent;
import com.deco2800.game.screens.RetroactiveDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A generic component for rendering onto the ui.
 */
public abstract class UIComponent extends RenderComponent {
    protected static final Logger logger = LoggerFactory.getLogger(RetroactiveDisplay.class);
    protected static final Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
    protected static final int UI_LAYER = 2;
    protected float renderPriority = RenderPriority.BACK.ordinal();
    protected Stage stage;
    protected Table table = new Table();

    @Override
    public void create() {
        super.create();
        table.setFillParent(true);
        table.setUserObject(new RenderService.TableUserData(renderPriority));
        stage = ServiceLocator.getRenderService().getStage();
        stage.addActor(table);
    }

    public void hide() {
        enabled = false;
        table.setVisible(false);
    }

    public void show() {
        enabled = true;
        table.setVisible(true);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }

    @Override
    public int getLayer() {
        return UI_LAYER;
    }

    @Override
    public float getRenderPriority() {
        return renderPriority;
    }
}
