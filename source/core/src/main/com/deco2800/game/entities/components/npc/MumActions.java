package com.deco2800.game.entities.components.npc;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.object.DoorActions;
import com.deco2800.game.entities.components.object.HorizontalDoorActions;
import com.deco2800.game.entities.components.object.VerticalDoorActions;
import com.deco2800.game.entities.components.player.CameraComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.screens.game.GameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MumActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(MumActions.class);
    private static final long WAIT_TIME_LENGTH = 2000L;
    private static final long ACTION_TIME_LENGTH = 2500L;
    private MumCinematicPhase phase;
    private CameraComponent camera;
    private PhysicsMovementComponent movementComponent;
    private Vector2 dest;
    private long startWaitTime;
    private long startActionTime;
    private String PROMPT_MESSAGE= "What are you still doing awake!? When I get my hands on you...!" +
            "                                                                                      ";

    @Override
    public void create() {
        super.create();
        targetLayer = PhysicsLayer.OBSTACLE;
        movementComponent = entity.getComponent(PhysicsMovementComponent.class);
        entity.getEvents().trigger("update_animation", "standing_south");
        bringCameraToMum();

    }

    @Override
    public void update() {
        if (phase.equals(MumCinematicPhase.FOCUSING)) {
            if (Math.abs(camera.getLastPosition().dst(entity.getPosition())) < 0.2) {
                walkThroughDoor();
            }
        } else if (phase.equals(MumCinematicPhase.WALK)) {
            if (Math.abs(dest.dst(entity.getPosition())) < 0.2) {
                waitInHome();
                String text = ServiceLocator.getGame().getUsername();
                ServiceLocator.getScreen(GameScreen.class).getGameUI().getEvents().trigger("create_textbox", text + PROMPT_MESSAGE);
            }
        } else if (phase.equals(MumCinematicPhase.WAIT)) {
            if (ServiceLocator.getTimeSource().getTime() - startWaitTime >= WAIT_TIME_LENGTH) {
                doMumAction();
            }
        } else if (phase.equals(MumCinematicPhase.ACTION)) {
            if (ServiceLocator.getTimeSource().getTime() - startActionTime >= ACTION_TIME_LENGTH) {
                catchPlayer();
            }
        }
    }

    @Override
    public void onCollisionStart(Entity target) {
        target.getEvents().trigger("mum_cinematic_open");
    }

    private void bringCameraToMum() {
        phase = MumCinematicPhase.FOCUSING;

        camera = ServiceLocator.getScreen(GameScreen.class).getCameraComponent();
        camera.setTarget(entity);

    }

    private void walkThroughDoor() {
        phase = MumCinematicPhase.WALK;

        GridPoint2 targetPos = ServiceLocator.getHome().getFloor().getMumTargetPos();
        dest = ServiceLocator.getHome().getFloor().getTerrain().tileToWorldPosition(targetPos);
        movementComponent.setTarget(dest);
        movementComponent.setMaxSpeed(new Vector2(3f, 3f));
        movementComponent.setMoving(true);
    }

    private void waitInHome() {
        phase = MumCinematicPhase.WAIT;

        movementComponent.setMoving(false);
        startWaitTime = ServiceLocator.getTimeSource().getTime();
    }

    private void doMumAction() {
        phase = MumCinematicPhase.ACTION;

        startActionTime = ServiceLocator.getTimeSource().getTime();

        logger.info("DO MUM ACTION!!");
    }

    private void catchPlayer() {
        ServiceLocator.getScreen(GameScreen.class).getGameUI().getEvents().trigger("player_caught");
    }

    private enum MumCinematicPhase {
        INIT, FOCUSING, WALK, WAIT, ACTION
    }
}
