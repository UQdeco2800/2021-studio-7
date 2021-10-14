package com.deco2800.game.chores;

/**
 * A struct-like class to simplify the chore system. Stores the entity and description of a chore
 */
public class Chore {
    private final ChoreList object;
    private String description;
    private int amount = 1;
    private boolean active = true;

    public Chore(ChoreList object) {
        this.object = object;
        this.description = object.getDescription();
    }

    public void increaseAmount() {
        amount++;
        this.description = object.getDescription(amount);
    }

    public void decreaseAmount() {
        amount--;
        switch (amount) {
            case 0:
                active = false;
                return;
            case 1:
                this.description = object.getDescription();
                return;
            default:
                this.description = object.getDescription(amount);
        }
    }

    public ChoreList getObject() {
        return this.object;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isActive() {
        return active;
    }
}
