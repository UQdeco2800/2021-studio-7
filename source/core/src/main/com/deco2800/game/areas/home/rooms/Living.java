package com.deco2800.game.areas.home.rooms;

import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.Room;
import com.deco2800.game.areas.home.RoomObject;

public class Living extends Room {

    private LivingInterior interior;

    public Living(Integer maxDoorways, ObjectMap<Class<Room>, String[]> doorwayRestrictions) {
        super(maxDoorways, doorwayRestrictions);
    }

    public LivingInterior getInterior() {
        return interior;
    }

    public void setInterior(LivingInterior interior) {
        this.interior = interior;
    }

    static public class LivingInterior extends RoomInterior {

        public LivingInterior(ObjectMap<Character, RoomObject> tileMappings,
                               ObjectMap<Character, RoomObject> entityMappings,
                               Character[][] tileGrid, Character[][] entityGrid) {
            super(tileMappings, entityMappings, tileGrid, entityGrid);
        }
    }
}
