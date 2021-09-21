package com.deco2800.game.areas.home.rooms;

import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.Room;
import com.deco2800.game.areas.home.RoomObject;

public class Kitchen extends Room {

    private KitchenInterior interior;

    public Kitchen(Integer maxDoorways, ObjectMap<Class<Room>, String[]> doorwayRestrictions) {
        super(maxDoorways, doorwayRestrictions);
    }

    public KitchenInterior getInterior() {
        return interior;
    }

    public void setInterior(KitchenInterior interior) {
        this.interior = interior;
    }

    static public class KitchenInterior extends RoomInterior {

        public KitchenInterior(ObjectMap<Character, RoomObject> tileMappings,
                              ObjectMap<Character, RoomObject> entityMappings,
                              Character[][] tileGrid, Character[][] entityGrid) {
            super(tileMappings, entityMappings, tileGrid, entityGrid);
        }
    }
}
