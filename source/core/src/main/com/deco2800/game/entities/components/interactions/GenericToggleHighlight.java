package com.deco2800.game.entities.components.interactions;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;

public class GenericToggleHighlight extends InteractionComponent {
    private final String name;
    private boolean hasInteracted = false;

    /**
     * An interaction component that contains the basic functionality for an interactable object
     * with a highlight and interaction that toggles an object 'off'.
     * @param name The name of this object used in the atlas files
     */
    public GenericToggleHighlight(String name) {
        this.name = name;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger("update_animation", name);
    }

    @Override
    public void onInteraction(Entity target) {
        this.hasInteracted = true;
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (hasInteracted) {
            entity.getEvents().trigger("update_animation", name + "_off");
        } else {
            if (shouldHighlight) {
                entity.getEvents().trigger("update_animation", name + "_highlight");
            } else {
                entity.getEvents().trigger("update_animation", name);
            }
        }
    }
}
