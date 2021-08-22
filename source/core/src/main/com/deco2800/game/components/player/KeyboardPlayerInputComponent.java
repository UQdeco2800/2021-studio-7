package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.utils.math.Vector2Utils;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
    private final Vector2 walkDirection = Vector2.Zero.cpy();
    private int direction = 0;
    private boolean w_pressed = false;
    private boolean a_pressed = false;
    private boolean s_pressed = false;
    private boolean d_pressed = false;
    private boolean shift_pressed = false;

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
        findDirection();
        switch (keycode) {
            case Keys.W:
                w_pressed = true;
                walkDirection.add(Vector2Utils.UP);
                findDirection();
                triggerWalkEvent();
                return true;
            case Keys.A:
                a_pressed = true;
                walkDirection.add(Vector2Utils.LEFT);
                findDirection();
                triggerWalkEvent();
                return true;
            case Keys.S:
                s_pressed = true;
                walkDirection.add(Vector2Utils.DOWN);
                findDirection();
                triggerWalkEvent();
                return true;
            case Keys.D:
                d_pressed = true;
                walkDirection.add(Vector2Utils.RIGHT);
                findDirection();
                triggerWalkEvent();
                return true;
            case Keys.SPACE:
                entity.getEvents().trigger("attack");
                return true;
            case Keys.SHIFT_LEFT:
                shift_pressed = true;
                enableSprint();
                findDirection();
                triggerWalkEvent();
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
        findDirection();
        switch (keycode) {
            case Keys.W:
                w_pressed = false;
                walkDirection.sub(Vector2Utils.UP);
                if (direction == Keys.W && shift_pressed) {
                    walkDirection.set(Vector2.Zero);
                    direction = 0;
                }
                findDirection();
                triggerWalkEvent();
                return true;
            case Keys.A:
                a_pressed = false;
                walkDirection.sub(Vector2Utils.LEFT);
                if (direction == Keys.A && shift_pressed) {
                    walkDirection.set(Vector2.Zero);
                    direction = 0;
                }
                findDirection();
                triggerWalkEvent();
                return true;
            case Keys.S:
                s_pressed = false;
                walkDirection.sub(Vector2Utils.DOWN);
                if (direction == Keys.S && shift_pressed) {
                    walkDirection.set(Vector2.Zero);
                    direction = 0;
                }
                findDirection();
                triggerWalkEvent();
                return true;
            case Keys.D:
                d_pressed = false;
                walkDirection.sub(Vector2Utils.RIGHT);
                if (direction == Keys.D && shift_pressed) {
                    walkDirection.set(Vector2.Zero);
                    direction = 0;
                }
                findDirection();
                triggerWalkEvent();
                return true;
            case Keys.SHIFT_LEFT:
                shift_pressed = false;
                findDirection();
                disableSprint();
                triggerWalkEvent();
                return true;
            default:
                findDirection();
                return false;
        }
    }

    private void enableSprint() {
        walkDirection.set(Vector2.Zero);
        if (direction == Keys.W) {
            walkDirection.add(Vector2Utils.SPRINT_UP);
        } else if (direction == Keys.S) {
            walkDirection.add(Vector2Utils.SPRINT_DOWN);
        } else if (direction == Keys.A) {
            walkDirection.add(Vector2Utils.SPRINT_LEFT);
        } else if (direction == Keys.D) {
            walkDirection.add(Vector2Utils.SPRINT_RIGHT);
        } else if (direction == Keys.W + Keys.A) {
            walkDirection.add(Vector2Utils.SPRINT_UPLEFT);
        } else if (direction == Keys.W + Keys.D) {
            walkDirection.add(Vector2Utils.SPRINT_UPRIGHT);
        } else if (direction == Keys.S + Keys.A) {
            walkDirection.add(Vector2Utils.SPRINT_DOWNLEFT);
        } else if (direction == Keys.S + Keys.D) {
            walkDirection.add(Vector2Utils.SPRINT_DOWNRIGHT);
        }
    }

    private void disableSprint() {
        walkDirection.set(Vector2.Zero);
        if (direction == Keys.W) {
            walkDirection.add(Vector2Utils.UP);
        } else if (direction == Keys.S) {
            walkDirection.add(Vector2Utils.DOWN);
        } else if (direction == Keys.A) {
            walkDirection.add(Vector2Utils.LEFT);
        } else if (direction == Keys.D) {
            walkDirection.add(Vector2Utils.RIGHT);
        } else if (direction == Keys.W + Keys.A) {
            walkDirection.add(Vector2Utils.UPLEFT);
        } else if (direction == Keys.W + Keys.D) {
            walkDirection.add(Vector2Utils.UPRIGHT);
        } else if (direction == Keys.S + Keys.A) {
            walkDirection.add(Vector2Utils.DOWNLEFT);
        } else if (direction == Keys.S + Keys.D) {
            walkDirection.add(Vector2Utils.DOWNRIGHT);
        }
    }

    private void findDirection() {
        if (w_pressed) {
            if (a_pressed) {
                direction = Keys.W + Keys.A;
            }
            if (d_pressed) {
                direction = Keys.W + Keys.D;
            }
            else {
                direction = Keys.W;
            }
        }
        if (s_pressed) {
            if (a_pressed) {
                direction = Keys.S + Keys.A;
            }
            if (d_pressed) {
                direction = Keys.S + Keys.D;
            }
            else {
                direction = Keys.S;
            }
        }
        if (a_pressed && !s_pressed && !w_pressed) {
            direction = Keys.A;
        }
        if (d_pressed && !s_pressed && !w_pressed) {
            direction = Keys.D;
        }
        if (!w_pressed && !s_pressed && !a_pressed && !d_pressed) {
            direction = 0;
            walkDirection.set(Vector2Utils.ZERO);
        }
    }

    private void triggerWalkEvent() {
        if (walkDirection.epsilonEquals(Vector2.Zero)) {
            entity.getEvents().trigger("walkStop");
        } else {
            entity.getEvents().trigger("walk", walkDirection);
        }
    }
}
