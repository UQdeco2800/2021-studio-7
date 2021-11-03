package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;

public class VerticalDoorActions extends DoorActions {

    @Override
    public void create() {
        // Door animation states
        super.closedState = "door_close_left_re";
        super.closedHlState = "left_highlight";
        super.openState = "door_open_left_re";

        super.create();
    }

    @Override
    public void onInteraction(Entity target) {
        super.onInteraction(target);
        super.logger.debug("PLAYER interacted with VERTICAL_DOOR, triggering door animation");
    }
}