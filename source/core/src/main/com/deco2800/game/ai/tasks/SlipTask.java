package com.deco2800.game.ai.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlipTask extends DefaultTask implements PriorityTask{

    private static final Logger logger = LoggerFactory.getLogger(SlipTask.class);

    private Vector2 slipRange = new Vector2(3,3);
    private Vector2 startPos;
    private MovementTask movementTask;
    private int priority = -1;

    public SlipTask (){
        // Placeholder method
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void start() {
        super.start();
        startPos = owner.getEntity().getPosition();
        movementTask = new MovementTask(getRandomPosInRange());
        movementTask.create(owner);
        movementTask.start();
    }

    public void update() {
        if (movementTask.getStatus() != Status.ACTIVE) {
            changePriority(-1);
        } movementTask.update();
    }

    /*private void resetSlip(){
        logger.debug("Recalibrating slip direction and status");
        movementTask.setTarget(getRandomPosInRange());
        movementTask.start();
    }*/

    private Vector2 getRandomPosInRange() {
        Vector2 halfRange = slipRange.cpy().scl(0.5f);
        Vector2 min = startPos.cpy().sub(halfRange);
        Vector2 max = startPos.cpy().add(halfRange);
        return RandomUtils.random(min, max);
    }

    public void changePriority(int newPriority){
        logger.info("Priority changed to {}", newPriority);
        this.priority = newPriority;
        /*if (priority == 1){
            resetSlip();
        }*/
    }
}
