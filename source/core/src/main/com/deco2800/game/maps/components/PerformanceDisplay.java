package com.deco2800.game.maps.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveWidget;

/**
 * Displays performance stats about the game for debugging purposes.
 */
public class PerformanceDisplay extends RetroactiveWidget {
    private Label profileLabel;

    public PerformanceDisplay() {
        Z_INDEX = 5f;
    }

    @Override
    protected void addActors() {
        table = new Table();
        profileLabel = new Label(getStats(), skin, "small");
        table.add(profileLabel);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (ServiceLocator.getRenderService().getDebug().getActive()) {
            profileLabel.setVisible(true);
            profileLabel.setText(getStats());

            int screenHeight = stage.getViewport().getScreenHeight();
            float offsetX = 5f;
            float offsetY = 180f;
            profileLabel.setPosition(offsetX, screenHeight - offsetY);
        } else {
            profileLabel.setVisible(false);
        }
    }

    private String getStats() {
        String message = "Debug\n";
        message =
            message
                .concat(String.format("FPS: %d fps%n", Gdx.graphics.getFramesPerSecond()))
                .concat(String.format("RAM: %d MB%n", Gdx.app.getJavaHeap() / 1000000));
        return message;
    }
}
