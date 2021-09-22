package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.entities.components.interactions.Actions.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.SingleUse;
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
    PhysicsUtils.setScaledHitbox(bed, 1.1f, 1.1f);

    return bed;
  }
  /**
   * Creates a door entity.
   * @return this door entity
   */
  public static Entity createDoor() {
    Entity door = new Entity();

    AnimationRenderComponent doorAnimations = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/objects/door/door_animationL.atlas",
                    TextureAtlas.class));
    doorAnimations.addAnimation("door_close_left", 1f);
    doorAnimations.addAnimation("Door_left_highlighted", 1f);
    doorAnimations.addAnimation("Door_left_highlihted_d", 1f);
    doorAnimations.addAnimation("Door_open_left", 1f);

    door.addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(doorAnimations)
            .addComponent(new DoorActions());

    door.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    door.scaleHeight(1.0f);
    PhysicsUtils.setScaledCollider(door, 0.5f, 0.5f);
    PhysicsUtils.setScaledHitbox(door, 1f, 1f);

    return door;
  }

  public static Entity createTV() {
    Entity tv = new Entity();

    AnimationRenderComponent tvAnimations = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/objects/tv/TV_animationL.atlas",
                    TextureAtlas.class));
    tvAnimations.addAnimation("TV_offL", 1f);
    tvAnimations.addAnimation("TV_offL2", 1f);
    tvAnimations.addAnimation("TV_ONA", 1f);
    tvAnimations.addAnimation("TV_ONB", 1f);
    tvAnimations.addAnimation("TV_ONC", 1f);

    tv.addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(tvAnimations)
            .addComponent(new tvActions());

    tv.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tv.scaleHeight(1.0f);
    PhysicsUtils.setScaledCollider(tv, 0.5f, 0.5f);
    PhysicsUtils.setScaledHitbox(tv, 1f, 1f);

    return tv;
  }

  public static Entity createEnergyDrink(){
    Entity energyDrink = new Entity();

    AnimationRenderComponent drinkAnimations = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/objects/energy_drink/energy.atlas",
                    TextureAtlas.class));
            drinkAnimations.addAnimation("energy_highlight", 1f);
            drinkAnimations.addAnimation("energy", 1f);

    energyDrink.addComponent(drinkAnimations)
            .addComponent(new PhysicsComponent().setBodyType(BodyType.DynamicBody))
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(new DrinkActions())
            .addComponent(new SingleUse());

    energyDrink.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(energyDrink, 0.5f, 0.5f);
    PhysicsUtils.setScaledHitbox(energyDrink, 1f, 1f);

    return energyDrink;

  }

  public static Entity createBananaPeel(){
    Entity bananaPeel = new Entity();

    AnimationRenderComponent peel = new AnimationRenderComponent(ServiceLocator.getResourceService().getAsset("images/objects" +
            "/banana_peel/banana.atlas",TextureAtlas.class));
    peel.addAnimation("banana_peel", 1f);

    bananaPeel.addComponent(peel)
            .addComponent(new PhysicsComponent().setBodyType(BodyType.DynamicBody))
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(new BananaPeelActions());
    bananaPeel.scaleHeight(1f);
    PhysicsUtils.setScaledCollider(bananaPeel, 0.5f, 0.5f);
    PhysicsUtils.setScaledHitbox(bananaPeel, 1f, 1f);

    return bananaPeel;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}

