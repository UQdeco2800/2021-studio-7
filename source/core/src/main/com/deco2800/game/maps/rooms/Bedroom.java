package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

public class Bedroom extends Room {

    private BedroomInterior interior;

    @Override
    public void create(GridPoint2 offset, Vector2 dimensions) {
        super.create(offset, dimensions);
        interior = designateInterior(BedroomInterior.class, dimensions,
                RoomProperties.ROOM_CLASS_TO_PATH.get(this.getClass()));
    }

    public BedroomInterior getInterior() {
        return interior;
    }

    static public class BedroomInterior extends RoomInterior {
    }
}
