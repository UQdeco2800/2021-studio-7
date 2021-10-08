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
        chores.add(chore);

        ((MainGameScreen) ServiceLocator.getGame().getScreen())
                .getMainGameEntity().getComponent(ChoreUI.class).display();
        //System.out.println("ChoreController");
    }

    /**
     * Marks the specified chore as complete
     * @param chore The chore to mark off as complete
     */
    private void markComplete(Chore chore) {
        chores.remove(chore);
        //chores.get(index).markComplete();
    }

    public ArrayList<Chore> getChores() {
        return chores;
    }

    public boolean checkComplete() {
        int numComplete = 0;
        for (Chore chore : chores) {
            if (!chore.isActive()) {
                numComplete++;
            }
        }
        return numComplete == chores.size();
    }
}
