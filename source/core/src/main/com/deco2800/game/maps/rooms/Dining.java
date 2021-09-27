package com.deco2800.game.maps.rooms;

public class Dining extends Room {

    @Override
    public void create() {
        if (!created) {
            super.create();
        }
        created = true;
    }
}
