package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.GridPoint2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class MapLevel {
    ArrayList<MapRoom> rooms;

    public MapLevel(String mapDirectoryPath)
            throws IOException, FileNotFoundException
    {
        this.rooms = RoomLoader.loadAllRooms(mapDirectoryPath);
    }

    public ArrayList<MapRoom> getRooms() {
        return this.rooms;
    }

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

    public ArrayList<String> getBaseWallTextures() {
        ArrayList<String> walls = new ArrayList<>();

        for (MapRoom room : getRooms()) {
            walls.add(room.getBaseWallTexture());
        }

        return walls;
    }

    public ArrayList<String> getBaseFloorTextures() {
        ArrayList<String> floors = new ArrayList<>();

        for (MapRoom room : getRooms()) {
            floors.add(room.getBaseFloorTexture());
        }

        return floors;
    }

    public ArrayList<String> getAllTexturePaths() {
        ArrayList<String> texturePaths = new ArrayList<>();

        // Add base textures
        texturePaths.addAll(getBaseWallTextures());
        texturePaths.addAll(getBaseFloorTextures());

        // Add all other textures
        for (MapRoom room : getRooms()) {
            room.getTextureSymbols().forEach((sym, texture) -> {
                texturePaths.add(texture);
            });
        }

        return texturePaths;
    }

    public boolean isInBounds(int x, int y) {
        for (MapRoom room : getRooms()) {
            if (room.isInBounds(x, y)) return true;
        }

        return false;
    }

    public boolean isInBounds(GridPoint2 pos) {
        return isInBounds(pos.x, pos.y);
    }

    public MapRoom findRoom(int x, int y) {
        for (MapRoom room : getRooms()) {
            if (room.isInBounds(x, y)) return room;
        }

        return null;
    }

    public MapRoom findRoom(GridPoint2 pos) {
        return findRoom(pos.x, pos.y);
    }
}