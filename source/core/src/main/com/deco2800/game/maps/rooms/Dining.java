package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Dining extends Room {

    private DiningInterior interior;

    @Override
    public void create(GridPoint2 offset, Vector2 dimensions) {
        if (!created) {
            super.create(offset, dimensions);
            if (interior == null) {
                interior = designateInterior(DiningInterior.class, dimensions,
                        RoomProperties.ROOM_CLASS_TO_PATH.get(this.getClass()));
            } else {
                interior.create();
            }
        }
        created = true;
    }

    public DiningInterior getInterior() {
        return interior;
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        interior = (DiningInterior) super.interior;
    }

    static public class DiningInterior extends RoomInterior {
    }
}
