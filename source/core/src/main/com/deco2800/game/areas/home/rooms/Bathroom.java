package com.deco2800.game.areas.home.rooms;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.Room;
import com.deco2800.game.areas.home.RoomObject;
import com.deco2800.game.files.FileLoader;

public class Bathroom extends Room {

    private static final String directory = "maps/bathroom";
    private BathroomInterior interior = null;

    public Bathroom(Integer maxDoorways, ObjectMap<Class<Room>, String[]> doorwayRestrictions) {
        super(maxDoorways, doorwayRestrictions);
    }

    public BathroomInterior getInterior() {
        return interior;
    }

    public void setInterior(BathroomInterior interior) {
        this.interior = interior;
    }

    public void setInterior(Vector2 dimensions) {
        this.interior = FileLoader.loadRandomRoomInterior(BathroomInterior.class, dimensions, directory);
    }

    static public class BathroomInterior extends RoomInterior {

        public BathroomInterior(ObjectMap<Character, RoomObject> tileMappings,
                                ObjectMap<Character, RoomObject> entityMappings,
                                Character[][] tileGrid, Character[][] entityGrid) {
            super(tileMappings, entityMappings, tileGrid, entityGrid);
        }
    }
}