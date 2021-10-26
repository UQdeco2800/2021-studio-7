package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.deco2800.game.GdxGame;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.generic.GameTime;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RetroactiveScreen extends ScreenAdapter {
    protected static final Logger logger = LoggerFactory.getLogger(RetroactiveScreen.class);
    protected final GdxGame game;
    protected Renderer renderer;
    protected Entity ui;
    protected boolean gamePaused = false;
    protected GdxGame.ScreenType nextScreen = null;

    public RetroactiveScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initialising screen services");
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();
    }

    @Override
    public void pause() {
        logger.info("Game paused");
        gamePaused = true;
    }

    @Override
    public void resume() {
        logger.info("Game resumed");
        gamePaused = false;
    }

    public void queueNextScreen(GdxGame.ScreenType screenType) {
        nextScreen = screenType;
    }

    protected abstract void loadAssets();

    protected abstract void unloadAssets();

    protected abstract void createUI();
}
