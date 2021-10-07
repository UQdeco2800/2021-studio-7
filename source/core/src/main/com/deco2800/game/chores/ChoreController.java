package com.deco2800.game.chores;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.maingame.MainGameScreen;

import java.util.HashMap;

/**
 * Stores and handles generation and completion of chores.
 */
public class ChoreController {
    HashMap<Entity, Boolean> choreList = new HashMap<>();
    Entity mainGame = ((MainGameScreen) ServiceLocator.getGame().getScreen()).getMainGameEntity();

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
        //mainGame.getEvents().trigger();
    }
}
