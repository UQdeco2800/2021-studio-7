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

    public void addObject(Entity object){
        if (isObjectInteractive(object)){
            for (Entity e : interactableObjectList){
                if (e == object){
                    return;
                }
            } interactableObjectList.add(object);
        }

    }

    public void removeObject(Entity object){
        interactableObjectList.remove(object);
    }

    private boolean isObjectInteractive(Entity object){
        if (object.getComponent(InteractableComponent.class) != null){
            return true;
        } return false;
    }
}
