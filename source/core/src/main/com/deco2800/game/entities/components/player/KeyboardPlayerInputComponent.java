package com.deco2800.game.entities.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.generic.ServiceLocator;

import com.deco2800.game.input.components.InputComponent;
import com.deco2800.game.screens.maingame.MainGamePauseMenuDisplay;
import com.deco2800.game.screens.maingame.MainGameScreen;
import com.deco2800.game.utils.math.Vector2Utils;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
    private static final String UPDATEANIMATION = "update_animation";
    private static final String STANDINGSOUTH = "standing_south";

    private final Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean running = false;
    private int lastDirection = 0; // Used to track animations
    private int currentDirection = 0;
    private boolean isPaused = false;
    private boolean buffed = false;

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
                movementEvents();
                return true;
            case Keys.A:
                if (isPaused) {
                    MainGamePauseMenuDisplay.moveLeft();
                } else {
                    walkDirection.add(Vector2Utils.LEFT);
                    triggerWalkEvent();
                    movementEvents();
                }
                return true;
            case Keys.S:
                walkDirection.add(Vector2Utils.DOWN);
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.D:
                if (isPaused) {
                    MainGamePauseMenuDisplay.moveRight();
                } else {
                    walkDirection.add(Vector2Utils.RIGHT);
                    triggerWalkEvent();
                    movementEvents();
                }
                return true;
            case Keys.R:
                walkDirection.add(Vector2Utils.NORTHEAST);
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.Q:
                walkDirection.add(Vector2Utils.NORTHWEST);
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.Z:
                walkDirection.add(Vector2Utils.SOUTHWEST);
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.C:
                walkDirection.add(Vector2Utils.SOUTHEAST);
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.SHIFT_LEFT:
                enableRun();
                movementEvents();
                triggerRunEvent();
                return true;
            case Keys.E:
                entity.getEvents().trigger("toggle_interacting", true);
                return true;
            case Keys.O:
                ServiceLocator.getScreen(MainGameScreen.class)
                        .getMainGameEntity().getEvents().trigger("toggle_chores");
                return true;
            case Keys.LEFT:
                if (isPaused) {
                    MainGamePauseMenuDisplay.moveLeft();
                }
                return true;
            case Keys.RIGHT:
                if (isPaused) {
                    MainGamePauseMenuDisplay.moveRight();
                }
                return true;
            case Keys.ENTER:
                if (isPaused) {
                    ((MainGameScreen) ServiceLocator.getGame().getScreen())
                            .getMainGameEntity().getEvents().trigger("pressed_enter_in_pause");
                    isPaused = false;
                }
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
                setWalkDirection(Vector2Utils.UP);
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.A:
                setWalkDirection(Vector2Utils.LEFT);
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.S:
                setWalkDirection(Vector2Utils.DOWN);
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.D:
                setWalkDirection(Vector2Utils.RIGHT);
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.E:
                entity.getEvents().trigger("toggle_interacting", false);
                return true;
            case Keys.F:
                entity.getEvents().trigger(UPDATEANIMATION, "interacting_south_normal");
                return true;
            case Keys.SHIFT_LEFT:
                disableRun();
                triggerRunEvent();
                movementEvents();
                return true;
            case Keys.P:
            case Keys.ESCAPE:
                ServiceLocator.getScreen(MainGameScreen.class)
                        .getMainGameEntity().getEvents().trigger("toggle_pause_visibility");
                isPaused = !isPaused;
                MainGamePauseMenuDisplay.resetHover();
                return true;
            default:
                return false;
        }
    }


    private void triggerWalkEvent() {
        if (walkDirection.epsilonEquals(Vector2.Zero)) {
            entity.getEvents().trigger("stop_walking");
            switch (lastDirection) {
                case 0:
                case 2:
                    this.setAnimation(STANDINGSOUTH);
                    break;
                case 1:
                    this.setAnimation("standing_east");
                    break;
                case 3:
                    this.setAnimation("standing_west");
                    break;
                case 4:
                    this.setAnimation("standing_northeast");
                    break;
                case 5:
                    this.setAnimation("standing_northwest");
                    break;
                case 6:
                    this.setAnimation("standing_southeast");
                    break;
                case 7:
                    this.setAnimation("standing_southwest");
                    break;
                default:
                    this.setAnimation(STANDINGSOUTH);
                    break;
            }
        } else {
            if (true) {
                entity.getEvents().trigger("walk", walkDirection);
                if (walkDirection.epsilonEquals(0, 1)) {
                    this.setAnimation("walking_north");

                } else if (walkDirection.epsilonEquals(1, 0)) {
                    this.setAnimation("walking_east");

                } else if (walkDirection.epsilonEquals(0, -1)) {
                    this.setAnimation("walking_south");

                } else if (walkDirection.epsilonEquals(-1, 0)) {
                    this.setAnimation("walking_west");

                } else if (walkDirection.epsilonEquals(1, 1)) {
                    this.setAnimation("walking_northeast");

                } else if (walkDirection.epsilonEquals(-1, 1)) {
                    this.setAnimation("walking_northwest");

                } else if (walkDirection.epsilonEquals(1, -1)) {
                    this.setAnimation("walking_southeast");

                } else if (walkDirection.epsilonEquals(-1, -1)) {
                    this.setAnimation("walking_southwest");
                }
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


    /**
     * Function used to update the entities animations based upon the direction of movement.
     * Character will display the animation that tis within 45 degrees of the nearest compass direction.
     * For example, if the entites vector is (-0.1,-0.9) than it will display a down walking animation.
     */
    public void movementEvents() {
        Vector2 entityDirection = walkDirection;
        float x = entityDirection.x;
        float y = entityDirection.y;

        if (lastDirection != currentDirection) {
            if (x < 0.5 && x > -0.5 && y > 0) {
                lastDirection = 0;

            } else if (x > 0 && y < 0.5 && y > -0.5) {
                lastDirection = 1;

            } else if (x < 0.5 && x > -0.5 && y < 0) {
                lastDirection = 2;

            } else if (x < 0 && y < 0.5 && y > -0.5) {
                lastDirection = 3;

            } else if (x > 0.5 && y >0.5) {
                lastDirection = 4;
            } else if (x < -0.5 && y >0.5) {
                lastDirection = 5;

            } else if (x > 0.5 && y < -0.5) {
                lastDirection = 6;

            } else if (x < -0.5 && y < -0.5) {
                lastDirection = 7;

            }

        }
    }
    public void setBuffed(){
        this.buffed = true;
    }

    public void setBuffedOff(){
        this.buffed = false;
    }

    public void setAnimation(String direction) {

        String animation;
        if (this.buffed) {
            animation = direction + "_buffed";
        } else {
            animation = direction + "_normal";
        }
        entity.getEvents().trigger(UPDATEANIMATION, animation);
    }



    public void setWalkDirection(Vector2 direction) {
        walkDirection.sub(direction);
    }

}