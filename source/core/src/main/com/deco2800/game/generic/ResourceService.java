package com.deco2800.game.generic;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Service for loading resources, e.g. textures, texture atlases, sounds, music, etc. Add new load
 * methods when new types of resources are added to the game.
 */
public class ResourceService implements Disposable {

    private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);
    private final AssetManager assetManager;

    public ResourceService() {
        this(new AssetManager());
    }

    public ResourceService(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Load an asset from a file
     *
     * @param filepath Asset path
     * @param type     Class to load into
     * @param <T>      Type of class to load into
     * @return Instance of class loaded from path
     * @see AssetManager#get(String, Class)
     */
    public <T> T getAsset(String filepath, Class<T> type) {
        return assetManager.get(filepath, type);
    }

    /**
     * Check if an asset has been loaded already
     *
     * @param filepath path of the asset
     * @param type     Class type of the asset
     * @param <T>      Type of the asset
     * @return true if asset has been loaded, false otherwise
     * @see AssetManager#contains(String)
     */
    public <T> boolean containsAsset(String filepath, Class<T> type) {
        return assetManager.contains(filepath, type);
    }

    /**
     * Returns the loading completion progress as a percentage.
     *
     * @return progress
     */
    public int getProgress() {
        return (int) (assetManager.getProgress() * 100);
    }

    /**
     * Blocking call to load all assets.
     *
     * @see AssetManager#finishLoading()
     */
    public void loadAll() {
        logger.debug("Loading all assets");
        try {
            assetManager.finishLoading();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Loads assets for the specified duration in milliseconds.
     *
     * @param duration duration to load for
     * @return finished loading
     * @see AssetManager#update(int)
     */
    public boolean loadForMillis(int duration) {
        logger.debug("Loading assets for {} ms", duration);
        try {
            return assetManager.update(duration);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return assetManager.isFinished();
    }

    /**
     * Clears all loaded assets and assets in the preloading queue.
     *
     * @see AssetManager#clear()
     */
    public void clearAllAssets() {
        logger.debug("Clearing all assets");
        assetManager.clear();
    }

    public void loadAsset(String filepath) {
        loadAsset(filepath, Objects.requireNonNull(getDefaultClass(filepath)));
    }

    public void loadAsset(String filepath, Class<?> type) {
        logger.debug("Loading {}: {}", type.getSimpleName(), filepath);
        try {
            if (!containsAsset(filepath, type)) {
                assetManager.load(filepath, type);
            }
        } catch (Exception e) {
            logger.error("Could not load {}: {}", type.getSimpleName(), filepath);
        }
    }

    public void loadAssets(String[] filePaths) {
        for (String filepath : filePaths) {
            loadAsset(filepath);
        }
    }

    public void loadAssets(String[] filePaths, Class<?> type) {
        for (String filepath : filePaths) {
            loadAsset(filepath, type);
        }
    }

    public void unloadAsset(String filepath) {
        logger.debug("Unloading {}", filepath);
        try {
            if (containsAsset(filepath, getDefaultClass(filepath))) {
                assetManager.unload(filepath);
            }
        } catch (Exception e) {
            if (!filepath.equals("")) {
                logger.error("Could not unload {}", filepath);
            }
        }
    }

    public void unloadAssets(String[] filePaths) {
        for (String filepath : filePaths) {
            unloadAsset(filepath);
        }
    }

    public static Class<?> getDefaultClass(String filepath) {
        if (filepath.endsWith(".png")) {
            return Texture.class;
        } else if (filepath.endsWith(".atlas")) {
            return TextureAtlas.class;
        } else if (filepath.endsWith(".mp3")) {
            return Music.class;
        } else if (filepath.endsWith(".ogg")) {
            return Sound.class;
        }
        return null;
    }

    @Override
    public void dispose() {
        clearAllAssets();
    }
}
