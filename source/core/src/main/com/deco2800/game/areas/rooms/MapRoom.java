package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a 'room' inside a 'house'. Is a discrete section of a game level's
 * map. Stores information that can be used for map generation.
 *
 * TODO add getters/setters
 * TODO need to separate walls and floor textures maybe? Could have walls be
 *  symbols specified on the perimeter
 */
public class MapRoom {
    // Room size and position
    private final GridPoint2 roomSize;
    private final GridPoint2 roomPos;

    // Textures
    private final String baseTexturePathWall;
    private final String baseTexturePathFloor;
    private HashMap textureSymbols;
    private HashMap texturePositions;

    public MapRoom(
            GridPoint2 dimensions,
            GridPoint2 offset,
            String[] baseTexturePaths,
            HashMap<String, String> texturePathSymbols,
            HashMap<GridPoint2, String> textureSymbolPositions
    ) {
        // Room size and position
        this.roomSize = dimensions;
        this.roomPos = offset;

        // Textures
        this.baseTexturePathWall = baseTexturePaths[0];
        this.baseTexturePathFloor = baseTexturePaths[1];

        this.textureSymbols = texturePathSymbols;
        this.texturePositions = textureSymbolPositions;
    }

    // TODO: Getters/setters
    public GridPoint2 getRoomSize() {
        return roomSize;
    }

    public GridPoint2 getRoomPosition() {
        return roomPos;
    }

    public String getBaseWallTexture() {
        return baseTexturePathWall;
    }

    public String getBaseFloorTexture() {
        return baseTexturePathFloor;
    }

    public HashMap<String, String> getTextureSymbols() {
        return textureSymbols;
    }

    public HashMap<GridPoint2, String> getTexturePositions() {
        return texturePositions;
    }

    public GridPoint2[] getRoomBounds() {
        /* Room position specifies the upper left corner of the room. This plus
            the size of the room gives the lower right corner of the room.
         */
        GridPoint2 c1 = getRoomPosition();
        GridPoint2 c2 = getRoomSize().add(c1);

        return new GridPoint2[]{c1, c2};
    }

    public boolean isInBounds(int x, int y) {

        GridPoint2[] bounds = getRoomBounds();

        /* Check if the position is greater than the upper left corner of the
            room, and less than the lower right corner.
         */
        if (bounds[0].x <= x && x <= bounds[1].x
                && bounds[0].y <= y && y <= bounds[1].y) {
            return true;
        }

        return false;
    }

    public boolean isInBounds(GridPoint2 pos) {
        return isInBounds(pos.x, pos.y);
    }

    public HashMap<GridPoint2, String> getSymbolsToPaths() {
        HashMap<GridPoint2, String> paths = new HashMap<>();

        getTexturePositions().forEach((pos, symbol) -> {
            paths.put(pos, getTextureSymbols().get(symbol));
        });

        return paths;
    }

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
