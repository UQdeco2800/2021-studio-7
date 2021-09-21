package com.deco2800.game.areas.home.roomtypes;

import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.RoomObject;

public class Bathroom extends RoomType {

    public Bathroom(Integer maxDoorways, ObjectMap<Class<RoomType>, RoomType.DoorwayType[]> doorwayRestrictions) {
        super(maxDoorways, doorwayRestrictions);
    }

    static public class BathroomStyle extends RoomInterior {

        public BathroomStyle(ObjectMap<Character, RoomObject> tileMappings, ObjectMap<Character, RoomObject> entityMappings, Character[][] tileGrid, Character[][] entityGrid) {
            super(tileMappings, entityMappings, tileGrid, entityGrid);
        }
    }
}

