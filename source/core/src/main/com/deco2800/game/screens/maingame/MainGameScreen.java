package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.maps.Home;
import com.deco2800.game.maps.components.PerformanceDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputService;
import com.deco2800.game.input.components.InputComponent;
import com.deco2800.game.input.components.InputDecorator;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.generic.GameTime;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import com.deco2800.game.ui.terminal.Terminal;
import com.deco2800.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.awt.*;
import java.util.stream.StreamSupport;

import java.awt.*;
import java.util.stream.StreamSupport;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  private static final String[] mainGameTextures = {""};
  private static final String testingFloorPlan = "maps/_floor_plans/floor_plan_testing.json";
   private static final boolean usingTestingFloorPlan = false;
  //add background music into the game
  private static final String[] backgroundMusic = {"sounds/backgroundMusic-MG.mp3"};
  private static final String[] pauseGameTextures = {
          "images/ui/screens/paused_screen.png"
  };
  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);
  private Entity entityPlayer;
  private Vector2 PLAYER_POSITION;

  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;
  private final Home home;
  private final Entity mainGameEntity = new Entity();
  private Entity player;
  private final Entity pauseGameEntity = new Entity();


  public static final int GAME_RUNNING = 0;
  public static final int GAME_PAUSED = 1;
  public static final int GAME_RESUMING = 2;
  private int gameStatus = GAME_RUNNING;

  private boolean builtPauseMenu = false;

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
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();
    createUI();

    if (usingTestingFloorPlan) {
      home = new Home(testingFloorPlan);
    } else {
      home = new Home();
    }
    home.setMainGameScreen(this);
    ServiceLocator.registerHome(home);
    home.create(renderer.getCamera());
    player = home.getActiveFloor().getPlayer();
    //playMusic();
  }

  @Override
  public void render(float delta) {
    final Table table = new Table();
    if (gameStatus == GAME_PAUSED) {
      if (!builtPauseMenu) {
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(pauseGameTextures);
        ServiceLocator.getResourceService().loadAll();

        //      Table table = new Table();
        table.setFillParent(true);
        //      Image bg = new Image(ServiceLocator.getResourceService()
        //              .getAsset("images/ui/screens/paused_screen.png", Texture.class));

        TextButton resumeBtn = new TextButton("Resume", new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json")));
        TextButton restartBtn = new TextButton("Restart from Start", new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json")));
        TextButton mainMenuBtn = new TextButton("Main Menu", new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json")));
        TextButton settingsBtn = new TextButton("Settings", new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json")));

        // Trigger resume game
        resumeBtn.addListener(
                new ChangeListener() {
                  @Override
                  public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Resume button clicked");
                    mainGameEntity.getEvents().trigger("resume");
                  }
                });

        // Trigger restart game
        restartBtn.addListener(
                new ChangeListener() {
                  @Override
                  public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Restart button clicked");
                    mainGameEntity.getEvents().trigger("restart");
                  }
                });

        // Trigger to go to settings menu
        settingsBtn.addListener(
                new ChangeListener() {
                  @Override
                  public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Settings button clicked");
                    mainGameEntity.getEvents().trigger("settings");
                  }
                });

        // Trigger to go to main menu
        mainMenuBtn.addListener(
                new ChangeListener() {
                  @Override
                  public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Main menu button clicked");
                    mainGameEntity.getEvents().trigger("main_menu");
                  }
                });

        //      table.add(bg);
        //      table.row();
        table.add(resumeBtn).padTop(50f);
        table.row();
        table.add(restartBtn).padTop(15f);
        table.row();
        table.add(settingsBtn).padTop(15f);
        table.row();
        table.add(mainMenuBtn).padTop(15f);
        table.setName("Pause Menu");
        ServiceLocator.getRenderService().getStage().addActor(table);
        ServiceLocator.getRenderService().getStage().draw();
        builtPauseMenu = true;
      }
      renderer.render();


    } else if (gameStatus == GAME_RESUMING) {
      gameStatus = GAME_RUNNING;
      Actor actorToRemove = new Actor();
      for (Actor actor : ServiceLocator.getRenderService().getStage().getActors()) {
        if (actor.getName() != null) {
          actorToRemove = actor;

        }
      }

      actorToRemove.remove();
      renderer.render();
      builtPauseMenu = false;
    } else {
      PLAYER_POSITION = entityPlayer.getPosition();
      renderer.getCamera().getEntity().setPosition(PLAYER_POSITION);
      physicsEngine.update();
      ServiceLocator.getEntityService().update();
      renderer.render();
    }
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
  }

  @Override
  public void pause() {
    logger.info("Game paused");
    gameStatus = GAME_PAUSED;
  }

  @Override
  public void resume() {
    logger.info("Game resumed");
    gameStatus = GAME_RESUMING;
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
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForTerminal();

    mainGameEntity.addComponent(new InputDecorator(stage, 10))
        .addComponent(new PerformanceDisplay())
        .addComponent(new MainGameActions())
        .addComponent(new MainGameExitDisplay())
        .addComponent(new MainGameTimerDisplay())
        .addComponent(new MainGameWinLossTestingDisplay())
        .addComponent(new MainGameTextDisplay())
        .addComponent(new MainGameChoresListDisplay())
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
    return testingFloorPlan;
  }

  public Entity getPlayer() {
    return player;
  }

  public void setPlayer(Entity player) {
    this.player = player;
  }
}
