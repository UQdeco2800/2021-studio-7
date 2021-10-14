package com.deco2800.game.chores;

import com.deco2800.game.entities.Entity;

import java.util.ArrayList;

/**
 * Stores and handles generation and completion of chores.
 */
public class ChoreController {
    //private static final Logger logger = LoggerFactory.getLogger(ChoreController.class);
    ArrayList<Chore> chores = new ArrayList<>();
    private int entityCount = 0;

    /**
     * Adds this entity as a chore to be completed by the player. Should be interactable.
     * @param entity The interactable entity required to complete the chore.
     * @param object The object type of the chore.
     */
    public void addChore(Entity entity, ChoreList object) {
        // Add a listener to the entity
        entity.getEvents().addListener("chore_complete", this::markCompleted);
        entityCount++;

        // Check if the entity already exists as a chore
        Chore chore = getChoreOf(object);
        if (chore != null) {
            // Already exists, tell Chore that there's another entity to interact with
            chore.increaseAmount();
        } else {
            // Register a new Chore
            chore = new Chore(object);
            chores.add(chore);
        }
    }

    /**
     * Marks the specified chore as complete
     * @param object The object chore to mark off as complete
     */
    private void markCompleted(ChoreList object) {
        System.out.println("Test");
        //chores.removeIf(chore -> chore.getEntity() == entity);
        Chore chore = getChoreOf(object);
        if (chore != null) {
            chore.decreaseAmount();
            entityCount--;
            // Check if the chore is complete
            if (!chore.isActive()) {
                chores.remove(chore);
            }
        }
    }

    /**
     * Get an ArrayList of the chores registered
     * @return The ArrayList of chores registered
     */
    public ArrayList<Chore> getChores() {
        return chores;
    }

    /**
     * Get the number of entities currently registered and NOT completed
     * @return The number of entity
     */
    public int getEntityCount() {
        return entityCount;
    }

    /**
     * Check if there are any more chores to complete
     * @return True if all chores are complete, false otherwise
     */
    public boolean checkComplete() {
        return (chores.size() == 0);
    }

    private Chore getChoreOf(ChoreList object) {
        for (Chore chore : chores) {
            if (chore.getObject() == object) {
                return chore;
            }
        }
        return null;
    }
}
