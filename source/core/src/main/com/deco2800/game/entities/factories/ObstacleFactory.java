package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.Actions.BananaPeelActions;
import com.deco2800.game.entities.components.interactions.Actions.BedActions;
import com.deco2800.game.entities.components.interactions.Actions.DrinkActions;
import com.deco2800.game.entities.components.interactions.Actions.TvActions;
import com.deco2800.game.entities.components.interactions.Actions.PlaceableBoxActions;
import com.deco2800.game.entities.components.interactions.SingleUse;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.components.AnimationRenderComponent;
import com.deco2800.game.rendering.components.TextureRenderComponent;

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
    bed.setScale(1.5f, 1f);
    PhysicsUtils.setScaledCollider(bed,1.5f, 1f);
    PhysicsUtils.setColliderShape(bed, 2f, 2.5f);
    return bed;
  }

  public static Entity createDoor(String[] assets) {
//    Entity door = createBaseInteractable(assets, BodyType.StaticBody)
//            .addComponent(new DoorActions());
    Entity door = new Entity();
    return door;
  }

  public static Entity createPlaceableBox(String[] assets) {
    Entity box = createBaseInteractable(assets, BodyType.StaticBody)
            .addComponent(new PlaceableBoxActions());
    return box;
  }

  public static Entity createTv(String[] assets) {
    Entity tv = createBaseChore(assets, BodyType.StaticBody, ChoreList.TV)
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
    Entity puddle = createBaseInteractable(assets, BodyType.KinematicBody).addComponent(new BananaPeelActions());
    puddle.setScale(1f, 0.5f);
    PhysicsUtils.setScaledCollider(puddle,1f,1f);
    return puddle;
  }

  public static Entity createBookcase(String[] assets) {
    Entity bookcase = createBaseObstacle(assets, BodyType.StaticBody);
    bookcase.setScale(2f,2f);
    PhysicsUtils.realignScaledCollider(bookcase,0.5f,0.5f, PhysicsComponent.AlignX.LEFT, PhysicsComponent.AlignY.CENTER);
    PhysicsUtils.setColliderShape(bookcase, 0.5f, 0.5f);
    return bookcase;
  }

  public static Entity createBath(String[] assets) {
    Entity bath = createBaseObstacle(assets, BodyType.StaticBody);
    bath.setScale(1.5f,1.5f);
    PhysicsUtils.setScaledCollider(bath,1f,1f);
    return bath;
  }

  public static Entity createLounge(String[] assets) {
    Entity lounge = createBaseObstacle(assets, BodyType.StaticBody);
    lounge.getComponent(TextureRenderComponent.class).scaleEntity();
    lounge.setScale(2f,1f);
    PhysicsUtils.realignScaledCollider(lounge, 1f, 2f, PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.BOTTOM);
    PhysicsUtils.setColliderShape(lounge, 2f, 2.5f);
    return lounge;
  }

  public static Entity createDesk(String[] assets) {
    Entity desk = createBaseObstacle(assets, BodyType.StaticBody);
    desk.getComponent(TextureRenderComponent.class).scaleEntity();
    desk.setScale(2f,2f);
    PhysicsUtils.setScaledCollider(desk,2f,2f);
    PhysicsUtils.setColliderShape(desk, 2f, 2.5f);

    return desk;
  }

  public static Entity createCoffeeTable(String[] assets) {
    Entity coffeeTable = createBaseObstacle(assets, BodyType.StaticBody);
    coffeeTable.getComponent(TextureRenderComponent.class).scaleEntity();
    coffeeTable.setScale(1.5f,1f);
    PhysicsUtils.setScaledCollider(coffeeTable,1f,1f);
    return coffeeTable;
  }

  public static Entity createLamp(String[] assets) {
    Entity lamp = createBaseObstacle(assets, BodyType.StaticBody);
    lamp.getComponent(TextureRenderComponent.class).scaleEntity();
    // New Part
    PhysicsUtils.setColliderShape(lamp, 0.5f, 0.5f);
    //
    lamp.setScale(0.5f,1f);
    return lamp;
  }

  public static Entity createChair(String[] assets) {
    Entity chair = createBaseObstacle(assets, BodyType.StaticBody);
    chair.setScale(1.25f, 1.25f);
    PhysicsUtils.setScaledCollider(chair,1.5f, 1.5f);
    PhysicsUtils.setColliderShape(chair, 0.5f, 0.5f);
    return chair;
  }

  public static Entity createSideTable(String[] assets) {
    Entity side = createBaseObstacle(assets, BodyType.StaticBody);
    side.setScale(0.5f, 0.5f);
    PhysicsUtils.setScaledCollider(side, 0.5f, 0.5f);
    return side;
  }

  public static Entity createFridge(String[] assets) {
    Entity fridge = createBaseObstacle(assets, BodyType.StaticBody);
    fridge.setScale(2f,2f);
    PhysicsUtils.realignScaledCollider(fridge,0.5f,0.5f, PhysicsComponent.AlignX.LEFT, PhysicsComponent.AlignY.CENTER);
    return fridge;
  }

  public static Entity createCabinet(String[] assets) {
    Entity cab = createBaseObstacle(assets, BodyType.StaticBody);
    cab.setScale(2.5f, 2f);
    PhysicsUtils.setScaledCollider(cab, 1f,1f);
    return cab;
  }

  public static Entity createBin(String[] assets) {
    Entity bin = createBaseObstacle(assets, BodyType.StaticBody);
    bin.setScale(0.5f, 0.5f);
    PhysicsUtils.setScaledCollider(bin, 0.5f, 0.5f);
    return bin;
  }

  /**
   * Creates the object as a chore, and registers it as a chore to the ChoreController
   * @param assets the image and atlas assets of this object
   * @param bodyType Static, kinematic or dynamic body type
   * @param object The ChoreList ID of this object
   * @return The new entity of this obstacle
   */
  public static Entity createBaseChore(String[] assets, BodyType bodyType, ChoreList object) {
    Entity entity = createBaseInteractable(assets, bodyType);
    ServiceLocator.getChoreController().addChore(entity, object);
    return entity;
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
    if (assets.length <= 0) {
      return obstacle;
    }
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
          if (region.name.equals("TV_on1") || region.name.equals("TV_onh1")) {
            animator.addAnimation(region.name, 0.1f, Animation.PlayMode.LOOP);
          } else {
            animator.addAnimation(region.name, 1f);
          }
        }
      }
      obstacle.addComponent(animator);
    }
    return obstacle;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}

