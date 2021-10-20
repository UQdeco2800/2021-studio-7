package com.deco2800.game.entities.components.interactions;

import com.deco2800.game.entities.components.InteractionComponent;

public class BaseInteractable extends InteractionComponent {
    private final String name;

    /**
     * An interaction component that contains the basic functionality for a chore.
     * @param name The name of the object (for triggering animation)
     */
    public BaseInteractable(String name) {
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
            entity.getEvents().trigger("update_animation", name+"_highlight");
        } else {
            entity.getEvents().trigger("update_animation", name);
        }
    }
}
