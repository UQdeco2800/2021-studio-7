package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.InteractableComponent;
// import com.deco2800.game.components.npc.InteractableComponentController;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

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
            .addComponent(new TextureRenderComponent("images/tree.png"))
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
  public static Entity createWall(float width, float height) {
    Entity wall = new Entity()
        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    wall.setScale(width, height);
    return wall;
  }

  /**
   * //TODO
   * @return bed entity
   */
  public static Entity createBed(){
    // New from here - Pranay
//    AnimationRenderComponent bedAnimation =
//            new AnimationRenderComponent(
//                    ServiceLocator.getResourceService().getAsset("images/bed.atlas", TextureAtlas.class));
//    bedAnimation.addAnimation("bed", 0.1f, Animation.PlayMode.LOOP);
//    bedAnimation.addAnimation("bed_highlight", 0.1f, Animation.PlayMode.LOOP);

    // to here

    Entity bed = new Entity()
            .addComponent(new TextureRenderComponent(("images/bed_inactive" +
                      ".png")))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent((new InteractableComponent(PhysicsLayer.PLAYER)));
//            .addComponent(bedAnimation) // Added component for the animation of th bed
//            .addComponent(new InteractableComponentController()); // Added to trigger animation
    bed.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    bed.getComponent(TextureRenderComponent.class).scaleEntity();
    bed.scaleHeight(1.0f);
    PhysicsUtils.setScaledCollider(bed,0.5f, 0.5f);

    // bed.getComponent(AnimationRenderComponent.class).scaleEntity();
    return bed;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}

