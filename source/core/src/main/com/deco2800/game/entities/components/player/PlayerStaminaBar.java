package com.deco2800.game.entities.components.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Player stamina bar for drawing visual representation of player stamina
 */
public class PlayerStaminaBar extends ProgressBar {

    public PlayerStaminaBar(int width, int height) {
        // sets range of stamina bar value and the step size
        super(0f, 100f, 1f, false, new ProgressBarStyle());
        // set background of stamina bar
        getStyle().background = new TextureRegionDrawable(new TextureRegion(new Texture("images/ui/elements/progressframe.png")));

        //change between progress-blue and progress-pink based on group opinion
        getStyle().knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture("images/ui/elements/progress-pink.png")));

        setWidth(width);
        setHeight(height);

        setAnimateDuration(0f);
        setValue(1f);

        setAnimateDuration(0.01f);
    }

    /**
     * Draws images with specified width, height and color.
     * @param width width of image
     * @param height height of image
     * @param color color of image
     * @return Drawable image of specified size and color.
     */
    public static Drawable getDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        pixmap.dispose();

        return drawable;
    }
}
