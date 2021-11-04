package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;

public class HorizontalDoorActions extends DoorActions {

    @Override
    public void create() {
        // Door animation states
        closedState = "door_close_right_re";
        closedHlState = "right_highlight";
        openState = "door_open_right_re";

        super.create();
    }

    @Override
    public void onInteraction(Entity target) {
        super.onInteraction(target);
        logger.debug("PLAYER interacted with HORIZONTAL_DOOR, triggering door animation");
    }
}