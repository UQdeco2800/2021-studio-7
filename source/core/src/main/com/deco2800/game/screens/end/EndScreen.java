package com.deco2800.game.screens.end;

import com.deco2800.game.GdxGame;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.input.components.InputDecorator;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.input.components.KeyboardMenuInputComponent;
import com.deco2800.game.screens.RetroactiveScreen;

/**
 * The win/lose screen at the end of the game.
 */
public class EndScreen extends RetroactiveScreen {
    private final int result;

    public EndScreen(GdxGame game, GdxGame.ScreenType result) {
        super(game);

        if (result == GdxGame.ScreenType.WIN_DEFAULT) {
            this.result = 0;
        } else if (result == GdxGame.ScreenType.LOSS_CAUGHT) {
            this.result = 1;
        } else {
            this.result = 2;
        }

        initialiseUI();
        loadAssets();
        ServiceLocator.getEntityService().register(ui);

        if (this.result == 0) {
            ui.getEvents().trigger("play_music", "win");
        } else if (this.result == 1) {
            ui.getEvents().trigger("play_music", "caught");
        } else {
            ui.getEvents().trigger("play_music", "timeout");
        }
    }

    @Override
    protected void initialiseUI() {
        logger.debug("Initialising end screen ui");

        ui = new Entity()
            .addComponent(new InputDecorator(ServiceLocator.getRenderService().getStage(), 10))
            .addComponent(new KeyboardMenuInputComponent())
            .addComponent(new EndDisplay())
            .addComponent(new EndActions(this));
    }

    @Override
    public void loadAssets() {
        logger.debug("Loading assets");

        ui.getComponent(EndDisplay.class).loadAssets();
        ui.getComponent(EndActions.class).loadAssets();

        ServiceLocator.getResourceService().loadAll();
    }

    @Override
    public void unloadAssets() {
        logger.debug("Unloading assets");

        ui.getComponent(EndDisplay.class).unloadAssets();
        ui.getComponent(EndActions.class).unloadAssets();
    }

    @Override
    public void dispose() {
        logger.debug("Disposing end game screen");

        unloadAssets();
        renderer.dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();
        ServiceLocator.clear();
    }

    public int getResult() {
        return result;
    }
}
