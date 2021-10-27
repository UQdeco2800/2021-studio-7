package com.deco2800.game.ui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.rendering.Renderable;
import com.deco2800.game.rendering.components.RenderComponent;
import com.deco2800.game.screens.RetroactiveDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;

/**
 * A generic component for rendering onto the ui.
 */
public abstract class UIComponent extends RenderComponent implements Renderable {
    protected static final Logger logger = LoggerFactory.getLogger(RetroactiveDisplay.class);
    protected static final Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
    private static final Comparator<Actor> comparator =
        Comparator.comparing(Actor::getName);
    protected int UI_LAYER;
    protected float Z_INDEX;
    protected Stage stage;
    protected Table table = new Table();

    public UIComponent() {
        super();
        creationPriority = 51;
        UI_LAYER = 2;
        Z_INDEX = 0f;
    }

    @Override
    public void create() {
        super.create();
        stage = ServiceLocator.getRenderService().getStage();
        addActors();
        table.setName(String.valueOf(Z_INDEX));
        stage.addActor(table);
        stage.getActors().sort(comparator);
    }

    protected abstract void addActors();

    public void hide() {
        table.setVisible(false);
    }

    public void show() {
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
    public float getZIndex() {
        return Z_INDEX;
    }
}
