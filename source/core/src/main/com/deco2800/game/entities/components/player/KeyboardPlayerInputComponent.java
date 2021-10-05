package com.deco2800.game.entities.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.input.components.InputComponent;
import com.deco2800.game.screens.maingame.MainGameScreen;
import com.deco2800.game.screens.pausemenu.PauseMenuScreen;
import com.deco2800.game.utils.math.Vector2Utils;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
    private final Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean running = false;
    private int lastDirection = 0; // Used to track animations
    private int currentDirection = 0;

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
                walkDirection.add(Vector2Utils.LEFT);
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.S:
                walkDirection.add(Vector2Utils.DOWN);
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.D:
                walkDirection.add(Vector2Utils.RIGHT);
                triggerWalkEvent();
                movementEvents();
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
                entity.getEvents().trigger("key_e", true);
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
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.A:
                walkDirection.sub(Vector2Utils.LEFT);
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.S:
                walkDirection.sub(Vector2Utils.DOWN);
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.D:
                walkDirection.sub(Vector2Utils.RIGHT);
                triggerWalkEvent();
                movementEvents();
                return true;
            case Keys.E:
                entity.getEvents().trigger("key_e", false);
                return true;
            case Keys.SHIFT_LEFT:
                disableRun();
                triggerRunEvent();
                movementEvents();
                return true;
            case Keys.P:
            case Keys.ESCAPE:
                triggerPauseResumeEvent();
//                ServiceLocator.getGame().setScreen(GdxGame.ScreenType.PAUSE_MENU);
//                ServiceLocator.getGame().pause();
                return true;
            default:
                return false;
        }
    }

    private void triggerPauseResumeEvent() {
        // If the screen is the main game it pauses the game, but if the screen is the pause screen
        // it resumes the game.
        System.out.println(ServiceLocator.getGame().getScreen().toString());
        if (ServiceLocator.getGame().getScreen().getClass() ==
                MainGameScreen.class) {
            ServiceLocator.getGame().setScreen(GdxGame.ScreenType.PAUSE_MENU);
            ServiceLocator.getGame().pause();
        } else if (ServiceLocator.getGame().getScreen().getClass() ==
                PauseMenuScreen.class) {
            ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_GAME);
            ServiceLocator.getGame().resume();
        }
    }

    private void triggerWalkEvent() {
        getcurrentDirectionCode();
        if (walkDirection.epsilonEquals(Vector2.Zero)) {
            entity.getEvents().trigger("stop_walking");
            entity.getEvents().trigger("update_animation", "standing_north");

        } else {
            if (true) {
                entity.getEvents().trigger("walk", walkDirection);
                if (walkDirection.epsilonEquals(0, 1)) {
                    entity.getEvents().trigger("update_animation", "walking_north");
                    lastDirection = 0;

                } else if (walkDirection.epsilonEquals(1, 0)) {
                    entity.getEvents().trigger("update_animation", "walking_east");
                    lastDirection = 1;

                } else if (walkDirection.epsilonEquals(0, -1)) {
                    entity.getEvents().trigger("update_animation", "walking_south");
                    lastDirection = 2;

                } else if (walkDirection.epsilonEquals(-1, 0)) {
                    entity.getEvents().trigger("update_animation", "walking_west");
                    lastDirection = 3;

                } else if (walkDirection.epsilonEquals(1, 1)) {
                    entity.getEvents().trigger("update_animation", "walking_northeast");
                    lastDirection = 4;

                } else if (walkDirection.epsilonEquals(-1, 1)) {
                    entity.getEvents().trigger("update_animation", "walking_northwest");
                    lastDirection = 5;

                } else if (walkDirection.epsilonEquals(1, -1)) {
                    entity.getEvents().trigger("update_animation", "walking_southeast");
                    lastDirection = 6;

                } else if (walkDirection.epsilonEquals(-1, -1)) {
                    entity.getEvents().trigger("update_animation", "walking_southwest");
                    lastDirection = 7;
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
     * Will asign an integer value to the direction. Directions are broken into compass quadrents.
     * Where:
     * 0 = North
     * 1 = East
     * 2 = South
     * 3 = West
     * 4 = NorthEast
     * 5 = NorthWest
     * 6 = SouthEast
     * 7 = SouthWest
     */
    public void getcurrentDirectionCode() {
        Vector2 entityDirection = walkDirection;
        float x = entityDirection.x;
        float y = entityDirection.y;

        if (walkDirection.epsilonEquals(Vector2.Zero)) {
            entity.getEvents().trigger("stop_walking");
            currentDirection = -1;
        } else {
            entity.getEvents().trigger("walk", walkDirection);
            if (walkDirection.epsilonEquals(0, 1)) {
                currentDirection = 0;
            } else if (walkDirection.epsilonEquals(1, 0)) {
                currentDirection = 1;
            } else if (walkDirection.epsilonEquals(0, -1) ) {
                currentDirection = 2;
            } else if (walkDirection.epsilonEquals(-1, 0)) {
                currentDirection = 3;
            } else if (walkDirection.epsilonEquals(1,1)) {
                currentDirection = 4;
            } else if (walkDirection.epsilonEquals(-1,1)) {
                currentDirection = 5;
            } else if (walkDirection.epsilonEquals(1,-1)) {
                currentDirection = 6;
            } else if (walkDirection.epsilonEquals(-1,-1)) {
                currentDirection = 7;
            }
        }
    }

    /**
     * Function used to update the entities animations based upon the direction of movement.
     * Character will display the animation that tis within 45 degrees of the nearest compass direction.
     * For example, if the entites vector is (-0.1,-0.9) than it will display a down walking animation.
     */
    public void movementEvents() {
        System.out.println("Triggering movement Events");
        System.out.println(walkDirection);
        Vector2 entityDirection = walkDirection;
        float x = entityDirection.x;
        float y = entityDirection.y;

        if (lastDirection != currentDirection) {
            if (x < 0.5 && x > -0.5 && y > 0) {
                entity.getEvents().trigger("update_animation", "walking_north");
                lastDirection = 0;
            } else if (x > 0 && y < 0.5 && y > -0.5) {
                entity.getEvents().trigger("update_animation", "walking_east");
                lastDirection = 1;
            } else if (x < 0.5 && x > -0.5 && y < 0) {
                entity.getEvents().trigger("update_animation", "walking_south");
                lastDirection = 2;
            } else if (x < 0 && y < 0.5 && y > -0.5) {
                entity.getEvents().trigger("update_animation", "walking_west");
                lastDirection = 3;
            } else if (x > 0.5 && y >0.5) {
                entity.getEvents().trigger("update_animation", "walking_northeast");
                lastDirection = 4;
            } else if (x < -0.5 && y >0.5) {
                entity.getEvents().trigger("update_animation", "walking_northwest");
                lastDirection = 5;
            } else if (x > 0.5 && y < -0.5) {
                entity.getEvents().trigger("update_animation", "walking_southeast");
                lastDirection = 6;
            } else if (x < -0.5 && y < -0.5) {
                entity.getEvents().trigger("update_animation", "walking_southwest");
                lastDirection = 7;
            }
        }
    }


}