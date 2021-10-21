package com.deco2800.game.screens.title;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.input.components.InputComponent;
import com.deco2800.game.input.components.InputDecorator;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the title.
 */
public class TitleScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(TitleScreen.class);

    private final Renderer renderer;
    private static final String[] TitleTextures = {
            "images/ui/screens/inactiveStart.png",
            "images/ui/title/RETROACTIVE-large.png"
    };
    //add background music into the game
    private static final String[] backgroundMusic = {"sounds/backgroundMusic" +
            "-EP.mp3"};

    public TitleScreen() {

        logger.debug("Initialising title screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();


        loadAssets();
        createUI();
        playMusic();
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
        logger.debug("Disposing title screen");

        renderer.dispose();
        unloadAssets();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();
    }

    /**
     * Play the background Music
     */
    private void playMusic() {
        Music music =
                ServiceLocator.getResourceService().getAsset(backgroundMusic[0],
                        Music.class);
        music.setLooping(true);
        music.setVolume(0.05f);
        music.play();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(TitleTextures);
        resourceService.loadMusic(backgroundMusic);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(TitleTextures);
        resourceService.unloadAssets(backgroundMusic);
    }

    /**
     * Creates the title ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForTitle();

        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new TitleScreenDisplay())
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(inputComponent)
                .addComponent(new TitleScreenActions());
        ServiceLocator.getEntityService().register(ui);
    }
}
