package com.deco2800.game.entities.components.npc;

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
    private MumCinematicPhase phase;
    private Entity closestDoor;
    private CameraComponent camera;
    private Vector2 dest;

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger("update_animation", "standing_south");
        findClosestDoor();
        bringCameraToMum();
    }

    private void triggerPlayerCaught() {
        logger.debug("MUM started collision with PLAYER, triggering player caught");
        ServiceLocator.getScreen(GameScreen.class).getGameUI().getEvents().trigger("player_caught");
    }

    private void findClosestDoor() {
        phase = MumCinematicPhase.INIT;

        // Derive coordinates from entity
        Vector2 pos = entity.getPosition();

        // Solve for direction vectors
        Vector2[] direction = new Vector2[4];
        direction[0] = new Vector2(pos.x, pos.y + 5);   // North
        direction[1] = new Vector2(pos.x + 5, pos.y);   // South
        direction[2] = new Vector2(pos.x, pos.y - 5);   // East
        direction[3] = new Vector2(pos.x - 5, pos.y);   // West

        // Find the closest door from the center of the mum
        RaycastHit closest = null;
        RaycastHit[] hits = new RaycastHit[4];
        for (int i = 0; i < 4; i++) {
            hits[i] = new RaycastHit();
            if (ServiceLocator.getPhysicsService().getPhysics()
                .raycast(pos, direction[i], PhysicsLayer.OBSTACLE, hits[i]) &&
                (closest == null || hits[i].normal.dst(pos) < closest.normal.dst(pos))) {
                Entity hitEntity = (Entity) hits[i].fixture.getBody().getUserData();
                if (hitEntity.getComponent(VerticalDoorActions.class) != null ||
                    hitEntity.getComponent(HorizontalDoorActions.class) != null) {
                    closest = hits[i];
                    closestDoor = hitEntity;
                }
            }
        }

        // Interact with the closest hit
        if (closestDoor != null) {
            closestDoor.getEvents().trigger("interaction", entity);
        } else {
            throw new NullPointerException("Mum has no door to interact with");
        }
    }

    private void bringCameraToMum() {
        phase = MumCinematicPhase.FOCUSING;

        camera = ServiceLocator.getScreen(GameScreen.class).getCameraComponent();
        camera.setTarget(entity);

    }

    @Override
    public void update() {
        if (phase.equals(MumCinematicPhase.FOCUSING)) {
            if (camera.getLastPosition().epsilonEquals(entity.getPosition())) {
                walkThroughDoor();
            }
        } else if (phase.equals(MumCinematicPhase.WALK)) {
            if (entity.getPosition().epsilonEquals(dest)) {
                waitInHome();
            }
        } else if (phase.equals(MumCinematicPhase.ACTION)) {
            doMumAction();
        }
    }

    private void walkThroughDoor() {
        phase = MumCinematicPhase.WALK;

        dest = entity.getPosition().sub(closestDoor.getPosition());
        if (Math.abs(dest.x) > 0.5f) {
            dest.x *= 2;
        } else if (Math.abs(dest.y) > 0.5f) {
            dest.y *= 2;
        }
        entity.getComponent(PhysicsMovementComponent.class).setTarget(dest);
        entity.getComponent(PhysicsMovementComponent.class).setMoving(true);
    }

    private void waitInHome() {
        phase = MumCinematicPhase.WAIT;

        try {
            Thread.sleep(500L);
        } catch (Exception e) {
            logger.error("Couldn't force thread to sleep for 0.5 seconds");
        }

        phase = MumCinematicPhase.ACTION;
    }

    private void doMumAction() {
        logger.info("DOING MUM ACTION!!");
    }

    private enum MumCinematicPhase {
        INIT, FOCUSING, WALK, WAIT, ACTION
    }
}
