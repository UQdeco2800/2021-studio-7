package com.deco2800.game.maps.floor;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.maps.floor.rooms.RoomProperties;
import com.deco2800.game.maps.floor.rooms.Room;
import com.deco2800.game.maps.floor.rooms.RoomObject;
import com.deco2800.game.utils.math.IntUtils;
import com.deco2800.game.utils.math.RandomUtils;

public class FloorPlan {

    private final RoomObject defaultTileObject;
    private final ObjectMap<Character, RoomPlan> roomMappings;
    private final ObjectMap<Character, String> miscMappings;
    private final Character[][] floorGrid;
    private final Vector2 homeDimensions;

    public FloorPlan(RoomObject defaultTileObject,
                     ObjectMap<Character, RoomPlan> roomMappings,
                     ObjectMap<Character, String> miscMappings,
                     Character[][] floorGrid) {
        this.defaultTileObject = defaultTileObject;
        this.roomMappings = roomMappings;
        this.miscMappings = miscMappings;
        this.floorGrid = floorGrid;
        this.homeDimensions = new Vector2(floorGrid.length, floorGrid[0].length);
    }

    public void create() {
        designateRooms();
    }

    public void designateRooms() {
        for (RoomPlan roomFloorPlan : roomMappings.values()) {
            roomFloorPlan.create();
        }
    }

    public RoomObject getDefaultTileObject() {
        return defaultTileObject;
    }

    public ObjectMap<Character, RoomPlan> getRoomMappings() {
        return roomMappings;
    }

    public ObjectMap<Character, String> getMiscMappings() {
        return miscMappings;
    }

    public Character[][] getFloorGrid() {
        return floorGrid;
    }

    public Vector2 getHomeDimensions() {
        return homeDimensions;
    }

    static public class RoomPlan {

        private final GridPoint2 offset;
        private final Vector2 dimensions;
        private final Integer numDoorways;
        private Room room;

        public RoomPlan(GridPoint2 offset, Vector2 dimensions, Integer numDoorways) {
            this.offset = offset;
            this.dimensions = dimensions;
            this.numDoorways = numDoorways;
        }

        public void create() {
            room = designateRoom();
        }

        public Room designateRoom() {
            String size = IntUtils.intToStrLetters((int) dimensions.x) +
                    "_by_" + IntUtils.intToStrLetters((int) dimensions.y);
            Array<Class<? extends Room>> classes = RoomProperties.ROOM_CLASS_TO_PATH.keys().toArray();

            Room randomRoom;
            do {
                Class<? extends Room> randomRoomClass = classes.get(RandomUtils.getSeed().nextInt() % classes.size);
                randomRoom = RoomProperties.get(randomRoomClass, size);
                classes.removeValue(randomRoomClass, true);

                if (randomRoom == null || numDoorways > randomRoom.getMaxDoorways()) {
                    randomRoom = null;
                    continue;
                }
                randomRoom.create(offset, dimensions);
                if (randomRoom.getInterior() == null) {
                    randomRoom = null;
                }
            } while (randomRoom == null && classes.size < 0);

            return randomRoom;
        }

        public GridPoint2 getOffset() {
            return offset;
        }

        public Room getRoom() {
            return room;
        }

        public void setRoom(Room room) {
            this.room = room;
        }
    }
}