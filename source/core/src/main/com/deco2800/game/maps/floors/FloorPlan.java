package com.deco2800.game.maps.floors;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.rooms.RoomProperties;
import com.deco2800.game.maps.rooms.Room;
import com.deco2800.game.maps.rooms.RoomObject;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.IntUtils;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.utils.math.Vector2Utils;

public class FloorPlan implements Json.Serializable {

    private RoomObject defaultTileObject;
    private ObjectMap<Character, RoomPlan> roomMappings;
    private ObjectMap<Character, String> miscMappings;
    private Character[][] floorGrid;
    private boolean created = false;

    public void create() {
        if (created) {
            designateRooms();
        }
        created = true;
    }

    public void designateRooms() {
        for (RoomPlan roomFloorPlan : new ObjectMap.Values<>(roomMappings)) {
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
        return new Vector2(floorGrid[0].length, floorGrid.length);
    }

    @Override
    public void write(Json json) {
        json.writeObjectStart();
        json.writeValue("defaultTileObject", defaultTileObject);
        json.writeValue("roomMappings", roomMappings);
        json.writeValue("miscMappings", miscMappings);
        json.writeValue("floorGrid", floorGrid);
        json.writeObjectEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        try {
            jsonData = jsonData.child();
            defaultTileObject = new RoomObject();
            defaultTileObject.read(json, jsonData);

            jsonData = jsonData.next();
            JsonValue iterator = jsonData.child();
            roomMappings = new ObjectMap<>();
            do {
                RoomPlan roomPlan = new RoomPlan();
                roomPlan.read(json, iterator);
                roomMappings.put(iterator.name().charAt(0), roomPlan);
                iterator = iterator.next();
            } while (!iterator.isNull());

            jsonData = jsonData.next();
            iterator = jsonData.child();
            miscMappings = new ObjectMap<>();
            do {
                miscMappings.put(iterator.name().charAt(0), iterator.asString());
                iterator = iterator.next();
            } while (!iterator.isNull());

            jsonData = jsonData.next();
            floorGrid = new Character[jsonData.size][jsonData.child().size];
            iterator = jsonData.child();
            for (int x = 0; x < floorGrid[0].length; x++) {
                JsonValue cellIterator = iterator.child();
                for (int y = 0; y < floorGrid.length; y++) {
                    floorGrid[x][y] = cellIterator.name().charAt(0);
                    cellIterator = cellIterator.next;
                }
                iterator = iterator.next;
            }

            jsonData = jsonData.next();
            assert jsonData.isNull();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Error reading floor plan at value " + jsonData.name());
        }
    }

    static public class RoomPlan implements Json.Serializable {

        private GridPoint2 offset;
        private Vector2 dimensions;
        private Integer numDoorways;
        private Room room;
        private boolean created = false;

        public void create() {
            if (created) {
                room = designateRoom();
            }
            created = true;
        }

        public Room designateRoom() {
            String size = IntUtils.intToStrLetters((int) dimensions.x) +
                    "_by_" + IntUtils.intToStrLetters((int) dimensions.y);
            Array<Class<? extends Room>> classes = (new ObjectMap.Keys<>(RoomProperties.ROOM_CLASS_TO_PATH)).toArray();

            Room randomRoom;
            do {
                Class<? extends Room> randomRoomClass = classes.get(RandomUtils.getSeed().nextInt(classes.size));
                randomRoom = ServiceLocator.getRoomProperties().get(randomRoomClass, size);
                classes.removeValue(randomRoomClass, true);

                if (randomRoom == null || numDoorways > randomRoom.getMaxDoorways()) {
                    randomRoom = null;
                    continue;
                }
                randomRoom.create(offset, dimensions);
                if (randomRoom.getInterior() == null) {
                    randomRoom = null;
                }
            } while (randomRoom == null && classes.size > 0);

            return randomRoom;
        }

        public GridPoint2 getOffset() {
            return offset;
        }

        public Room getRoom() {
            return room;
        }

        @Override
        public void write(Json json) {
            json.writeObjectStart();
            json.writeValue("offset", offset);
            json.writeValue("dimensions", dimensions);
            json.writeValue("numDoorways", numDoorways);
            json.writeValue("room", room);
            json.writeObjectEnd();
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            try {
                jsonData = jsonData.child();
                offset = new GridPoint2();
                offset = GridPoint2Utils.read(jsonData);

                jsonData = jsonData.next();
                dimensions = new Vector2();
                dimensions = Vector2Utils.read(jsonData);

                jsonData = jsonData.next();
                numDoorways = IntUtils.strDigitsToInt(jsonData.name());

                jsonData = jsonData.next();
                if (!jsonData.isNull()) {
                    room = new Room();
                    room.read(json, jsonData);
                    jsonData = jsonData.next();
                } else {
                    room = null;
                }

                assert jsonData.isNull();
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("Error reading room plan from value " + jsonData.name());
            }
        }
    }
}