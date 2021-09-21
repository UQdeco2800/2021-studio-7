package com.deco2800.game.areas.home.rooms;

import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.Room;
import com.deco2800.game.areas.home.RoomObject;

public class Dining extends Room {

    private DiningInterior interior;

    public Dining(Integer maxDoorways, ObjectMap<Class<Room>, String[]> doorwayRestrictions) {
        super(maxDoorways, doorwayRestrictions);
    }

    public DiningInterior getInterior() {
        return interior;
    }

    public void setInterior(DiningInterior interior) {
        this.interior = interior;
    }

    static public class DiningInterior extends RoomInterior {

        public DiningInterior(ObjectMap<Character, RoomObject> tileMappings,
                               ObjectMap<Character, RoomObject> entityMappings,
                               Character[][] tileGrid, Character[][] entityGrid) {
            super(tileMappings, entityMappings, tileGrid, entityGrid);
        }
    }
}
