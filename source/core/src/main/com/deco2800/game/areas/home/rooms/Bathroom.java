package com.deco2800.game.areas.home.rooms;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.RoomObject;

public class Bathroom extends Room {
    public Bathroom(Vector2 roomScale, RoomObject[] tileDefinitions, RoomObject[] entityDefinitions, String[][] tileGrid, String[][] entityGrid) {
        super(roomScale, tileDefinitions, entityDefinitions, tileGrid, entityGrid);
    }

    static public class BathroomStyle extends RoomStyle {

        public BathroomStyle(int maximumEntries, ObjectMap<Class<Room>, DoorwayType[]> doorwayRestrictions) {
            super(maximumEntries, doorwayRestrictions);
        }
    }
}

