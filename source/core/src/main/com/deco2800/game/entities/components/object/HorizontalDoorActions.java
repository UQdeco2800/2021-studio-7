package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.screens.game.GameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HorizontalDoorActions extends DoorActions {

    @Override
    public void create() {
        // Door animation states
        super.CLOSED_STATE = "door_close_right_re";
        super.CLOSED_HL_STATE = "right_highlight";
        super.OPEN_STATE = "door_open_right_re";

        super.create();
    }

    @Override
    public void onInteraction(Entity target) {
        super.onInteraction(target);
        super.logger.debug("PLAYER interacted with HORIZONTAL_DOOR, triggering door animation");
    }
}