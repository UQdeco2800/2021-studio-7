package com.deco2800.game.areas.home;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Home {
    private static final Logger logger = LoggerFactory.getLogger(Home.class);
    private static final String directory = "maps/_floorplans";
    private HomeFloorPlan homeFloorPlan;

    public HomeFloorPlan getHomeFloorPlan() {
        if (homeFloorPlan == null) {
            homeFloorPlan = FileLoader.loadRandomHomeFloorPlan(directory);
        }
        return homeFloorPlan;
    }

    public void setHomeFloorPlan(HomeFloorPlan homeFloorPlan) {
        this.homeFloorPlan = homeFloorPlan;
    }

    static public class HomeFloorPlan {

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
            private Room room;

            public RoomFloorPlan(GridPoint2 offset, Vector2 dimensions, Integer numDoorways) {
                this.offset = offset;
                this.dimensions = dimensions;
                this.numDoorways = numDoorways;
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

            public Room getRoom() {
                return room;
            }

            public void setRoom(Room room) {
                this.room = room;
            }
        }
    }
}