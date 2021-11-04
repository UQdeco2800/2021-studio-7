package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.screens.game.GameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DoorActions extends InteractionComponent {
    protected static final Logger logger = LoggerFactory.getLogger(DoorActions.class);
    private static final String PROMPT_MESSAGE = "You opened a door! That's pretty cool.";
    private static final String UPDATE_ANIMATION = "update_animation";

    // Door animation states
    protected String closedState = null;
    protected String closedHlState = null;
    protected String openState = null;

    private static boolean hasOpenedDoor = false;

    private boolean isOpened = false;

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("mum_cinematic_close", this::onMumCinematicClose);
        entity.getEvents().addListener("mum_cinematic_open", this::onMumCinematicOpen);
        entity.getEvents().trigger(UPDATE_ANIMATION, closedState);
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) == null)
            return;

        // Check if door has been opened already
        if (!isOpened) {
            // Give first time message
            if (!hasOpenedDoor) {
                hasOpenedDoor = true;
                ServiceLocator.getScreen(GameScreen.class).getGameUI().getEvents()
                        .trigger("create_textbox", PROMPT_MESSAGE);
            }

            // Open the door
            entity.getComponent(ColliderComponent.class).setSensor(true);
            this.isOpened = true;
            entity.getEvents().trigger(UPDATE_ANIMATION, openState);
        }
    }

    public void onMumCinematicClose() {
        entity.getComponent(ColliderComponent.class).setSensor(false);
        this.isOpened = false;
        entity.getEvents().trigger(UPDATE_ANIMATION, openState);
    }

    public void onMumCinematicOpen() {
        entity.getComponent(ColliderComponent.class).setSensor(true);
        this.isOpened = true;
        entity.getEvents().trigger(UPDATE_ANIMATION, openState);
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight && !isOpened) {
            logger.debug("DOOR started collision with PLAYER, highlighting door");
            entity.getEvents().trigger(UPDATE_ANIMATION, closedHlState);
        }  else if (!isOpened) {
            logger.debug("DOOR ended collision with PLAYER, un-highlighting door");
            entity.getEvents().trigger(UPDATE_ANIMATION, closedState);
        }
    }
}
