package com.deco2800.game.ai.tasks;

import com.badlogic.gdx.math.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MumWaitTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(SlipTask.class);

    private MovementTask movementTask;
    private WaitTask waitTask;
    private Vector2 startPos;
    private int priority = 100;
    private Task currentTask;

    public MumWaitTask(){}

    @Override
    public int getPriority() {
        return priority; // Low priority task
    }

    @Override
    public void start(){
        super.start();

        waitTask = new WaitTask(60);
        waitTask.create(owner);

        movementTask = new MovementTask(new Vector2(20f,-1.5f));
        movementTask.create(owner);

        waitTask.start();
        currentTask = waitTask;
    }
    @Override
    public void update() {
        if (currentTask.getStatus() != Status.ACTIVE) {
            if (currentTask == waitTask) {
                startMoving();
                this.priority = -1; //Change priority to now chase.
            }
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