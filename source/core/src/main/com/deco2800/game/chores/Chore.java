package com.deco2800.game.chores;

/**
 * A struct-like class to simplify the chore system. Stores the object type and
 * description of a chore, and keeps track of the amount of entities left for this chore.
 */
public class Chore {
    private final ChoreList object;
    private String description;
    private int amount = 1;
    private boolean active = true;

    /**
     * Initialise a new chore for the given object, that handles number of entities left
     * and the corresponding description.
     * @param object The ChoreList object type of this chore.
     */
    public Chore(ChoreList object) {
        this.object = object;
        this.description = object.getDescription();
    }

    /**
     * Increase the number of entities stored for this chore.
     */
    public void increaseAmount() {
        amount++;
        this.description = object.getDescription(amount);
    }

    /**
     * Decrease the count of remaining entities for this chore.
     */
    public void decreaseAmount() {
        amount--;
        switch (amount) {
            case 0:
                // No more remaining entities, chore complete.
                active = false;
                return;
            case 1:
                // Non-plural description
                this.description = object.getDescription();
                return;
            default:
                // Plural description
                this.description = object.getDescription(amount);
        }
    }

    /**
     * Get the ChoreList object type of this chore.
     * @return The ChoreList object type of this chore.
     */
    public ChoreList getObject() {
        return this.object;
    }

    /**
     * Get the description of this chore to be printed to the UI. Includes amount of entities.
     * @return A String of the description of this chore.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Whether the chore is not complete (active), or completed (inactive).
     * @return True if active, false if complete.
     */
    public boolean isActive() {
        return active;
    }
}
