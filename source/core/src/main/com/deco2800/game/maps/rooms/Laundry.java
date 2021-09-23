package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

public class Laundry extends Room {

    private LaundryInterior interior;

    @Override
    public void create(GridPoint2 offset, Vector2 dimensions) {
        super.create(offset, dimensions);
        interior = designateInterior(LaundryInterior.class, dimensions,
                RoomProperties.ROOM_CLASS_TO_PATH.get(this.getClass()));
    }

    public LaundryInterior getInterior() {
        return interior;
    }

    static public class LaundryInterior extends RoomInterior {
    }
}
