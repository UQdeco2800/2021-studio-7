package com.deco2800.game.screens.endgame;

import com.badlogic.gdx.Gdx;
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
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The win/lose screen at the end of the game.
 */
public class EndGameScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(EndGameScreen.class);
    private static final String[] winScreenTextures = {"images/ui/screens/win_screen.png"};
    private static final String[] loseScreenTextures = {"images/ui/screens/lose_screen.png"};
    private static final String[] timeoutScreenTextures = {"images/ui/screens/time_out.png"};
    private final String[] activeScreenTextures;
    private final GdxGame.ScreenType result;

    private final Renderer renderer;

    private static final String[] buttonSounds = {
            "sounds/confirm.ogg",
            "sounds/browse-short.ogg"
    };

    public EndGameScreen(GdxGame.ScreenType result) {
        this.result = result;
        switch (this.result) {
            case WIN_DEFAULT:
                this.activeScreenTextures = winScreenTextures;
                break;
            case LOSS_TIMED:
                this.activeScreenTextures = timeoutScreenTextures;
                break;
            case LOSS_CAUGHT:
            default:
                this.activeScreenTextures = loseScreenTextures;
        }

        logger.debug("Initialising end game screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();

        loadAssets();
        createUI();

        playButtonSound("enter");
    }

    public static void playButtonSound(String button) {
        Sound sound;
        if (button.equals("enter")) {
            sound = ServiceLocator.getResourceService().getAsset(buttonSounds[0], Sound.class);
        } else {
            sound = ServiceLocator.getResourceService().getAsset(buttonSounds[1], Sound.class);
        }

        sound.play();
        logger.info("{} button sound played on end game screen", button);
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
        logger.debug("Disposing end game screen");

        renderer.dispose();
        unloadAssets();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();
    }

    public GdxGame.ScreenType getResult() {
        return this.result;
    }

    public String[] getActiveScreenTextures() {
        return this.activeScreenTextures;
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(activeScreenTextures);
        resourceService.loadSounds(buttonSounds);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(activeScreenTextures);
        resourceService.unloadAssets(buttonSounds);
    }

    /**
     * Creates the end game's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new EndGameDisplay(this))
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new EndGameActions());
        ServiceLocator.getEntityService().register(ui);
        Gdx.input.setInputProcessor(new EndGameInputProcessor());
    }
}
