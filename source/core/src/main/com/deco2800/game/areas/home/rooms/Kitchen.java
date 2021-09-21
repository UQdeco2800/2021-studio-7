package com.deco2800.game.areas.home.rooms;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.Room;
import com.deco2800.game.areas.home.RoomObject;
import com.deco2800.game.files.FileLoader;

public class Kitchen extends Room {

    private static final String directory = "maps/kitchen";
    private KitchenInterior interior;

    public Kitchen(Integer maxDoorways, ObjectMap<Class<Room>, String[]> doorwayRestrictions) {
        super(maxDoorways, doorwayRestrictions);
    }

    public KitchenInterior getInterior() {
        return interior;
    }

    public void setInterior(KitchenInterior interior) {
        this.interior = interior;
    }

    public void setInterior(Vector2 dimensions) {
        this.interior = FileLoader.loadRandomRoomInterior(KitchenInterior.class, dimensions, directory);
    }

    static public class KitchenInterior extends RoomInterior {

        public KitchenInterior(ObjectMap<Character, RoomObject> tileMappings,
                              ObjectMap<Character, RoomObject> entityMappings,
                              Character[][] tileGrid, Character[][] entityGrid) {
            super(tileMappings, entityMappings, tileGrid, entityGrid);
        }
    }
}
