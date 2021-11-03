package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;

public class HorizontalDoorActions extends DoorActions {

    @Override
    public void create() {
        // Door animation states
        super.closedState = "door_close_right_re";
        super.closedHlState = "right_highlight";
        super.openState = "door_open_right_re";

        super.create();
    }

    @Override
    public void onInteraction(Entity target) {
        super.onInteraction(target);
        super.logger.debug("PLAYER interacted with HORIZONTAL_DOOR, triggering door animation");
    }
}