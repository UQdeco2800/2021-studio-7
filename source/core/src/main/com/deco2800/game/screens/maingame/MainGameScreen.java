package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.chores.ChoreController;
import com.deco2800.game.chores.ChoreUI;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.components.player.CameraComponent;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.generic.GameTime;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.input.components.InputComponent;
import com.deco2800.game.input.components.InputDecorator;
import com.deco2800.game.maps.Home;
import com.deco2800.game.maps.components.PerformanceDisplay;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
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
  private static final String[] mainGameTextures = {};
  private static final String TEST_FLOOR_PLAN = "maps/_floor_plans/floor_plan_testing.json";
   private static final boolean USE_TEST_FLOOR_PLAN = false;
  //add background music into the game
  private static final String[] backgroundMusic = {"sounds/backgroundMusic-MG.mp3"};
  private static final String[] pauseGameTextures = {"images/ui/screens/paused_screen.png"};

  private final Renderer renderer;
  private final Renderer miniMapRenderer;
  private OrthographicCamera cameraMiniMap;
  private final PhysicsEngine physicsEngine;
  private final Home home;
  private final Entity mainGameEntity = new Entity();
  private Entity player;
  private boolean gamePaused = false;
  private static int level = 1;


  public MainGameScreen() {
    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());

    ServiceLocator.registerChoreController(new ChoreController());

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());

    Entity cameraMiniMap = new Entity().addComponent(new CameraComponent());
    CameraComponent camComponent = cameraMiniMap.getComponent(CameraComponent.class);

    //This is the renderer for the minimap, essentially its display
    miniMapRenderer = new Renderer(camComponent);
    miniMapRenderer.getCamera().getEntity().setPosition(10,10);

    //This is the main game renderer, which must be called last so the UI is shown
    renderer = RenderFactory.createRenderer();
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();
    createUI();

    if (USE_TEST_FLOOR_PLAN) {
      home = new Home(TEST_FLOOR_PLAN);
    } else {
      home = new Home();
    }
    home.setMainGameScreen(this);
    ServiceLocator.registerHome(home);

    home.create(miniMapRenderer.getCamera(), renderer.getCamera());
    home.getActiveFloor().getMiniMapCamera().position.set(10,10,10);

    //Adjust the minimap renderer to achieve a more isometric
    miniMapRenderer.getCamera().resize(2,1,200);
    player = home.getActiveFloor().getPlayer();
    this.level = ++level;
    //playMusic();
  }

  public static int getLevel() {
    return level;
  }

  @Override
  public void render(float delta) {
    if (!gamePaused) {
      physicsEngine.update();
      ServiceLocator.getEntityService().update();
    }
    renderer.getCamera().getEntity().setPosition(player.getPosition());
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
    gamePaused = true;
  }

  @Override
  public void resume() {
    logger.info("Game resumed");
    gamePaused = false;
  }

  @Override
  public void dispose() {
    logger.debug("Disposing main game screen");

    renderer.dispose();
    unloadAssets();
    player.getEvents().trigger("write_score");
    ServiceLocator.getEntityService().dispose();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getResourceService().dispose();

    ServiceLocator.clear();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainGameTextures);
    resourceService.loadMusic(backgroundMusic);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainGameTextures);
    resourceService.unloadAssets(backgroundMusic);
  }

//  /**
//   * Play the background Music
//   */
//  private void playMusic() {
//    Music music =
//            ServiceLocator.getResourceService().getAsset(backgroundMusic[0],
//                    Music.class);
//    music.setLooping(true);
//    music.setVolume(0.3f);
//    music.play();
//  }

  /**
   * Creates the main game's ui including components for rendering ui elements to the screen and
   * capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForTerminal();

    mainGameEntity.addComponent(new InputDecorator(stage, 10))
        .addComponent(new PerformanceDisplay())
        .addComponent(new MainGameActions())
        .addComponent(new MainGamePauseMenuDisplay())
        .addComponent(new MainGameExitDisplay())
        .addComponent(new MainGameFogScreen())
        .addComponent(new MainGameTimerDisplay())
        .addComponent(new MainGameTextDisplay())
        .addComponent(new ChoreUI())
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay());
    ServiceLocator.getEntityService().register(mainGameEntity);
  }

  public Entity getMainGameEntity() {
    return mainGameEntity;
  }

  public Home getHome() {
    return home;
  }

  public String getTestingFloorPlan() {
    return TEST_FLOOR_PLAN;
  }

  public Entity getPlayer() {
    return player;
  }

  public void setPlayer(Entity player) {
    this.player = player;
  }
}
