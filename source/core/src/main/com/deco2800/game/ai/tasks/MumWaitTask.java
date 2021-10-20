package com.deco2800.game.ai.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.components.AITaskComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MumWaitTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(MumWaitTask.class);

    private MovementTask movementTask;
    private WaitTask waitTask;
    private int priority = 100;
    private Task currentTask;

    public MumWaitTask(){
        //This method is empty because all is handled by start() and other tasks.
    }

    @Override
    public int getPriority() {
        return priority; // Low priority task
    }

    @Override
    public void start(){
        super.start();
        Vector2 startPos = owner.getEntity().getPosition();

        waitTask = new WaitTask(60);
        waitTask.create(owner);

        movementTask = new MovementTask(new Vector2(20f,-1.5f));
        movementTask.create(owner);

        waitTask.start();
        currentTask = waitTask;
    }
    @Override
    public void update() {
        if (currentTask.getStatus() != Status.ACTIVE && currentTask == waitTask) {
                startMoving();
                this.priority = -1; //Change priority to now chase.
        }
        currentTask.update();
    }

    private void startMoving() {
        logger.debug("Starting moving");
        swapTask(movementTask);
    }

    private void swapTask(Task newTask) {
        currentTask = newTask;
        currentTask.start();
    }

}