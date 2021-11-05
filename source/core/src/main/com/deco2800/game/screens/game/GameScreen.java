package com.deco2800.game.screens.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.GdxGame;
import com.deco2800.game.chores.ChoreController;
import com.deco2800.game.chores.ChoreUI;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.ScoreComponent;
import com.deco2800.game.entities.components.player.CameraComponent;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.input.components.InputDecorator;
import com.deco2800.game.input.components.KeyboardMenuInputComponent;
import com.deco2800.game.maps.Home;
import com.deco2800.game.maps.components.PerformanceDisplay;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.screens.RetroactiveScreen;
import com.deco2800.game.screens.SettingsDisplay;
import com.deco2800.game.ui.terminal.KeyboardTerminalInputComponent;
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
    private Home home;
    private Entity player;

    public GameScreen(GdxGame game) {
        super(game);
        game.setLevel(game.getLevel() + 1);

        ServiceLocator.registerChoreController(new ChoreController(game.getLevel()));
        physicsEngine = ServiceLocator.getPhysicsService().getPhysics();
        renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

        initialiseHome();
        initialisePlayer();
        initialiseUI();

        loadAssets();

        ServiceLocator.getEntityService().register(ui);
        ServiceLocator.registerHome(home);

        ui.getEvents().trigger("play_music", "game");
    }

    @Override
    public void render(float delta) {
        if (nextScreen != null) {
            game.setScreen(nextScreen);
        }
        if (!gamePaused) {
            physicsEngine.update();
            ServiceLocator.getEntityService().update();
        }
        renderer.getCamera().getEntity().setPosition(player.getPosition());
        renderer.render();
    }

    protected void initialiseHome() {
        logger.debug("Initialising game screen home");
        home = new Home(this);
        if (TESTING) {
            home.initialise(TEST_FLOOR_PLAN);
        } else {
            home.initialise();
        }
    }

    protected void initialisePlayer() {
        String playerAtlas = PlayerFactory.getAtlas();
        ServiceLocator.getResourceService().loadAsset(playerAtlas, TextureAtlas.class);
        ServiceLocator.getResourceService().loadAll();
        player = PlayerFactory.createPlayer(new String[]{playerAtlas});
    }

    @Override
    protected void initialiseUI() {
        logger.debug("Initialising game screen ui");

        ui = new Entity()
            .addComponent(new InputDecorator(ServiceLocator.getRenderService().getStage(), 10))
            .addComponent(new KeyboardTerminalInputComponent())
            .addComponent(new KeyboardMenuInputComponent())
            .addComponent(new FogWidget())
            .addComponent(new TimerWidget())
            .addComponent(new PromptWidget())
            .addComponent(new ChoreUI())
            .addComponent(new Terminal())
            .addComponent(new TerminalDisplay())
            .addComponent(new PerformanceDisplay())
            .addComponent(new ContextDisplay())
            .addComponent(new PauseDisplay())
            .addComponent(new SettingsDisplay())
            .addComponent(new GameActions(this));
    }

    @Override
    public void loadAssets() {
        logger.debug("Loading game screen assets");

        ui.loadAssets();
        home.loadAssets();

        ServiceLocator.getResourceService().loadAll();
    }

    @Override
    public void unloadAssets() {
        logger.debug("Unloading game screen assets");

        ui.unloadAssets();
        home.unloadAssets();
    }

    @Override
    public void dispose() {
        logger.debug("Disposing game screen");

        player.getEvents().trigger("write_score");
        unloadAssets();
        renderer.dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();
        ServiceLocator.clear();
    }

    public Entity getGameUI() {
        return ui;
    }

    public CameraComponent getCameraComponent() {
        return renderer.getCamera();
    }

    public Entity getPlayer() {
        return player;
    }

    public Home getHome() {
        return home;
    }
}
