package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.entities.components.interactions.Actions.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.SingleUse;
import com.deco2800.game.generic.ResourceService;
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
@SuppressWarnings({"unused", "UnnecessaryLocalVariable"})
public class ObstacleFactory {

  public static Entity createWall(String[] assets) {
    final float wallScale = 1f;
    Entity wall = createBaseObstacle(assets, BodyType.StaticBody);
    wall.setScale(wallScale, wallScale);
    // Set the collision box of the wall to the full wall size
    PhysicsUtils.setScaledCollider(wall, wallScale, wallScale);
    return wall;
  }

  public static Entity createBed(String[] assets) {
    Entity bed = createBaseInteractable(assets, BodyType.StaticBody)
            .addComponent(new BedActions());
    return bed;
  }

  public static Entity createDoor(String[] assets) {
    //Entity door = createBaseInteractable(assets, BodyType.StaticBody)
            //.addComponent(new DoorActions());
    Entity door = new Entity();
    return door;
  }

  public static Entity createTv(String[] assets) {
    Entity tv = createBaseInteractable(assets, BodyType.StaticBody)
            .addComponent(new TvActions());
    return tv;
  }

  public static Entity createEnergyDrink(String[] assets) {
    Entity energyDrink = createBaseInteractable(assets, BodyType.DynamicBody)
            .addComponent(new DrinkActions())
            .addComponent(new SingleUse());
    return energyDrink;
  }

  public static Entity createBananaPeel(String[] assets) {
    Entity bananaPeel = createBaseInteractable(assets, BodyType.DynamicBody)
            .addComponent(new BananaPeelActions());
    return bananaPeel;
  }

  public static Entity createPuddle(String[] assets){
    Entity puddle = createBaseInteractable(assets, BodyType.StaticBody).addComponent(new BananaPeelActions());
    puddle.getComponent(PhysicsComponent.class).dispose();
    return puddle;
  }

  public static Entity createBookcase(String[] assets) {
    Entity bookcase = createBaseObstacle(assets, BodyType.StaticBody);
    bookcase.setScale(2f,2f);
    PhysicsUtils.realignScaledCollider(bookcase,0.5f,0.5f, PhysicsComponent.AlignX.LEFT, PhysicsComponent.AlignY.CENTER);
    //System.out.printf("x = %d, y = %d\n", bookcase.getScale().x, bookcase.getScale().y);
    return bookcase;
  }

  public static Entity createLounge(String[] assets) {
    Entity lounge = createBaseObstacle(assets, BodyType.StaticBody);
    lounge.getComponent(TextureRenderComponent.class).scaleEntity();
    lounge.setScale(2f,1f);
    PhysicsUtils.realignScaledCollider(lounge, 1f, 2f, PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.BOTTOM);
    return lounge;
  }

  public static Entity createCoffeeTable(String[] assets) {
    Entity coffeeTable = createBaseObstacle(assets, BodyType.StaticBody);
    coffeeTable.setScale(1.5f,1f);
    PhysicsUtils.setScaledCollider(coffeeTable,1f,1f);
    return coffeeTable;
  }

  public static Entity createLamp(String[] assets) {
    return createBaseObstacle(assets, BodyType.StaticBody);
  }

  public static Entity createBaseInteractable(String[] assets, BodyType bodyType) {
    // Set interactable to have a base hitbox component
    Entity interactable = createBaseObstacle(assets, bodyType)
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE));
    PhysicsUtils.setScaledHitbox(interactable, 1f, 1f);
    return interactable;
  }

  public static Entity createBaseObstacle(String[] assets, BodyType bodyType) {
    // Set obstacle to have base physics components
    Entity obstacle = new Entity()
            .addComponent(new PhysicsComponent().setBodyType(bodyType))
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    PhysicsUtils.setScaledCollider(obstacle, 0.5f, 0.5f);
    obstacle.scaleHeight(1f);
    // Set obstacle to have a base render component
    ResourceService resourceService = ServiceLocator.getResourceService();
    if (assets[0].endsWith(".png")) {
      // Asset is a texture, add a TextureRenderComponent
      Texture texture = resourceService.getAsset(assets[0], Texture.class);
      obstacle.addComponent(new TextureRenderComponent(texture));
    } else if (assets[0].endsWith(".atlas")) {
      // Asset is an atlas, add an AnimationRenderComponent
      TextureAtlas textureAtlas = resourceService.getAsset(assets[0], TextureAtlas.class);
      AnimationRenderComponent animator = new AnimationRenderComponent(textureAtlas);
      // Add all atlas regions as animations to the component
      for (TextureAtlas.AtlasRegion region : new Array.ArrayIterator<>(textureAtlas.getRegions())) {
        if (!animator.hasAnimation(region.name)) {
          animator.addAnimation(region.name, 1f);
        }
      }
      obstacle.addComponent(animator);
    }
    return obstacle;
  }
}