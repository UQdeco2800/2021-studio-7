package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaceableBoxActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(PlaceableBoxActions.class);
    private static final String UPDATEANIMATION = "update_animation";

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(UPDATEANIMATION, "box");
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
            entity.getEvents().trigger(UPDATEANIMATION, "box_highlight");
        } else {
            logger.debug("BOX started collision with PLAYER");
            entity.getEvents().trigger(UPDATEANIMATION, "box");
        }
    }
}
