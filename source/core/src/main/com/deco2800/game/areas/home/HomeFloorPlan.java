package com.deco2800.game.areas.home;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.roomtypes.RoomType;

public class HomeFloorPlan {
    private final ObjectMap<Character, RoomFloorPlan> roomMappings;
    private final ObjectMap<Character, String> miscMappings;
    private final Character[][] floorGrid;

    public HomeFloorPlan(ObjectMap<Character, RoomFloorPlan> roomMappings,
                         ObjectMap<Character, String> miscMappings,
                         Character[][] floorGrid) {
        this.roomMappings = roomMappings;
        this.miscMappings = miscMappings;
        this.floorGrid = floorGrid;
    }

    public ObjectMap<Character, RoomFloorPlan> getRoomMappings() {
        return roomMappings;
    }

    public ObjectMap<Character, String> getMiscMappings() {
        return miscMappings;
    }

    public Character[][] getFloorGrid() {
        return floorGrid;
    }

    static public class RoomFloorPlan {

        private final GridPoint2 offset;
        private final Vector2 dimensions;
        private final Integer numDoorways;
        private RoomType roomType;

        public RoomFloorPlan(GridPoint2 offset, Vector2 dimensions, Integer numDoorways,
                    RoomType roomType) {
            this.offset = offset;
            this.dimensions = dimensions;
            this.numDoorways = numDoorways;
            this.roomType = roomType;
        }

        public GridPoint2 getOffset() {
            return offset;
        }

        public Vector2 getDimensions() {
            return dimensions;
        }

        public Integer getNumDoorways() {
            return numDoorways;
        }

        public RoomType getRoom() {
            return roomType;
        }

        public void setRoom(RoomType roomType) {
            this.roomType = roomType;
        }
    }
}