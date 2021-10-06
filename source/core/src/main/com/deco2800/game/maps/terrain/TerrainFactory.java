package com.deco2800.game.maps.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;

@SuppressWarnings({"unused", "UnnecessaryLocalVariable"})
public class TerrainFactory {

    public static TerrainTile createTile(String[] assets) {
        TerrainTile tile = createBaseTile(assets);
        return tile;
    }
    
    public static TerrainTile createBaseTile(String[] assets) {
        TerrainTile baseTile = null;
        ResourceService resourceService = ServiceLocator.getResourceService();
        if (assets[0].endsWith(".png")) {
            baseTile = new TerrainTile(new TextureRegion(
                    resourceService.getAsset(assets[0], Texture.class)));
        }
        return baseTile;
    }
}
