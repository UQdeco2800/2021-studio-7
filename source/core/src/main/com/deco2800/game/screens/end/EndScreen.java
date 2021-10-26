package com.deco2800.game.screens.end;

import com.deco2800.game.GdxGame;
import com.deco2800.game.input.components.InputDecorator;
import com.deco2800.game.generic.ServiceLocator;
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

        createUI();
        loadAssets();
        ServiceLocator.getEntityService().register(ui);
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    public int getResult() {
        return result;
    }

    @Override
    protected void createUI() {
        logger.debug("Creating ui");

        ui.addComponent(new InputDecorator(ServiceLocator.getRenderService().getStage(), 10))
            .addComponent(new EndDisplay())
            .addComponent(new EndActions(this));
    }

    @Override
    protected void loadAssets() {
        logger.debug("Loading assets");

        ui.getComponent(EndDisplay.class).loadAssets();
        ui.getComponent(EndActions.class).loadAssets();

        ServiceLocator.getResourceService().loadAll();
    }

    @Override
    protected void unloadAssets() {
        logger.debug("Unloading assets");

        ui.getComponent(EndDisplay.class).unloadAssets();
        ui.getComponent(EndActions.class).unloadAssets();
    }

    @Override
    public void dispose() {
        logger.debug("Disposing end game screen");

        renderer.dispose();
        unloadAssets();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();
    }
}
