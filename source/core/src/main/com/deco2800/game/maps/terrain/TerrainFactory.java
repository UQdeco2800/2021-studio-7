package com.deco2800.game.maps.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.ObjectData;

@SuppressWarnings("unused")
public class TerrainFactory {

    public static TerrainTile createTile(ObjectData data, int numRotations, GridPoint2 worldPos) {
        return new TerrainTile(
            new TextureRegion(ServiceLocator.getResourceService().getAsset(data.getAssets()[0], Texture.class)));
    }

    private TerrainFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
