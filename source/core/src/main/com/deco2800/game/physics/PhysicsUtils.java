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
     * Set the shape of a collider component or subclass. Shape is isometric.
     *
     * @param entity Entity to set the component shape for
     * @param left the length of the bottom left side of the component (and top right), in tile units.
     * @param right the length of the bottom right side of the component (and top left), in tile units.
     * @param type the component class to modify
     */
    private static void setComponentShape(Entity entity, float left, float right, Class type) {
        ColliderComponent component = (ColliderComponent) entity.getComponent(type);
        component.setIsoShape(left, right);
    }

    /**
     * Set the offset of a collider component or subclass.
     *
     * @param entity Entity to set the component shape for
     * @param offX pixel offset in the x direction.
     * @param offY pixel offset in the y direction.
     * @param type the component class to modify
     */
    private static void setComponentOffset(Entity entity, float offX, float offY, Class type) {
        ColliderComponent component = (ColliderComponent) entity.getComponent(type);
        component.setIsoShape(
                component.getSides()[ColliderComponent.LEFT_SIDE],
                component.getSides()[ColliderComponent.RIGHT_SIDE],
                offX, offY);
    }

    /**
     * Rotate a component. More akin to mirroring across the vertical axis.
     *
     * @param entity Entity to set the component shape for
     * @param type the component class to modify
     */
    private static void rotateComponent(Entity entity, Class type) {
        ColliderComponent component = (ColliderComponent) entity.getComponent(type);

        float[] size = component.getSides();
        float[] offset = component.getOffset();

        float newLeft = size[ColliderComponent.RIGHT_SIDE];
        float newRight = size[ColliderComponent.LEFT_SIDE];
        float newOffX = -1 * offset[ColliderComponent.X_OFFSET];
        float newOffY = offset[ColliderComponent.Y_OFFSET];

        setComponentShape(entity, newLeft, newRight, type);
        setComponentOffset(entity, newOffX, newOffY, type);
    }

    /**
     * Set the shape of an entity's collider component.
     *
     * @param entity the entity to change the collider of.
     * @param leftSides the length of the bottom left side of the collider (and top right), in tile units.
     * @param rightSides the length of the bottom right side of the collider (and top left), in tile units.
     */
    public static void setColliderShape(Entity entity, float leftSides, float rightSides) {
        setComponentShape(entity, leftSides, rightSides, ColliderComponent.class);
    }

    /**
     * Set the shape of an entity's hitbox component.
     *
     * @param entity the entity to change the hitbox of.
     * @param leftSides the length of the bottom left side of the hitbox (and top right), in tile units.
     * @param rightSides the length of the bottom right side of the hitbox (and top left), in tile units.
     */
    public static void setHitboxShape(Entity entity, float leftSides, float rightSides) {
        setComponentShape(entity, leftSides, rightSides, HitboxComponent.class);
    }

    /**
     * Set the offset for an entity's collider component.
     *
     * @param entity the entity to change the collider of.
     * @param offsetX pixel offset in the x direction.
     * @param offsetY pixel offset in the y direction.
     */
    public static void setColliderOffset(Entity entity, float offsetX, float offsetY) {
        setComponentOffset(entity, offsetX, offsetY, ColliderComponent.class);
    }

    /**
     * Set the offset for an entity's hitbox component.
     *
     * @param entity the entity to change the hitbox of.
     * @param offsetX pixel offset in the x direction.
     * @param offsetY pixel offset in the y direction.
     */
    public static void setHitboxOffset(Entity entity, float offsetX, float offsetY) {
        setComponentOffset(entity, offsetX, offsetY, HitboxComponent.class);
    }

    /**
     * Rotate a collider. More akin to mirroring across the vertical axis.
     *
     * @param entity Entity to set the component shape for
     */
    public static void rotateCollider(Entity entity) {
        rotateComponent(entity, ColliderComponent.class);
    }

    /**
     * Rotate a hitbox. More akin to mirroring across the vertical axis.
     *
     * @param entity Entity to set the component shape for
     */
    public static void rotateHitbox(Entity entity) {
        rotateComponent(entity, HitboxComponent.class);
    }

  private PhysicsUtils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
