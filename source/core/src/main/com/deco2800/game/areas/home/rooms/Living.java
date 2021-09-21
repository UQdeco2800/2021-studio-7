package com.deco2800.game.areas.home.rooms;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.Room;
import com.deco2800.game.areas.home.RoomObject;
import com.deco2800.game.files.FileLoader;

public class Living extends Room {

    private static final String directory = "maps/living";
    private LivingInterior interior;

    public Living(Integer maxDoorways, ObjectMap<Class<Room>, String[]> doorwayRestrictions) {
        super(maxDoorways, doorwayRestrictions);
    }

    public LivingInterior getInterior() {
        return interior;
    }

    public void setInterior(LivingInterior interior) {
        this.interior = interior;
    }

    public void setInterior(Vector2 dimensions) {
        this.interior = FileLoader.loadRandomRoomInterior(LivingInterior.class, dimensions, directory);
    }

    static public class LivingInterior extends RoomInterior {

        public LivingInterior(ObjectMap<Character, RoomObject> tileMappings,
                               ObjectMap<Character, RoomObject> entityMappings,
                               Character[][] tileGrid, Character[][] entityGrid) {
            super(tileMappings, entityMappings, tileGrid, entityGrid);
        }
    }
}
