package com.deco2800.game.screens.context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
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
import com.deco2800.game.screens.context.ContextInputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the context.
 */
public class ContextScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ContextScreen.class);
    private static final String[] ContextTextures = {
            "images/context_screen/context_screen.PNG",
            "images/ui/screens/inactiveStart.png"
    };

    private final Renderer renderer;

    public ContextScreen() {

        logger.debug("Initialising Context screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();

        loadAssets();
        createUI();
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            ContextScreenDisplay.playButton();
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
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(ContextTextures);
    }

    /**
     * Creates the context screens ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new ContextScreenDisplay())
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new ContextScreenActions());
        ServiceLocator.getEntityService().register(ui);
        Gdx.input.setInputProcessor(new ContextInputProcessor());
    }
}
