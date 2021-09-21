package com.deco2800.game.areas.home.rooms;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.Room;
import com.deco2800.game.areas.home.RoomObject;
import com.deco2800.game.files.FileLoader;

public class Laundry extends Room {

    private static final String directory = "maps/laundry";
    private LaundryInterior interior;

    public Laundry(Integer maxDoorways, ObjectMap<Class<Room>, String[]> doorwayRestrictions) {
        super(maxDoorways, doorwayRestrictions);
    }

    public LaundryInterior getInterior() {
        return interior;
    }

    public void setInterior(LaundryInterior interior) {
        this.interior = interior;
    }

    public void setInterior(Vector2 dimensions) {
        this.interior = FileLoader.loadRandomRoomInterior(LaundryInterior.class, dimensions, directory);
    }

    static public class LaundryInterior extends RoomInterior {

        public LaundryInterior(ObjectMap<Character, RoomObject> tileMappings,
                               ObjectMap<Character, RoomObject> entityMappings,
                               Character[][] tileGrid, Character[][] entityGrid) {
            super(tileMappings, entityMappings, tileGrid, entityGrid);
        }
    }
}
