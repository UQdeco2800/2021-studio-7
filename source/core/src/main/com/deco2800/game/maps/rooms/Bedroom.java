package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Bedroom extends Room {

    private BedroomInterior interior;

    @Override
    public void create(GridPoint2 offset, Vector2 dimensions) {
        if (!created) {
            super.create(offset, dimensions);
            if (interior == null) {
                interior = designateInterior(BedroomInterior.class, dimensions,
                        RoomProperties.ROOM_CLASS_TO_PATH.get(this.getClass()));
            } else {
                interior.create();
            }
        }
        created = true;
    }

    public BedroomInterior getInterior() {
        return interior;
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        interior = (BedroomInterior) super.interior;
    }

    static public class BedroomInterior extends RoomInterior {
    }
}
