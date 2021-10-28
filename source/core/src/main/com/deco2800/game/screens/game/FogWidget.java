package com.deco2800.game.screens.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.rendering.components.RenderPriority;
import com.deco2800.game.screens.RetroactiveWidget;

public class FogWidget extends RetroactiveWidget {
    private static final String FOG_TEXTURE = "images/ui/screens/fog_effect_1.png";

    public FogWidget() {
        super();
        renderPriority = RenderPriority.BACK.ordinal();
    }

    @Override
    protected void addActors() {
        table = new Table();
        table.setFillParent(true);
        Image background = new Image(ServiceLocator.getResourceService().getAsset(FOG_TEXTURE, Texture.class));
        table.setBackground(background.getDrawable());
    }

    @Override
    public void loadAssets() {
        logger.debug("    Loading fog widget assets");
        super.loadAssets();
        ServiceLocator.getResourceService().loadAsset(FOG_TEXTURE, Texture.class);
    }

    @Override
    public void unloadAssets() {
        logger.debug("    Unloading fog widget assets");
        super.unloadAssets();
        ServiceLocator.getResourceService().unloadAsset(FOG_TEXTURE);
    }
}
