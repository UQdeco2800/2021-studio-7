package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.GridPoint2;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents a collection of rooms. Effectively a 'house'. Will contain all
 * details for a single level's map.
 */
public class MapLevel {
    ArrayList<MapRoom> rooms;

    public MapLevel(String mapDirectoryPath)
            throws IOException
    {
        this.rooms = RoomLoader.loadAllRooms(mapDirectoryPath);
    }

    public MapLevel(ArrayList<MapRoom> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<MapRoom> getRooms() {
        return this.rooms;
    }

    /**
     * Calculates and returns a maximum size for the map, based on the size and
     * position of the rooms.
     *
     * @return
     * The maximum width and height of the map.
     */
    public GridPoint2 getMaxSize() {
        int maxWidth = 0;
        int maxHeight = 0;

        for (MapRoom room : this.rooms) {
            GridPoint2 pos = room.getRoomPosition();
            GridPoint2 size = room.getRoomSize();

            int width = pos.x + size.x;
            int height = pos.y + size.y;

            maxWidth = Math.max(maxWidth, width);
            maxHeight = Math.max(maxHeight, height);
        }

        return new GridPoint2(maxWidth, maxHeight);
    }

    /**
     * Gets all base wall textures from the map's rooms.
     *
     * @return
     * A list of paths to each room's base wall texture.
     */
    public ArrayList<String> getBaseWallTextures() {
        ArrayList<String> walls = new ArrayList<>();

        for (MapRoom room : getRooms()) {
            walls.add(room.getBaseWallTexture());
        }

        return walls;
    }

    /**
     * Gets all base floor textures from the map's rooms.
     *
     * @return
     * A list of paths to each room's base floor texture.
     */
    public ArrayList<String> getBaseFloorTextures() {
        ArrayList<String> floors = new ArrayList<>();

        for (MapRoom room : getRooms()) {
            floors.add(room.getBaseFloorTexture());
        }

        return floors;
    }

    /**
     * Check if the given x and y coord are within the bounds of any rooms.
     *
     * @param x
     * X coordinate of the position to check
     * @param y
     * X coordinate of the position to check
     *
     * @return
     * True if the given coordinate can be found in a map's rooms. False
     * otherwise.
     */
    public boolean isInBounds(int x, int y) {
        int relX, relY;

        for (MapRoom room : getRooms()) {

            // Convert the position into a relative X and Y
            relX = x - room.getRoomPosition().x;
            relY = y - room.getRoomPosition().y;

            if (room.isInBounds(relX, relY)) return true;
        }

        return false;
    }

    /**
     * Check if a given position is within the bounds of any room.
     *
     * @param pos
     * The position to check
     *
     * @return
     * True if the given coordinate can be found in a map's rooms. False
     * otherwise.
     */
    public boolean isInBounds(GridPoint2 pos) {
        return isInBounds(pos.x, pos.y);
    }

    /**
     * Get a room based on a given x and y coordinate.
     *
     * @param x
     * x position to search for a room with.
     * @param y
     * y position to search for a room with.
     *
     * @return
     * The room object at the given coordinate, if one exists. Null otherwise.
     */
    public MapRoom findRoom(int x, int y) {
        int relX, relY;
        for (MapRoom room : getRooms()) {

            // Convert the position into a relative X and Y
            relX = x - room.getRoomPosition().x;
            relY = y - room.getRoomPosition().y;

            if (room.isInBounds(relX, relY)) return room;
        }

        return null;
    }

    /**
     * Get a room based on a given position.
     *
     * @param pos
     * The position to check for the room at.
     *
     * @return
     * The room object at the given position, if one exists. Null otherwise.
     */
    public MapRoom findRoom(GridPoint2 pos) {
        return findRoom(pos.x, pos.y);
    }
}