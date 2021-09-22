package com.deco2800.game.maps.floor.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public class Garage extends Room {

    private GarageInterior interior;

    public Garage(Integer maxDoorways, ObjectMap<Class<Room>, String[]> doorwayRestrictions) {
        super(maxDoorways, doorwayRestrictions);
    }

    @Override
    public void create(GridPoint2 offset, Vector2 dimensions) {
        super.create(offset, dimensions);
        interior = designateInterior(GarageInterior.class, dimensions,
                RoomProperties.ROOM_CLASS_TO_PATH.get(this.getClass()));
    }

    public GarageInterior getInterior() {
        return interior;
    }

    public void setInterior(GarageInterior interior) {
        this.interior = interior;
    }

    static public class GarageInterior extends RoomInterior {

        public GarageInterior(ObjectMap<Character, RoomObject> tileMappings,
                                  ObjectMap<Character, RoomObject> entityMappings,
                                  Character[][] tileGrid, Character[][] entityGrid) {
            super(tileMappings, entityMappings, tileGrid, entityGrid);
        }
    }
}
