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
    // Room size and position
    GridPoint2 roomSize;
    GridPoint2 roomPos;

    // Textures
    String baseTexturePathWall;
    String baseTexturePathFloor;
    HashMap textureSymbols;
    HashMap texturePositions;

    public MapRoom(
            GridPoint2 dimensions,
            GridPoint2 offset,
            String[] baseTexturePaths,
            HashMap<String, String> texturePathSymbols,
            HashMap<GridPoint2, String> textureSymbolPositions
    ) {
        // Room size and position
        roomSize = dimensions;
        roomPos = offset;

        // Textures
        baseTexturePathWall = baseTexturePaths[0];
        baseTexturePathFloor = baseTexturePaths[1];

        textureSymbols = texturePathSymbols;
        texturePositions = textureSymbolPositions;
    }

    // TODO: Getters/setters

    /**
     * Convert the map room object into a string for textual representation. The
     * string representation is the room's dimensions, then offset, then the
     * number of textures defined for that room, including the two base
     * textures.
     *
     * E.g. A 2 by 4 room at (0, 0) with 2 defined textures:
     *  "(2, 4); (0, 0); Textures: 2"
     *
     * @return
     * A string representation of the map room objects.
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        String sep = "; ";

        sb.append(roomSize.toString()).append(sep);
        sb.append(roomPos.toString()).append(sep);

        sb.append("Textures: ").append(textureSymbols.size() + 1);

        return sb.toString();
    }
}
