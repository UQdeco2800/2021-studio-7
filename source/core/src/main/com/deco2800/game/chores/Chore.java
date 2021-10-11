package com.deco2800.game.chores;

import com.deco2800.game.entities.Entity;

/**
 * A struct-like class to simplify the chore system
 */
public class Chore {
    private Entity entity;
    private String description;
    private boolean active = true;

    public Chore(Entity entity, String description) {
        this.entity = entity;
        this.description = description;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public String getDescription() {
        return this.description;
    }

    public void markComplete() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }
}
