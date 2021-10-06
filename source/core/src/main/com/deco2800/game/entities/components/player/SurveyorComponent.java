package com.deco2800.game.entities.components.player;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.raycast.RaycastHit;

public class SurveyorComponent extends Component {

    private long lastTime = 0L;

    @Override
    public void create() {
        super.create();
        setEnabled(false);
    }

    @Override
    public void update() {
        // Check if one second has passed since the key has been held down
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (currentTime - lastTime >= 1000L) {
            lastTime = currentTime;
            interactClosest();
        }
    }

    public void interactClosest() {
        // Derive coordinates from entity
        Vector2 center = entity.getCenterPosition();
        float centerX = center.x;
        float centerY = center.y;

        // Hitbox's scale (width and height)
        float width = entity.getComponent(HitboxComponent.class).getScale().x;
        float height = entity.getComponent(HitboxComponent.class).getScale().y;

        // Solve for entity's hitbox boundaries
        Vector2[] hitboxBorders = new Vector2[8];
        hitboxBorders[0] = new Vector2(centerX - width / 2, centerY + height / 2);   // Top left
        hitboxBorders[1] = new Vector2(centerX, centerY + height / 2);                  // Top center
        hitboxBorders[2] = new Vector2(centerX + width / 2, centerY + height / 2);   // Top right
        hitboxBorders[3] = new Vector2(centerX - width / 2, centerY);                   // Middle left
        hitboxBorders[4] = new Vector2(centerX + width / 2, centerY);                   // Middle right
        hitboxBorders[5] = new Vector2(centerX - width / 2, centerY - height / 2);   // Bottom left
        hitboxBorders[6] = new Vector2(centerX, centerY - height / 2);                  // Bottom center
        hitboxBorders[7] = new Vector2(centerX + width / 2, centerY - height / 2);   // Bottom right

        // Find the closest raycast hit from the center to the boundaries
        RaycastHit closest = null;
        RaycastHit[] hits = new RaycastHit[8];
        for (int i = 0; i < 8; i++) {
            hits[i] = new RaycastHit();
            if (ServiceLocator.getPhysicsService().getPhysics()
                    .raycast(center, hitboxBorders[i], PhysicsLayer.OBSTACLE, hits[i]) &&
                    (closest == null || hits[i].normal.dst(center) < closest.normal.dst(center))) {
                closest = hits[i];
            }
        }

        // Interact with the closest hit
        if (closest != null) {
            ((BodyUserData) closest.fixture.getBody().getUserData())
                    .entity.getEvents().trigger("interaction", entity);
        }
    }
}
