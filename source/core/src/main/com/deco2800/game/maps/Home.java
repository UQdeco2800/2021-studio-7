package com.deco2800.game.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.maps.floor.Floor;
import com.deco2800.game.entities.components.player.CameraComponent;

public class Home {
    private final Array<Floor> floors = new Array<>();
    private Floor activeFloor;

    public void create(CameraComponent cameraComponent) {
        floors.add(new Floor((OrthographicCamera) cameraComponent.getCamera()));
        activeFloor = floors.get(0);
        floors.get(0).create();
    }

    public Floor getActiveFloor() {
        return activeFloor;
    }
}
