package com.deco2800.game.maps.floor.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public class Bathroom extends Room {

    private BathroomInterior interior = null;

    public Bathroom(Integer maxDoorways, ObjectMap<Class<Room>, String[]> doorwayRestrictions) {
        super(maxDoorways, doorwayRestrictions);
    }

    @Override
    public void create(GridPoint2 offset, Vector2 dimensions) {
        super.create(offset, dimensions);
        interior = designateInterior(BathroomInterior.class, dimensions,
                RoomProperties.ROOM_CLASS_TO_PATH.get(this.getClass()));
    }

    public BathroomInterior getInterior() {
        return interior;
    }

    public void setInterior(BathroomInterior interior) {
        this.interior = interior;
    }

    static public class BathroomInterior extends RoomInterior {

        public BathroomInterior(ObjectMap<Character, RoomObject> tileMappings,
                                ObjectMap<Character, RoomObject> entityMappings,
                                Character[][] tileGrid, Character[][] entityGrid) {
            super(tileMappings, entityMappings, tileGrid, entityGrid);
        }
    }
}