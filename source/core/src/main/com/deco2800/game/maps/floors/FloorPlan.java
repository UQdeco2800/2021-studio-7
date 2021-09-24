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
        JsonValue iterator = jsonData.child();
        try {
            assert iterator.name().equals("defaultTileObject");
            defaultTileObject = new RoomObject();
            defaultTileObject.read(json, iterator);

            iterator = iterator.next();
            assert iterator.name().equals("roomMappings");
            roomMappings = new ObjectMap<>();
            JsonValue roomIterator = jsonData.child();
            do {
                assert roomIterator.name().length() == 1;
                RoomPlan roomPlan = new RoomPlan();
                roomPlan.read(json, roomIterator);
                roomMappings.put(roomIterator.name().charAt(0), roomPlan);
                roomIterator = roomIterator.next();
            } while (roomIterator != null);

            iterator = iterator.next();
            assert iterator.name().equals("miscMappings");
            miscMappings = new ObjectMap<>();
            JsonValue miscIterator = iterator.child();
            do {
                assert miscIterator.name().length() == 1;
                miscMappings.put(miscIterator.name().charAt(0), miscIterator.asString());
                miscIterator = miscIterator.next();
            } while (miscIterator != null);

            iterator = iterator.next();
            assert iterator.name().equals("floorGrid");
            floorGrid = new Character[iterator.size][iterator.child().size];
            JsonValue rowIterator = jsonData.child();
            for (int x = 0; x < floorGrid[0].length; x++) {
                JsonValue cellIterator = rowIterator.child();
                for (int y = 0; y < floorGrid.length; y++) {
                    assert cellIterator.name().length() == 1;
                    floorGrid[x][y] = cellIterator.name().charAt(0);
                    cellIterator = cellIterator.next();
                }
                rowIterator = rowIterator.next();
                assert cellIterator == null;
            }
            assert rowIterator == null;

            assert iterator.next() == null;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Error reading floor plan at value " + iterator.name());
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
            JsonValue iterator = jsonData.child();
            try {
                assert iterator.name().equals("offset");
                offset = new GridPoint2();
                offset = GridPoint2Utils.read(iterator);

                iterator = iterator.next();
                assert iterator.name().equals("dimensions");
                dimensions = new Vector2();
                dimensions = Vector2Utils.read(iterator);

                iterator = iterator.next();
                assert iterator.name().equals("numDoorways");
                numDoorways = IntUtils.strDigitsToInt(iterator.name());

                iterator = iterator.next();
                if (iterator != null) {
                    assert iterator.name().equals("room");
                    room = new Room();
                    room.read(json, iterator);
                    iterator = iterator.next();
                } else {
                    room = null;
                }

                assert iterator == null;
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("Error reading room plan from value " + iterator.name());
            }
        }
    }
}