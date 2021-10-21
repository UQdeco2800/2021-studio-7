package com.deco2800.game.chores;

import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Stores and handles generation and completion of chores.
 */
public class ChoreController {
    private static final Logger logger = LoggerFactory.getLogger(ChoreController.class);
    List<Chore> chores;
    private int entityCount = 0;
    private int level;

    /**
     * Stores and handles generation and completion of chores, scaled to the current level.
     * @param level The current level.
     */
    public ChoreController(int level) {
        this.level = level;
        this.chores = new ArrayList<>();
    }

    /**
     * Adds this entity as a chore to be completed by the player. Should be interactable.
     * @param entity The interactable entity required to complete the chore.
     * @param object The object type of the chore.
     */
    public void addChore(Entity entity, ChoreList object) {
        logger.debug("Added chore to chore controller");

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
     * Marks the specified chore as complete.
     * @param object The object chore to mark off as complete.
     */
    private void markCompleted(ChoreList object) {
        Chore chore = getChoreOf(object);
        if (chore != null) {
            // Reduce the count of the remaining chore entities
            chore.decreaseAmount();
            entityCount--;
            // Check if the whole chore is complete
            if (!chore.isActive()) {
                chores.remove(chore);
            }
        }
    }

    /**
     * Scales the current list of chores to the difficulty level by reducing the number of chores.
     */
    private void scaleChores() {
        // How many chores we want to keep
        int numChores;
        if (level < 2) {
            numChores = 0;
        } else {
            numChores = 2 * level - 2; // 0, 0, 4, 6, 8...
        }

        if (chores.size() <= numChores) {
            // We have the correct number of chores already
            return;
        }

        while (chores.size() > numChores) {
            // Keep removing random chores until we have the correct amount.
            int randomNum = ThreadLocalRandom.current().nextInt(chores.size());
            chores.remove(randomNum);
        }
    }

    /**
     * Get an ArrayList of the chores registered.
     * @return The ArrayList of chores registered.
     */
    public List<Chore> getChores() {
        scaleChores();
        return chores;
    }

    /**
     * Get the number of entities currently registered and NOT completed.
     * @return The number of entity.
     */
    public int getEntityCount() {
        return entityCount;
    }

    /**
     * Check if there are any more chores to complete.
     * @return True if all chores are complete, false otherwise.
     */
    public boolean checkComplete() {
        return chores.isEmpty();
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
