package com.deco2800.game.entities.components.interactions.Actions;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HorizontalDoorActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(BedActions.class);

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger("update_animation", "door_close_right_re");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            String string = "You opened a horizontal door";
            logger.debug("PLAYER interacted with DOOR, triggering door animation");
            ((MainGameScreen) ServiceLocator.getGame().getScreen())
                    .getMainGameEntity().getEvents().trigger("create_textbox", string);
            entity.getComponent(ColliderComponent.class).setSensor(true);
            entity.getEvents().trigger("update_animation", "door_open_right_re");
        }
    }

    /*
    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("DOOR started collision with PLAYER, highlighting door");
            entity.getEvents().trigger("update_animation", "right_highlight"); //Door_left_highlighted
        } else {
            logger.debug("DOOR ended collision with PLAYER, un-highlighting door");
            entity.getEvents().trigger("update_animation", "door_open_right_re"); //door_close_left
        }
    }*/
}