package com.deco2800.game.entities.components.interactions;

import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;

public class GenericChore extends InteractionComponent {
    private final ChoreList object;

    /**
     * An interaction component that contains the basic functionality for a chore.
     *
     * @param object The chore type of this entity.
     */
    public GenericChore(ChoreList object) {
        this.object = object;
    }

    @Override
    public void onInteraction(Entity target) {
        super.onInteraction(target);
        entity.getEvents().trigger("chore_complete", object);
    }
}