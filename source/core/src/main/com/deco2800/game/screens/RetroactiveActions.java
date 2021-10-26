package com.deco2800.game.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RetroactiveActions extends Component {
    protected static final Logger logger = LoggerFactory.getLogger(RetroactiveActions.class);
    protected static final String[] BUTTON_SOUNDS = {"sounds/confirm.ogg", "sounds/browse-short.ogg"};
    protected String backgroundMusic = null;

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("play_music", this::playMusic);
        entity.getEvents().addListener("play_sound", this::playSound);
    }

    protected void playMusic() {
        if (backgroundMusic != null) {
            Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
            music.setLooping(true);
            music.setVolume(0.05f);
            music.play();
        }
    }

    protected void playSound(String name) {
        if (name.equals("confirm")) {
            logger.info("enter button sound played on main game screen launch");
            ServiceLocator.getResourceService().getAsset(BUTTON_SOUNDS[0], Sound.class).play();
        } else if (name.equals("browse")){
            logger.info("scrolling button sound played on main game screen launch");
            ServiceLocator.getResourceService().getAsset(BUTTON_SOUNDS[1], Sound.class).play();
        }
    }

    public void loadAssets() {
        ResourceService service = ServiceLocator.getResourceService();
        if (backgroundMusic != null && !service.containsAsset(backgroundMusic, Music.class)) {
            service.loadMusic(backgroundMusic);
        }
        for (String asset : BUTTON_SOUNDS) {
            if (!service.containsAsset(asset, Sound.class)) {
                service.loadSound(asset);
            }
        }
    }

    public void unloadAssets() {
        ResourceService service = ServiceLocator.getResourceService();
        if (backgroundMusic != null && service.containsAsset(backgroundMusic, Music.class)) {
            service.unloadAsset(backgroundMusic);
        }
        for (String asset : BUTTON_SOUNDS) {
            if (service.containsAsset(asset, Sound.class)) {
                service.unloadAsset(asset);
            }
        }
    }
}