package com.deco2800.game.entities.components.object;

import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookActions extends InteractionComponent {
    // Note this class requires the addition of the SingleUse component be added to obstacle entity
    private static final Logger logger = LoggerFactory.getLogger(BookActions.class);
    private static final String UPDATE_ANIMATION = "update_animation";
    private long startTime;
    private boolean hasInteracted = false;

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(UPDATE_ANIMATION, "dropped_book");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            logger.debug("PLAYER interacted with Book");
            startTime = ServiceLocator.getTimeSource().getTime();
            hasInteracted = true;
            //entity.getEvents().trigger(UPDATE_ANIMATION, "dust1");
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("BOOK started collision with PLAYER");
            entity.getEvents().trigger(UPDATE_ANIMATION, "dropped_book_highlight");
        } else {
            logger.debug("BOOK ended collision with PLAYER");
            entity.getEvents().trigger(UPDATE_ANIMATION, "dropped_book");
        }
    }

    @Override
    public void update() {
        //long currentTime = ServiceLocator.getTimeSource().getTime();
        if (hasInteracted) {
            ServiceLocator.getEntityService().scheduleEntityForRemoval(entity);
            entity.getEvents().trigger("chore_complete", ChoreList.BOOKS);
        }
    }
}