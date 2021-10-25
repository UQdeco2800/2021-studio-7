package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.deco2800.game.GdxGame;

public class RetroactiveScreen extends ScreenAdapter {
    protected final GdxGame game;
    protected GdxGame.ScreenType nextScreen = null;

    public RetroactiveScreen(GdxGame game) {
        this.game = game;
    }

    public void queueNextScreen(GdxGame.ScreenType screenType) {
        nextScreen = screenType;
    }

    protected void loadAssets() {
        // No assets loaded by default
    }

    protected void unloadAssets() {
        // No assets to unload by default
    }

    protected void createUI() {
        // Entity for intra-screen communication between components
    }
}
