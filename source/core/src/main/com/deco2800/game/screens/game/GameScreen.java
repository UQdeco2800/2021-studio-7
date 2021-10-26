package com.deco2800.game.screens.game;

import com.deco2800.game.GdxGame;
import com.deco2800.game.chores.ChoreController;
import com.deco2800.game.chores.ChoreUI;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.input.components.InputDecorator;
import com.deco2800.game.maps.Home;
import com.deco2800.game.maps.components.PerformanceDisplay;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.screens.RetroactiveScreen;
import com.deco2800.game.screens.SettingsDisplay;
import com.deco2800.game.screens.game.widgets.FogOverlay;
import com.deco2800.game.screens.game.widgets.PromptWidget;
import com.deco2800.game.screens.game.widgets.TimerWidget;
import com.deco2800.game.ui.terminal.Terminal;
import com.deco2800.game.ui.terminal.TerminalDisplay;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class GameScreen extends RetroactiveScreen {
    private static final String TEST_FLOOR_PLAN = "maps/testing/demo.json";
    private static final boolean TESTING = false;
    private final PhysicsEngine physicsEngine;
    private final Home home;
    private Entity player;

    public GameScreen(GdxGame game) {
        super(game);

        ServiceLocator.registerChoreController(new ChoreController(game.getLevel()));
        physicsEngine = ServiceLocator.getPhysicsService().getPhysics();
        renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

        createUI();
        loadAssets();
        ServiceLocator.getEntityService().register(ui);

        if (TESTING) {
            home = new Home(TEST_FLOOR_PLAN);
        } else {
            home = new Home();
        }
        ServiceLocator.registerHome(home);

        home.create(renderer.getCamera());

        player = home.getActiveFloor().getPlayer();
        game.setLevel(game.getLevel() + 1);
    }

    @Override
    public void render(float delta) {
        if (!gamePaused) {
            physicsEngine.update();
            ServiceLocator.getEntityService().update();
        }
        if (nextScreen == null) {
            renderer.getCamera().getEntity().setPosition(player.getPosition());
            renderer.render();
        } else {
            game.setScreen(nextScreen);
        }
    }

    @Override
    protected void loadAssets() {
        logger.debug("Loading assets");

        ui.getComponent(ContextDisplay.class).loadAssets();
        ui.getComponent(PauseDisplay.class).loadAssets();
        ui.getComponent(SettingsDisplay.class).loadAssets();
        ui.getComponent(GameActions.class).loadAssets();

        ServiceLocator.getResourceService().loadAll();
    }

    @Override
    protected void unloadAssets() {
        logger.debug("Unloading assets");

        ui.getComponent(ContextDisplay.class).unloadAssets();
        ui.getComponent(PauseDisplay.class).unloadAssets();
        ui.getComponent(SettingsDisplay.class).unloadAssets();
        ui.getComponent(GameActions.class).unloadAssets();
    }

    /**
     * Creates the main game's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    @Override
    protected void createUI() {
        logger.debug("Creating ui");

        ui.addComponent(new InputDecorator(ServiceLocator.getRenderService().getStage(), 10))
            .addComponent(ServiceLocator.getInputService().getInputFactory().createForTerminal())
            .addComponent(new FogOverlay())
            .addComponent(new PerformanceDisplay())
            .addComponent(new TimerWidget())
            .addComponent(new PromptWidget())
            .addComponent(new ChoreUI())
            .addComponent(new Terminal())
            .addComponent(new TerminalDisplay())
            .addComponent(new ContextDisplay())
            .addComponent(new PauseDisplay())
            .addComponent(new SettingsDisplay())
            .addComponent(new GameActions(this));
    }

    @Override
    public void dispose() {
        logger.debug("Disposing main game screen");

        player.getEvents().trigger("write_score");
        unloadAssets();
        renderer.dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();
        ServiceLocator.clear();
    }

    public Entity getMainGameEntity() {
        return ui;
    }

    public Entity getPlayer() {
        return player;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }
}
