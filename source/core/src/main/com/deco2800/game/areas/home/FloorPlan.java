package com.deco2800.game.areas.home;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public class FloorPlan {
    private ObjectMap<Character, RoomRestrictions> roomMappings;
    private ObjectMap<Character, String> miscMappings;
    private Character[][] floorGrid;

    public FloorPlan(ObjectMap<Character, RoomRestrictions> roomMappings,
                     ObjectMap<Character, String> miscMappings,
                     Character[][] floorGrid) {
        this.roomMappings = roomMappings;
        this.miscMappings = miscMappings;
        this.floorGrid = floorGrid;
    }

    public ObjectMap<Character, Vector2> getRoomMappings() {
        return roomMappings;
    }

    public ObjectMap<Character, String> getMiscMappings() {
        return miscMappings;
    }

    public Character[][] getFloorGrid() {
        return floorGrid;
    }

    public class RoomRestrictions {
        public Vector2 dimensions;
        public Integer numEntries;
    }
}
