package com.deco2800.game.entities.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.input.components.InputComponent;
import com.deco2800.game.screens.maingame.MainGameScreen;
import com.deco2800.game.utils.math.Vector2Utils;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
    private final Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean running = false;

    public KeyboardPlayerInputComponent() {
        super(5);
    }

    /**
     * Triggers player events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
                walkDirection.add(Vector2Utils.UP);
                triggerWalkEvent();
                return true;
            case Keys.A:
                walkDirection.add(Vector2Utils.LEFT);
                triggerWalkEvent();
                return true;
            case Keys.S:
                walkDirection.add(Vector2Utils.DOWN);
                triggerWalkEvent();
                return true;
            case Keys.D:
                walkDirection.add(Vector2Utils.RIGHT);
                triggerWalkEvent();
                return true;
            case Keys.SPACE:
                entity.getEvents().trigger("attack");
                return true;
            case Keys.SHIFT_LEFT:
                enableRun();
                triggerRunEvent();
                return true;
            case Keys.E:
                entity.getComponent(SurveyorComponent.class).setEnabled(true);
                return true;
            default:
                return false;
        }
    }

    /**
     * Triggers player events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyUp(int)
     */
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
                walkDirection.sub(Vector2Utils.UP);
                entity.getEvents().trigger("update_animation", "standing_north");
                triggerWalkEvent();
                return true;
            case Keys.A:
                walkDirection.sub(Vector2Utils.LEFT);
                entity.getEvents().trigger("update_animation", "standing_west");
                triggerWalkEvent();
                return true;
            case Keys.S:
                walkDirection.sub(Vector2Utils.DOWN);
                entity.getEvents().trigger("update_animation", "standing_south");
                triggerWalkEvent();
                return true;
            case Keys.D:
                walkDirection.sub(Vector2Utils.RIGHT);
                entity.getEvents().trigger("update_animation", "standing_east");
                triggerWalkEvent();
                return true;
            case Keys.E:
                entity.getComponent(SurveyorComponent.class).setEnabled(false);
                return true;
            case Keys.SHIFT_LEFT:
                disableRun();
                triggerRunEvent();
                return true;
            default:
                return false;
        }
    }

    private void triggerWalkEvent() {
        if (walkDirection.epsilonEquals(Vector2.Zero)) {
            entity.getEvents().trigger("stop_walking");
        } else {
            entity.getEvents().trigger("walk", walkDirection);
            if (walkDirection.epsilonEquals(-1, 1) ||
                    walkDirection.epsilonEquals(0, 1) ||
                    walkDirection.epsilonEquals(1, 1)) {
                entity.getEvents().trigger("update_animation", "walking_north");
            } else if (walkDirection.epsilonEquals(1, 0)) {
                entity.getEvents().trigger("update_animation", "walking_east");
            } else if (walkDirection.epsilonEquals(-1, -1) ||
                    walkDirection.epsilonEquals(0, -1) ||
                    walkDirection.epsilonEquals(1, -1)) {
                entity.getEvents().trigger("update_animation", "walking_south");
            } else if (walkDirection.epsilonEquals(-1, 0)) {
                entity.getEvents().trigger("update_animation", "walking_west");
            }
        }
    }

    private void triggerRunEvent() {
        if(running) {
            entity.getEvents().trigger("run");
        } else {
            entity.getEvents().trigger("stop_running");
        }
    }


    @Override
    public void enableRun(){
        running = true;
    }

    @Override
    public void disableRun(){
        running = false;
    }

    @Override
    public boolean running(){
        return running;
    }
}