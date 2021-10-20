package com.deco2800.game.screens.mainmenu;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.components.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the main menu.
 */
public class MainMenuScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuScreen.class);
  private final Renderer renderer;
  private static final String[] mainMenuTextures = {
          "images/ui/elements/menuFrame-LONG.png",
          "images/ui/title/RETROACTIVE-large.png",
          "images/characters/boy_01/boy_01_menu_preview.png",
          "images/characters/girl_00/girl_00_menu_preview.png",
          "images/characters/boy_00/boy_00_menu_preview.png",
          "images/main_menu/bgart.png",
          "images/main_menu/pointer-R-inactive.png",
          "images/main_menu/pointer-L-inactive.png"
  };
  //add background music into the game
  private static final String[] backgroundMusic = {"sounds/backgroundMusic-EP" +
          ".mp3"};

  private static final String[] buttonSounds = {
          "sounds/browse-short.ogg"
  };

  public MainMenuScreen() {
    logger.debug("Initialising main menu screen services");
    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());

    renderer = RenderFactory.createRenderer();

    loadAssets();
    createUI();
    playMusic();
  }

  /**
   * Play the background Music
   */
  private void playMusic() {
    Music music =
            ServiceLocator.getResourceService().getAsset(backgroundMusic[0],
                    Music.class);
    music.setLooping(true);
    music.setVolume(0.2f);
    music.play();
  }

  /**
   * Play button sounds
   * @param button button pressed
   */
  public static void playButtonSound(String button) {
    Sound sound = ServiceLocator.getResourceService().getAsset(buttonSounds[0], Sound.class);
    sound.play();
    logger.info("{} button sound played", button);
  }

  @Override
  public void render(float delta) {
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
    logger.debug("Disposing main menu screen");

    renderer.dispose();
    unloadAssets();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getEntityService().dispose();

    ServiceLocator.clear();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainMenuTextures);
    resourceService.loadMusic(backgroundMusic);
    resourceService.loadSounds(buttonSounds);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainMenuTextures);
    resourceService.unloadAssets(backgroundMusic);
    resourceService.unloadAssets(buttonSounds);
  }

  /**
   * Creates the main menu's ui including components for rendering ui elements to the screen and
   * capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    Entity ui = new Entity();
    ui.addComponent(new MainMenuDisplay())
        .addComponent(new InputDecorator(stage, 10))
        .addComponent(new MainMenuActions());
    ServiceLocator.getEntityService().register(ui);
    Gdx.input.setInputProcessor(new MenuInputProcessor());
  }
  
}


