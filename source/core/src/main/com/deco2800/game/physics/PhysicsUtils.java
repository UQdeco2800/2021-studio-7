package com.deco2800.game.physics;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;

public class PhysicsUtils {

  // Set the collider to the base of the entity, scaled relative to the entity size.
  public static void setScaledCollider(Entity entity, float scaleX, float scaleY) {
    entity.getScale().cpy().scl(scaleX, scaleY);
    entity
        .getComponent(ColliderComponent.class)
        .setIsoShape(scaleX, scaleY);
  }

  // Set the collider to the base of the entity, scaled relative to the entity size.
  public static void setScaledHitbox(Entity entity, float scaleX, float scaleY) {
    entity.getScale().cpy().scl(scaleX, scaleY);
    entity
            .getComponent(HitboxComponent.class)
            .setIsoShape(scaleX, scaleY);
  }

    /**
     * Set the shape of an entity's collider component.
     *
     * @param entity the entity to change the collider of.
     * @param leftSides the length of the bottom left side of the collider (and top right), in tile units.
     * @param rightSides the length of the bottom right side of the collider (and top left), in tile units.
     */
    public static void setColliderShape(Entity entity, float leftSides, float rightSides) {
        entity.getComponent(ColliderComponent.class)
              .setIsoShape(leftSides, rightSides);
    }

    /**
     * Set the offset for an entity's collider component.
     *
     * @param entity the entity to change the collider of.
     * @param offsetX pixel offset in the x direction.
     * @param offsetY pixel offset in the y direction.
     */
    public static void setColliderOffset(Entity entity, float offsetX, float offsetY) {
        ColliderComponent collider = entity.getComponent(ColliderComponent.class);
        collider.setIsoShapeOffset(
                collider.getSides()[ColliderComponent.LEFT_SIDE],
                collider.getSides()[ColliderComponent.RIGHT_SIDE],
                offsetX, offsetY);
    }

    /**
     * Set the shape of an entity's hitbox component.
     *
     * @param entity the entity to change the hitbox of.
     * @param leftSides the length of the bottom left side of the hitbox (and top right), in tile units.
     * @param rightSides the length of the bottom right side of the hitbox (and top left), in tile units.
     */
    public static void setHitboxShape(Entity entity, float leftSides, float rightSides) {
        entity.getComponent(HitboxComponent.class)
              .setIsoShape(leftSides, rightSides);
    }

    /**
     * Set the offset for an entity's hitbox component.
     *
     * @param entity the entity to change the hitbox of.
     * @param offsetX pixel offset in the x direction.
     * @param offsetY pixel offset in the y direction.
     */
    public static void setHitboxOffset(Entity entity, float offsetX, float offsetY) {
        ColliderComponent hitbox = entity.getComponent(HitboxComponent.class);
        hitbox.setIsoShapeOffset(
                hitbox.getSides()[ColliderComponent.LEFT_SIDE],
                hitbox.getSides()[ColliderComponent.RIGHT_SIDE],
                offsetX, offsetY);
    }

    public static void rotateCollider(Entity entity) {
        float[] size = entity.getComponent(ColliderComponent.class).getSides();
        float[] offset = entity.getComponent(ColliderComponent.class).getOffset();

        float newLeft = size[ColliderComponent.RIGHT_SIDE];
        float newRight = size[ColliderComponent.LEFT_SIDE];
        float newOffX = offset[ColliderComponent.X_OFFSET] * -1;
        float newOffY = offset[ColliderComponent.Y_OFFSET];

        setColliderShape(entity, newLeft, newRight);
        setColliderOffset(entity, newOffX, newOffY);
    }

    public static void rotateHitbox(Entity entity) {
        float[] size = entity.getComponent(HitboxComponent.class).getSides();
        float[] offset = entity.getComponent(HitboxComponent.class).getOffset();

        float newLeft = size[ColliderComponent.RIGHT_SIDE];
        float newRight = size[ColliderComponent.LEFT_SIDE];
        float newOffX = offset[ColliderComponent.X_OFFSET] * -1;
        float newOffY = offset[ColliderComponent.Y_OFFSET];

        setHitboxShape(entity, newLeft, newRight);
    }

  private PhysicsUtils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
