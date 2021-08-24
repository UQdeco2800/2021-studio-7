package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.GridPoint2;

import java.util.HashMap;

/**
 * Represents a 'room' inside a 'house'. Is a discrete section of a game level's
 * map. Stores information that can be used for map generation.
 *
 * TODO add getters/setters
 */
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
