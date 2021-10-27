package com.deco2800.game.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.Loadable;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RetroactiveActions extends Component implements Loadable {
    protected static final Logger logger = LoggerFactory.getLogger(RetroactiveActions.class);
    private static final String[] BACKGROUND_MUSICS = {
        "sounds/backgroundMusic-EP.mp3",
        "sounds/backgroundMusic-MG.mp3",
        "sounds/8bit_game_win_sounds.mp3",
        "sounds/lose-caught.mp3",
        "sounds/time_up.mp3"
    };
    protected static final String[] BUTTON_SOUNDS = {
        "sounds/confirm.ogg",
        "sounds/browse-short.ogg"
    };

    public RetroactiveActions() {
        creationPriority = 50;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("play_music", this::playMusic);
        entity.getEvents().addListener("play_sound", this::playSound);
    }

    protected void playMusic(String name) {
        ResourceService service = ServiceLocator.getResourceService();
        Music music = null;

        if (name.equals("menu")) {
            music = service.getAsset(BACKGROUND_MUSICS[0], Music.class);
        } else if (name.equals("game")) {
            music = service.getAsset(BACKGROUND_MUSICS[1], Music.class);
        } else if (name.equals("win")) {
            music = service.getAsset(BACKGROUND_MUSICS[2], Music.class);
        } else if (name.equals("caught")) {
            music = service.getAsset(BACKGROUND_MUSICS[3], Music.class);
        } else if (name.equals("timeout")) {
            music = service.getAsset(BACKGROUND_MUSICS[4], Music.class);
        } else if (ServiceLocator.getResourceService().containsAsset(name, Music.class)) {
            music = service.getAsset(name, Music.class);
        }

        if (music != null) {
            music.setLooping(true);
            music.setVolume(0.05f);
            music.play();
        }
    }

    protected void playSound(String name) {
        ResourceService service = ServiceLocator.getResourceService();
        Sound sound = null;

        if (name.equals("confirm")) {
            sound = service.getAsset(BUTTON_SOUNDS[0], Sound.class);
        } else if (name.equals("browse")) {
            sound = service.getAsset(BUTTON_SOUNDS[1], Sound.class);
        } else if (ServiceLocator.getResourceService().containsAsset(name, Sound.class)) {
            sound = service.getAsset(name, Sound.class);
        }

        if (sound != null) {
            sound.play(0.05f);
        }
    }

    @Override
    public void loadAssets() {
        ServiceLocator.getResourceService().loadAssets(BACKGROUND_MUSICS, Music.class);
        ServiceLocator.getResourceService().loadAssets(BUTTON_SOUNDS, Sound.class);
    }

    @Override
    public void unloadAssets() {
        ServiceLocator.getResourceService().unloadAssets(BACKGROUND_MUSICS);
        ServiceLocator.getResourceService().unloadAssets(BUTTON_SOUNDS);
    }
}