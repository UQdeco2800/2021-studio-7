package com.deco2800.game.screens.menu;

import com.badlogic.gdx.Gdx;
import com.deco2800.game.GdxGame;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.input.components.InputDecorator;
import com.deco2800.game.input.components.KeyboardMenuInputComponent;
import com.deco2800.game.screens.RetroactiveScreen;
import com.deco2800.game.screens.SettingsDisplay;

/**
 * The game screen containing the main menu.
 */
public class MenuScreen extends RetroactiveScreen {

    public MenuScreen(GdxGame game) {
        super(game);
        logger.debug("Initialising main menu screen services");

        createUI();
        loadAssets();
        ServiceLocator.getEntityService().register(ui);
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
        if (nextScreen != null) {
            game.setScreen(nextScreen);
        }
    }

    @Override
    public void dispose() {
        logger.debug("Disposing main menu screen");

        renderer.dispose();
        unloadAssets();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();

        ServiceLocator.clear();
    }

    @Override
    protected void loadAssets() {
        logger.debug("Loading assets");

        ui.getComponent(TitleDisplay.class).loadAssets();
        ui.getComponent(MenuDisplay.class).loadAssets();
        ui.getComponent(LeaderboardDisplay.class).loadAssets();
        ui.getComponent(SettingsDisplay.class).loadAssets();
        ui.getComponent(MenuActions.class).loadAssets();

        ServiceLocator.getResourceService().loadAll();
    }

    @Override
    protected void unloadAssets() {
        logger.debug("Unloading assets");

        ui.getComponent(TitleDisplay.class).unloadAssets();
        ui.getComponent(MenuDisplay.class).unloadAssets();
        ui.getComponent(LeaderboardDisplay.class).unloadAssets();
        ui.getComponent(SettingsDisplay.class).unloadAssets();
        ui.getComponent(MenuActions.class).unloadAssets();
    }

    @Override
    protected void createUI() {
        logger.debug("Creating ui");

        ui = new Entity()
            .addComponent(new InputDecorator(ServiceLocator.getRenderService().getStage(), 10))
            .addComponent(new KeyboardMenuInputComponent())
            .addComponent(new TitleDisplay())
            .addComponent(new MenuDisplay())
            .addComponent(new LeaderboardDisplay())
            .addComponent(new SettingsDisplay())
            .addComponent(new MenuActions(this));
    }
}