package com.deco2800.game.entities.components.interactions.Actions;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.CollisionInteractionComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BananaPeelActions extends CollisionInteractionComponent {

    private static final Logger logger = LoggerFactory.getLogger(BananaPeelActions.class);

    @Override
    public void create(){
        super.create();
        animator.startAnimation("banana");
    }

    public void onInteraction (Entity target){
        System.out.println("slipped on a banana!!");
    }

}
