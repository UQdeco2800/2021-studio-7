package com.deco2800.game.screens.context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.input.components.InputDecorator;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the context.
 */
public class ContextScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ContextScreen.class);
    private static final String[] ContextTextures = {
            "images/objects/bed/bed_static.PNG"
    };

    private final Renderer renderer;
    private Stage stage;
    private static int screen = 1;
    private static boolean skip = false;

    private static final String[] buttonSounds = {
            "sounds/confirm.ogg",
    };

    public ContextScreen() {

        logger.debug("Initialising Context screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();

        loadAssets();
        createUI();
        playButtonSound();
    }

    public void playButtonSound() {
        Sound sound = ServiceLocator.getResourceService().getAsset(buttonSounds[0], Sound.class);
        sound.play();
        logger.info("enter button sound played on context screen launch");
    }

    public static int getScreen() {
        return screen;
    }

    public static void incrementScreen() {
        screen++;
    }

    public static void setSkip() {
        skip = true;
    }

    public static boolean getSkip() {
        return skip;
    }

    public static void screenZero() {
        screen = 1;
    }


    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
        if (stage != null) {
            stage.draw();
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
    }

    @Override
    public void resume() {
        logger.info("Game resumed");
    }

    @Override
    public void dispose() {
        logger.debug("Disposing context screen");

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
        resourceService.loadTextures(ContextTextures);
        resourceService.loadSounds(buttonSounds);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(ContextTextures);
        resourceService.unloadAssets(buttonSounds);
    }

    /**
     * Creates the context screens ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new ContextScreenDisplay())
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new ContextScreenActions());
        ContextInputProcessor input = new ContextInputProcessor();
        ui.addComponent(input);
        Gdx.input.setInputProcessor(input);
        ServiceLocator.getEntityService().register(ui);
    }
}
