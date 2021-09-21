package com.deco2800.game.areas.home.rooms;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.Room;
import com.deco2800.game.areas.home.RoomObject;
import com.deco2800.game.files.FileLoader;

public class Garage extends Room {

    private static final String directory = "maps/garage";
    private GarageInterior interior;

    public Garage(Integer maxDoorways, ObjectMap<Class<Room>, String[]> doorwayRestrictions) {
        super(maxDoorways, doorwayRestrictions);
    }

    public GarageInterior getInterior() {
        return interior;
    }

    public void setInterior(GarageInterior interior) {
        this.interior = interior;
    }

    public void setInterior(Vector2 dimensions) {
        this.interior = FileLoader.loadRandomRoomInterior(GarageInterior.class, dimensions, directory);
    }

    static public class GarageInterior extends RoomInterior {

        public GarageInterior(ObjectMap<Character, RoomObject> tileMappings,
                                  ObjectMap<Character, RoomObject> entityMappings,
                                  Character[][] tileGrid, Character[][] entityGrid) {
            super(tileMappings, entityMappings, tileGrid, entityGrid);
        }
    }
}
