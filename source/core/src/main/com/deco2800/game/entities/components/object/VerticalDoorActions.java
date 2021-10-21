package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticalDoorActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(VerticalDoorActions.class);
    private boolean isOpened = false;
    private static String UPDATE_ANIMATION = "update_animation";

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(UPDATE_ANIMATION, "door_close_left_re");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) == null)
            return;
        if (!isOpened) {
            String msg = "You opened a door!";
            logger.debug("PLAYER interacted with VERTICAL_DOOR, triggering door animation");
            ((MainGameScreen) ServiceLocator.getGame().getScreen())
                    .getMainGameEntity().getEvents().trigger("create_textbox", msg);
            entity.getComponent(ColliderComponent.class).setSensor(true);
            this.isOpened = true;
            entity.getEvents().trigger(UPDATE_ANIMATION, "door_open_left_re");
        }

    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight && !isOpened) {
            logger.debug("DOOR started collision with PLAYER, highlighting door");
            entity.getEvents().trigger(UPDATE_ANIMATION, "left_highlight"); //Door_left_highlighted
        } else if (!isOpened){
            logger.debug("DOOR ended collision with PLAYER, un-highlighting door");
            entity.getEvents().trigger(UPDATE_ANIMATION, "door_close_left_re"); //door_close_left
        }
    }
}