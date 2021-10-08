package com.deco2800.game.chores;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Stores and handles generation and completion of chores.
 */
public class ChoreController {
    private static final Logger logger = LoggerFactory.getLogger(ChoreController.class);
    ArrayList<Chore> chores = new ArrayList<>();
    //Entity mainGame = ((MainGameScreen) ServiceLocator.getGame().getScreen()).getMainGameEntity();

    /**
     * Adds this entity as a chore to be completed by the player. Should be interactable.
     * @param entity The interactable entity required to complete the chore.
     * @param object The object type of the chore.
     */
    public void addChore(Entity entity, ChoreList object) {
        Chore chore = new Chore(entity, object.getDescription());
        entity.getEvents().addListener("chore_complete", this::markComplete);
        chores.add(chore);
    }

    /**
     * Marks the specified chore as complete
     * @param entity The entity to mark off as complete
     */
    private void markComplete(Entity entity) {
        //chores.remove(chore);
        for (Chore chore : chores) {
            if (chore.getEntity() == entity) {
                chores.remove(chore);
                break;
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
     * Check if there are any more chores to complete
     * @return True if all chores are complete, false otherwise
     */
    public boolean checkComplete() {
        return (chores.size() == 0);
    }
}
