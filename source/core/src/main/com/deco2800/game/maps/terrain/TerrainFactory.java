package com.deco2800.game.maps.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.ObjectData;
import com.deco2800.game.maps.ObjectDescription;

@SuppressWarnings("unused")
public class TerrainFactory {

    public static TerrainTile createTile(ObjectDescription desc, GridPoint2 worldPos) {
        ObjectData data = desc.getData();
        return new TerrainTile(
            new TextureRegion(ServiceLocator.getResourceService().getAsset(data.getAssets()[0], Texture.class)));
    }

    private TerrainFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
