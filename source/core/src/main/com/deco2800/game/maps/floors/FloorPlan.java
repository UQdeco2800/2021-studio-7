package com.deco2800.game.maps.floors;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.rooms.RoomProperties;
import com.deco2800.game.maps.rooms.Room;
import com.deco2800.game.maps.rooms.RoomObject;
import com.deco2800.game.utils.math.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class FloorPlan implements Json.Serializable {

    private static final Logger logger = LoggerFactory.getLogger(FloorPlan.class);
    private RoomObject defaultTileObject;
    private ObjectMap<Character, RoomPlan> roomMappings;
    private ObjectMap<Character, String> miscMappings;
    private Character[][] floorGrid;
    private boolean created = false;

    public void create() {
        if (!created) {
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
            JsonValue iterator = jsonData.child();
            FileLoader.assertJsonValueName(iterator, "defaultTileObject");
            defaultTileObject = new RoomObject();
            defaultTileObject.read(json, iterator);

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "roomMappings");
            roomMappings = new ObjectMap<>();
            FileLoader.readCharacterObjectMap(RoomPlan.class, roomMappings, json, iterator);

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "miscMappings");
            miscMappings = new ObjectMap<>();
            JsonValue miscIterator = iterator.child();
            while (miscIterator != null) {
                if (miscIterator.name().length() != 1) {
                    throw new IllegalArgumentException("Misc mapping key should be one character");
                }
                miscMappings.put(miscIterator.name().charAt(0), miscIterator.asString());
                miscIterator = miscIterator.next();
            }

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "floorGrid");
            floorGrid = new Character[iterator.size][iterator.child().size];
            FileLoader.readCharacterGrid(floorGrid, iterator);
            MatrixUtils.flipVertically(floorGrid);

            FileLoader.assertJsonValueNull(iterator.next());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    static public class RoomPlan implements Json.Serializable {

        private GridPoint2 offset;
        private Vector2 dimensions;
        private Integer numDoorways;
        private Room room;
        private boolean created = false;

        public void create() {
            if (!created) {
                if (room == null) {
                    room = designateRoom();
                } else {
                    room.create(offset, dimensions);
                }
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
                randomRoom = ServiceLocator.getHome().getRoomProperties().get(randomRoomClass, size);
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
            json.writeValue(room.getClass().getName(), room);
            json.writeObjectEnd();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void read(Json json, JsonValue jsonData) {
            try {
                JsonValue iterator = jsonData.child();
                FileLoader.assertJsonValueName(iterator, "offset");
                offset = GridPoint2Utils.read(iterator);

                iterator = iterator.next();
                FileLoader.assertJsonValueName(iterator, "dimensions");
                dimensions = Vector2Utils.read(iterator);

                iterator = iterator.next();
                FileLoader.assertJsonValueName(iterator, "numDoorways");
                numDoorways = iterator.asInt();

                iterator = iterator.next();
                room = null;
                if (iterator != null) {
                    if (!iterator.isNull()) {
                        Class<? extends Room> clazz = (Class<? extends Room>) Class.forName(iterator.name());
                        room = clazz.getConstructor().newInstance();
                        room.read(json, iterator);
                    }
                    iterator = iterator.next();
                }

                FileLoader.assertJsonValueNull(iterator);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}