package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.GridPoint2;

import java.util.HashMap;

public class MapRoom {

    public MapRoom(
            GridPoint2 dimensions,
            GridPoint2 offset,
            String[] baseTexturePaths,
            HashMap<String, String> texturePathSymbols,
            HashMap<GridPoint2, String> textureSymbolPositions
    ) {
        // Room size and position
        GridPoint2 roomSize = dimensions;
        GridPoint2 roomPos = offset;

        // Textures
        String baseTexturePathWall = baseTexturePaths[0];
        String baseTexturePathFloor = baseTexturePaths[1];

        HashMap textureSymbols = texturePathSymbols;
        HashMap texturePositions = textureSymbolPositions;
    }

    // TODO: Getters/setters
}
