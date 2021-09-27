package com.deco2800.game.maps.rooms;

public class Laundry extends Room {

    @Override
    public void create() {
        if (!created) {
            super.create();
        }
        created = true;
    }
}
