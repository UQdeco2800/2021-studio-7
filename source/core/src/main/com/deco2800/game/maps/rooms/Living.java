package com.deco2800.game.maps.rooms;

public class Living extends Room {

    @Override
    public void create() {
        if (!created) {
            super.create();
        }
        created = true;
    }
}
