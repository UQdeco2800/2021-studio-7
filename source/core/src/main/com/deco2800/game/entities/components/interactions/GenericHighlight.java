package com.deco2800.game.entities.components.interactions;

import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;

public class GenericHighlight extends InteractionComponent {
    private final String name;

    /**
     * An interaction component that contains the basic functionality for an interactable object
     * with a highlight.
     * @param name The name of this object used in the atlas file
     */
    public GenericHighlight(String name) {
        this.name = name;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger("update_animation", name);
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            entity.getEvents().trigger("update_animation", name + "_highlight");
        } else {
            entity.getEvents().trigger("update_animation", name);
        }
    }
}
