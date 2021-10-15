package com.deco2800.game.entities.components.interactions.actions;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.physics.PhysicsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaceableBoxActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(PlaceableBoxActions.class);

    private boolean boxMoved = false;

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger("update_animation", "box");
    }

    @Override
    public void onInteraction(Entity target) {
    if (target.getComponent(PlayerActions.class) != null) {
            logger.debug("BOX started interaction with SURVEYOR");
            this.getEntity().dispose();
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("BOX ended collision with PLAYER");
            entity.getEvents().trigger("update_animation", "box_highlight");
        } else {
            logger.debug("BOX started collision with PLAYER");
            entity.getEvents().trigger("update_animation", "box");
        }
    }
}
