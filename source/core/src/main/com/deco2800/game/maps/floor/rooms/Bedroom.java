package com.deco2800.game.maps.floor.rooms;

import com.badlogic.gdx.utils.ObjectMap;

public class Bedroom extends Room {

    private BedroomInterior interior;

    public Bedroom(Integer maxDoorways, ObjectMap<Class<Room>, String[]> doorwayRestrictions) {
        super(maxDoorways, doorwayRestrictions);
    }

    public BedroomInterior getInterior() {
        return interior;
    }

    public void setInterior(BedroomInterior interior) {
        this.interior = interior;
    }

    static public class BedroomInterior extends RoomInterior {

        public BedroomInterior(ObjectMap<Character, RoomObject> tileMappings,
                                ObjectMap<Character, RoomObject> entityMappings,
                                Character[][] tileGrid, Character[][] entityGrid) {
            super(tileMappings, entityMappings, tileGrid, entityGrid);
        }
    }
}
