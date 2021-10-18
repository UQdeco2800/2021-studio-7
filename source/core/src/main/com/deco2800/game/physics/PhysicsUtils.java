package com.deco2800.game.physics;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;

public class PhysicsUtils {

  // Set the collider to the base of the entity, scaled relative to the entity size.
  public static void setScaledCollider(Entity entity, float scaleX, float scaleY) {
    Vector2 boundingBox = entity.getScale().cpy().scl(scaleX, scaleY);
    entity
        .getComponent(ColliderComponent.class)
        .setIsoShape(scaleX, scaleY);
  }

  // Set the collider to the base of the entity, scaled relative to the entity size.
  public static void setScaledHitbox(Entity entity, float scaleX, float scaleY) {
    Vector2 boundingBox = entity.getScale().cpy().scl(scaleX, scaleY);
    entity
            .getComponent(HitboxComponent.class)
            .setIsoShape(scaleX, scaleY);
  }

  public static void setColliderShape(Entity entity, float leftSides, float rightSides) {
      entity.getComponent(ColliderComponent.class)
              .setIsoShape(leftSides, rightSides);
  }

  public static void setColliderOffset(Entity entity, float offsetX, float offsetY) {
      ColliderComponent collider = entity.getComponent(ColliderComponent.class);
      collider.setIsoShapeOffset(collider.getSides()[0], collider.getSides()[1],
                      offsetX, offsetY);
  }

  public static void setHitboxShape(Entity entity, float leftSides, float rightSides) {
      entity.getComponent(HitboxComponent.class)
              .setIsoShape(leftSides, rightSides);
  }

  public static void setHitboxOffset(Entity entity, float offsetX, float offsetY) {
      ColliderComponent hitbox = entity.getComponent(HitboxComponent.class);
      hitbox.setIsoShapeOffset(hitbox.getSides()[0], hitbox.getSides()[1],
                      offsetX, offsetY);
  }

  private PhysicsUtils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
