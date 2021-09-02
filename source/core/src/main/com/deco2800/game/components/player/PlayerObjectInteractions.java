package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.InteractableComponent;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PlayerObjectInteractions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PlayerObjectInteractions.class);
    private ArrayList<Entity> interactableObjectList;

    public PlayerObjectInteractions(){
        this.interactableObjectList = new ArrayList<>();
    }

    /**
     * Returns ArrayList of InteractableObjects the player is touching
     * @return ArrayList
     */
    public ArrayList getInteractableObjects(){
        return this.interactableObjectList;
    }

    /**
     * Adds a object only once to the interactableObject arraylist
     * @param object entity
     */
    public void addObject(Entity object){
        if (isObjectInteractive(object)){
            for (Entity e : interactableObjectList){
                if (e == object){
                    return;
                }
            } interactableObjectList.add(object);
        }

    }

    /**
     * Removes the object specified from the interactableObject arraylist
     * @param object entity
     */
    public void removeObject(Entity object){
        interactableObjectList.remove(object);
    }

    /**
     * Checks if a given entity possesses the InteractableComponent
     * @param object entity
     * @return boolean true if object possesses component, false otherwise
     */
    public boolean isObjectInteractive(Entity object){
        if (object.getComponent(InteractableComponent.class) != null){
            return true;
        } return false;
    }
}
