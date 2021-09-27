package com.deco2800.game.maps.floors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.maps.Home;
import com.deco2800.game.maps.rooms.Room;
import com.deco2800.game.maps.rooms.RoomObject;
import com.deco2800.game.utils.math.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FloorPlan implements Json.Serializable {

    private static final Logger logger = LoggerFactory.getLogger(FloorPlan.class);
    private RoomObject defaultFloorTile;
    private ObjectMap<Character, RoomObject> floorTiles;
    private ObjectMap<Character, RoomObject> floorEntities;
    private ObjectMap<Character, RoomPlan> floorRooms;
    private Character[][] floorGrid;
    private Vector2 dimensions;
    private boolean created = false;

    public void create() {
        if (!created) {
            designateRooms();
        }
        created = true;
    }

    public void designateRooms() {
        for (RoomPlan roomPlan : new ObjectMap.Values<>(floorRooms)) {
            roomPlan.setOffset(new GridPoint2(roomPlan.getOffset().y, (int) dimensions.y - roomPlan.getOffset().x - 1));
            roomPlan.create();
        }
    }

    public RoomObject getDefaultFloorTile() {
        return defaultFloorTile;
    }

    public ObjectMap<Character, RoomPlan> getFloorRooms() {
        return floorRooms;
    }

    public ObjectMap<Character, RoomObject> getFloorTiles() {
        return floorTiles;
    }

    public ObjectMap<Character, RoomObject> getFloorEntities() {
        return floorEntities;
    }

    public Character[][] getFloorGrid() {
        return floorGrid;
    }

    public Vector2 getDimensions() {
        return dimensions;
    }

    @Override
    public void write(Json json) {
        json.writeObjectStart();
        json.writeValue("defaultFloorTile", defaultFloorTile);
        json.writeValue("floorTiles", floorTiles);
        json.writeValue("floorEntities", floorEntities);
        json.writeValue("floorRooms", floorRooms);
        json.writeValue("floorGrid", floorGrid);
        json.writeObjectEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        try {
            JsonValue iterator = jsonData.child();
            FileLoader.assertJsonValueName(iterator, "defaultFloorTile");
            defaultFloorTile = new RoomObject();
            defaultFloorTile.read(json, iterator);

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "floorTiles");
            floorTiles = new ObjectMap<>();
            FileLoader.readCharacterObjectMap(RoomObject.class, floorTiles, json, iterator);

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "floorEntities");
            floorEntities = new ObjectMap<>();
            FileLoader.readCharacterObjectMap(RoomObject.class, floorEntities, json, iterator);

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "floorRooms");
            floorRooms = new ObjectMap<>();
            FileLoader.readCharacterObjectMap(RoomPlan.class, floorRooms, json, iterator);

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "floorGrid");
            floorGrid = new Character[iterator.size][iterator.child().size];
            FileLoader.readCharacterGrid(floorGrid, iterator);
            floorGrid = MatrixUtils.rotateAntiClockwise(floorGrid);

            dimensions = new Vector2(floorGrid[0].length, floorGrid.length);

            FileLoader.assertJsonValueNull(iterator.next());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    static public class RoomPlan implements Json.Serializable {

        private GridPoint2 offset;
        private Vector2 dimensions;
        private Class<? extends Room> roomClass;
        private Room room;
        private boolean created = false;

        public void create() {
            if (!created) {
                if (room == null) {
                    room = designateRoom();
                } else {
                    (roomClass.cast(room)).create();
                }
            }
            created = true;
        }

        public Room designateRoom() {
            Array<FileHandle> fileHandles = FileLoader.getJsonFiles(Home.ROOM_CLASS_TO_PATH.get(roomClass));

            Room randomRoom;
            do {
                FileHandle fileHandle = fileHandles.get(RandomUtils.getSeed().nextInt(fileHandles.size));
                randomRoom = FileLoader.readClass(roomClass, fileHandle.path());
                fileHandles.removeValue(fileHandle, true);

                if (!dimensions.equals(randomRoom.getDimensions())) {
                    randomRoom = null;
                }
            } while (randomRoom == null && fileHandles.size > 0);

            if (randomRoom == null) {
                throw new NullPointerException("A valid room interior json file could not be loaded");
            }

            return randomRoom;
        }

        public GridPoint2 getOffset() {
            return offset;
        }

        public void setOffset(GridPoint2 offset) {
            this.offset = offset;
        }

        public Vector2 getDimensions() {
            return dimensions;
        }

        public Room getRoom() {
            return room;
        }

        @Override
        public void write(Json json) {
            json.writeObjectStart();
            json.writeValue("offset", offset);
            json.writeValue("dimensions", dimensions);
            json.writeValue("class", room.getClass().getName());
            json.writeValue("room", room);
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
                FileLoader.assertJsonValueName(iterator, "class");
                roomClass = (Class<? extends Room>) Class.forName(iterator.asString());
                
                iterator = iterator.next();
                room = null;
                if (iterator != null) {
                    FileLoader.assertJsonValueName(iterator, "room");
                    if (!iterator.isNull()) {
                        room = roomClass.getConstructor().newInstance();
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