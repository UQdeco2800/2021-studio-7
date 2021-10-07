package com.deco2800.game.chores;

import com.deco2800.game.entities.Entity;

import java.util.HashMap;

/**
 * Stores and handles generation and completion of chores.
 */
public class ChoreController {
    HashMap<Entity, Boolean> choreList = new HashMap<>();

    /**
     * Adds this entity as a chore to be completed by the player. Should be interactable.
     * @param chore The interactable entity required to complete the chore.
     */
    public void addChore(Entity chore) {
        //chore.getEvents().addListener("");
        if (!choreList.containsKey(chore)) {
            choreList.put(chore, Boolean.FALSE);
        }
        chore.getEvents().addListener("completed", this::markComplete);
    }

    /**
     * Marks the specified chore as complete
     * @param chore The chore to mark off as complete
     */
    private void markComplete(Entity chore) {
        choreList.replace(chore, Boolean.TRUE);
        // Should probably signal chore UI
    }
}
