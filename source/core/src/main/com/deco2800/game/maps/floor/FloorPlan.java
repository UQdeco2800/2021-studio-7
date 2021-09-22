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

    private RoomObject defaultTileObject;
    private ObjectMap<Character, RoomPlan> roomMappings;
    private ObjectMap<Character, String> miscMappings;
    private Character[][] floorGrid;
    private Vector2 homeDimensions;

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

        private GridPoint2 offset;
        private Vector2 dimensions;
        private Integer numDoorways;
        private Room room;

        public void create() {
            room = designateRoom();
        }

        public Room designateRoom() {
            String size = IntUtils.intToStrLetters((int) dimensions.x) +
                    "_by_" + IntUtils.intToStrLetters((int) dimensions.y);
            Array<Class<? extends Room>> classes = RoomProperties.ROOM_CLASS_TO_PATH.keys().toArray();

            Room randomRoom;
            do {
                Class<? extends Room> randomRoomClass = classes.get(RandomUtils.getSeed().nextInt(classes.size));
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