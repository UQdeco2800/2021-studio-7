package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.entities.components.interactions.Actions.BedActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.components.AnimationRenderComponent;
import com.deco2800.game.rendering.components.TextureRenderComponent;
import com.deco2800.game.generic.ServiceLocator;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class ObstacleFactory {

  /**
   * Creates a tree entity.
   * @return entity
   */
  public static Entity createTree() {
    Entity tree =
        new Entity()
            .addComponent(new TextureRenderComponent("images/objects/tree/tree.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(tree, 0.5f, 0.2f);
    return tree;
  }

  /**
   * Creates an invisible physics wall.
   * @param width Wall width in world units
   * @param height Wall height in world units
   * @return Wall entity of given width and height
   */
  public static Entity createWall(float width, float height, String texture) {
    Entity wall = new Entity()
            .addComponent(new TextureRenderComponent(texture))
            .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    wall.setScale(width, height);
    return wall;
  }

  /**
   * Creates a bed entity which can be interacted with by the player
   * @return This bed entity
   */
  public static Entity createBed() {
    Entity bed = new Entity();

    AnimationRenderComponent bedAnimations = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/objects/bed/bed.atlas",
                    TextureAtlas.class));
    bedAnimations.addAnimation("bed", 1f);
    bedAnimations.addAnimation("bed_highlight", 1f);

    bed.addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(bedAnimations)
            .addComponent(new BedActions());

    bed.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    bed.scaleHeight(1.0f);
    PhysicsUtils.setScaledCollider(bed, 0.5f, 0.5f);
    PhysicsUtils.setScaledHitbox(bed, 1f, 1f);

    //bed.getComponent(AnimationRenderComponent.class).scaleEntity();
    return bed;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}

