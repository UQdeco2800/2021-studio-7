package com.deco2800.game.screens.settingsmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.components.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.generic.GameTime;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.mainmenu.MainMenuDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The game screen containing the settings. */
public class SettingsScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(SettingsScreen.class);

  private final Renderer renderer;

  private static final String[] buttonSounds = {
          "sounds/confirm.ogg",
  };

  public SettingsScreen() {
    logger.debug("Initialising settings screen services");
    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    ServiceLocator.registerTimeSource(new GameTime());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(5f, 5f);

    loadAssets();
    createUI();
    playButtonSound();
  }

  public static void playButtonSound() {
    Sound sound = ServiceLocator.getResourceService().getAsset(buttonSounds[0], Sound.class);
    sound.play();
    logger.info("enter button sound played on settings screen");
  }

  @Override
  public void render(float delta) {
    ServiceLocator.getEntityService().update();
    renderer.render();

    if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
      SettingsMenuDisplay.exitSettingsMenu();
    }
    if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
      SettingsMenuDisplay.applySettings();
    }
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
  }

  @Override
  public void dispose() {
    renderer.dispose();
    unloadAssets();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getEntityService().dispose();

    ServiceLocator.clear();
  }

  /**
   * Creates the setting screen's ui including components for rendering ui elements to the screen
   * and capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    Entity ui = new Entity();
    ui.addComponent(new SettingsMenuDisplay())
            .addComponent(new InputDecorator(stage, 10));
    ServiceLocator.getEntityService().register(ui);
    //Gdx.input.setInputProcessor(new SettingsInputProcessor());
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadSounds(buttonSounds);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(buttonSounds);
  }
}
