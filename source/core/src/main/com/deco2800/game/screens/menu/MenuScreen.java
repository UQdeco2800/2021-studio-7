package com.deco2800.game.screens.menu;

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

        initialiseUI();
        loadAssets();
        ui.getEvents().trigger("play_music", "menu");
    }

    @Override
    protected void initialiseUI() {
        logger.debug("Creating menu screen ui");

        ui = new Entity()
            .addComponent(new InputDecorator(ServiceLocator.getRenderService().getStage(), 10))
            .addComponent(new KeyboardMenuInputComponent())
            .addComponent(new TitleDisplay())
            .addComponent(new MenuDisplay())
            .addComponent(new LeaderboardDisplay())
            .addComponent(new SettingsDisplay())
            .addComponent(new MenuActions(this));
    }

    @Override
    public void loadAssets() {
        logger.debug("Loading menu screen assets");

        ui.loadAssets();
        ServiceLocator.getResourceService().loadAll();
        ServiceLocator.getEntityService().register(ui);
    }

    @Override
    public void unloadAssets() {
        logger.debug("Unloading menu screen assets");

        ui.unloadAssets();
    }

    @Override
    public void dispose() {
        logger.debug("Disposing menu screen");

        renderer.dispose();
        unloadAssets();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.clear();
    }
}