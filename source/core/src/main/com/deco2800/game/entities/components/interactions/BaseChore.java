package com.deco2800.game.entities.components.interactions;

import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;

public class BaseChore extends InteractionComponent {
    ChoreList object;

    /**
     * An interaction component that contains the basic functionality for a chore.
     * @param object The ChoreList type of this chore
     */
    public BaseChore(ChoreList object) {
        this.object = object;
    }

    @Override
    public void onInteraction(Entity target) {
        super.onInteraction(target);

        // Tell the chore controller that this chore is complete
        entity.getEvents().trigger("chore_complete", object);
    }
}
