package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.components.PerformanceDisplay;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputService;
import com.deco2800.game.input.components.InputComponent;
import com.deco2800.game.input.components.InputDecorator;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.generic.GameTime;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.terminal.Terminal;
import com.deco2800.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  private static final String[] mainGameTextures = {""};
  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);
  private Entity entityPlayer;
  private Vector2 PLAYER_POSITION;

  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;
  private final ForestGameArea mainGameArea;
  private final Entity mainGameEntity = new Entity();

  public MainGameScreen() {
    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();
    createUI();
    ServiceLocator.getEntityService().register(mainGameEntity);

    logger.debug("Initialising main game screen entities");
      TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera(), TerrainComponent.TerrainOrientation.ISOMETRIC);
      mainGameArea = new ForestGameArea(terrainFactory);
//    LevelTerrainFactory terrainFactory;
//    try {
//        terrainFactory =
//                new LevelTerrainFactory("./maps/s1",
//                        renderer.getCamera());
//    } catch (IOException e) {
//        logger.error(e.toString());
//        logger.error("Check map files that were loaded");
//        return;
//    }
//    ForestGameArea forestGameArea = new ForestGameArea(terrainFactory);
    mainGameArea.create();
    //physicsEngine.getContactListener().setTargetFixture(forestGameArea.
            //getPlayer().getComponent(ColliderComponent.class));
    //physicsEngine.getContactListener().setEnemyFixture(forestGameArea.
            //getMom().getComponent(ColliderComponent.class));
    entityPlayer = mainGameArea.player;
    PLAYER_POSITION = entityPlayer.getPosition();
    renderer.getCamera().getEntity().setPosition(PLAYER_POSITION);


  }

  @Override
  public void render(float delta) {
    PLAYER_POSITION = entityPlayer.getPosition();
    renderer.getCamera().getEntity().setPosition(PLAYER_POSITION);
    physicsEngine.update();
    ServiceLocator.getEntityService().update();
    renderer.render();
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
  }

  @Override
  public void pause() {
    logger.info("Game paused");
  }

  @Override
  public void resume() {
    logger.info("Game resumed");
  }

  @Override
  public void dispose() {
    logger.debug("Disposing main game screen");

    renderer.dispose();
    unloadAssets();

    ServiceLocator.getEntityService().dispose();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getResourceService().dispose();

    ServiceLocator.clear();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainGameTextures);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainGameTextures);
  }

  /**
   * Creates the main game's ui including components for rendering ui elements to the screen and
   * capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForTerminal();
    MainGameTimerDisplay mainGameTimer =
            new MainGameTimerDisplay(10);

    mainGameEntity.addComponent(new InputDecorator(stage, 10))
        .addComponent(new PerformanceDisplay())
        .addComponent(new MainGameActions())
        .addComponent(new MainGameExitDisplay())
//        .addComponent(mainGameTimer)
        .addComponent(new MainGameTimerDisplay())
        .addComponent(new MainGameWinLossTestingDisplay())
        .addComponent(new MainGameTextDisplay())
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay());
  }

  public ForestGameArea getMainGameArea() {
    return mainGameArea;
  }

  public Entity getMainGameEntity() {
    return mainGameEntity;
  }
}
