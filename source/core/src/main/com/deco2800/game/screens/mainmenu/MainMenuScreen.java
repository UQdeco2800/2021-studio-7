package com.deco2800.game.screens.mainmenu;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.components.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.input.components.KeyboardMenuInputComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

/**
 * The game screen containing the main menu.
 */
public class MainMenuScreen extends RetroactiveScreen {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuScreen.class);
  private final Renderer renderer;
  private static final String backgroundMusic = "sounds/backgroundMusic-EP.mp3";
  private static final String buttonSounds = "sounds/browse-short.ogg";

  public MainMenuScreen(GdxGame game) {
    super(game);
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
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(0.2f);
    music.play();
  }

  /**
   * Play button sounds
   * @param button button pressed
   */
  public static void playButtonSound(String button) {
    Sound sound = ServiceLocator.getResourceService().getAsset(buttonSounds, Sound.class);
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

  @Override
  protected void loadAssets() {
    logger.debug("Loading assets");

    HashSet<String> displayAssets = new HashSet<>();
    displayAssets.addAll(TitleDisplay.getAssets());
    displayAssets.addAll(MenuDisplay.getAssets());
    displayAssets.addAll(LeaderboardDisplay.getAssets());
    displayAssets.addAll(SettingsDisplay.getAssets());

    ServiceLocator.getResourceService().loadTextures(displayAssets.toArray(new String[0]));
    ServiceLocator.getResourceService().loadAll();
  }

  @Override
  protected void unloadAssets() {
    logger.debug("Unloading assets");

    HashSet<String> displayAssets = new HashSet<>();
    displayAssets.addAll(TitleDisplay.getAssets());
    displayAssets.addAll(MenuDisplay.getAssets());
    displayAssets.addAll(LeaderboardDisplay.getAssets());
    displayAssets.addAll(SettingsDisplay.getAssets());

    ServiceLocator.getResourceService().unloadAssets(displayAssets.toArray(new String[0]));
  }

  @Override
  protected void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();

    Entity ui = new Entity()
            .addComponent(new InputDecorator(stage, 10))
            .addComponent(new TitleDisplay())
            .addComponent(new MenuDisplay())
            .addComponent(new LeaderboardDisplay())
            .addComponent(new SettingsDisplay())
            .addComponent(new MainMenuActions(this));

    ServiceLocator.getEntityService().register(ui);
    Gdx.input.setInputProcessor(new KeyboardMenuInputComponent());
  }
}