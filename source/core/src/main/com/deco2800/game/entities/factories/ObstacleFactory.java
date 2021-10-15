package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.actions.BananaPeelActions;
import com.deco2800.game.entities.components.interactions.actions.BedActions;
import com.deco2800.game.entities.components.interactions.actions.DrinkActions;
import com.deco2800.game.entities.components.interactions.actions.TvActions;
import com.deco2800.game.entities.components.interactions.actions.PlaceableBoxActions;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
@SuppressWarnings({"unused", "UnnecessaryLocalVariable"})
public class ObstacleFactory {
  private static final Logger logger = LoggerFactory.getLogger(com.deco2800.game.entities.factories.ObstacleFactory.class);

  public static Entity createWall(String[] assets) {
    final float wallScale = 1f;
    Entity wall = createBaseObstacle(assets);
    wall.setScale(wallScale, wallScale);
    // Set the collision box of the wall to the full wall size
    PhysicsUtils.setScaledCollider(wall, wallScale, wallScale);
    return wall;
  }

  public static Entity createBed(String[] assets) {
    Entity bed = createBaseInteractable(assets);
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
    Entity box = createBaseInteractable(assets);
    return box;
  }

  public static Entity createTv(String[] assets) {
    Entity tv = createBaseChore(assets);
    return tv;
  }

  public static Entity createEnergyDrink(String[] assets) {
    Entity energyDrink = createBaseChore(assets);
    return energyDrink;
  }

  public static Entity createBananaPeel(String[] assets) {
    Entity bananaPeel = createBaseInteractable(assets);
    return bananaPeel;
  }

  public static Entity createPuddle(String[] assets){
    Entity puddle = createBaseInteractable(assets);
    puddle.setScale(1f,0.5f);
    PhysicsUtils.setScaledCollider(puddle,1f,1f);
    return puddle;
  }

  public static Entity createBookcase(String[] assets) {
    Entity bookcase = createBaseObstacle(assets);
    bookcase.setScale(2f,2f);
    PhysicsUtils.realignScaledCollider(bookcase,0.5f,0.5f, PhysicsComponent.AlignX.LEFT, PhysicsComponent.AlignY.CENTER);
    PhysicsUtils.setColliderShape(bookcase, 0.5f, 0.5f);
    return bookcase;
  }

  public static Entity createBath(String[] assets) {
    Entity bath = createBaseObstacle(assets);
    bath.setScale(1.5f,1.5f);
    PhysicsUtils.setScaledCollider(bath,1f,1f);
    return bath;
  }

  public static Entity createLounge(String[] assets) {
    Entity lounge = createBaseObstacle(assets);
    lounge.getComponent(TextureRenderComponent.class).scaleEntity();
    lounge.setScale(2f,1f);
    PhysicsUtils.realignScaledCollider(lounge, 1f, 2f, PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.BOTTOM);
    PhysicsUtils.setColliderShape(lounge, 2f, 2.5f);
    return lounge;
  }

  public static Entity createDesk(String[] assets) {
    Entity desk = createBaseObstacle(assets);
    desk.getComponent(TextureRenderComponent.class).scaleEntity();
    desk.setScale(2f,2f);
    PhysicsUtils.setScaledCollider(desk,2f,2f);
    PhysicsUtils.setColliderShape(desk, 2f, 2.5f);

    return desk;
  }

  public static Entity createCoffeeTable(String[] assets) {
    Entity coffeeTable = createBaseObstacle(assets);
    coffeeTable.getComponent(TextureRenderComponent.class).scaleEntity();
    coffeeTable.setScale(1.5f,1f);
    PhysicsUtils.setScaledCollider(coffeeTable,1f,1f);
    return coffeeTable;
  }

  public static Entity createLamp(String[] assets) {
    Entity lamp = createBaseObstacle(assets);
    lamp.getComponent(TextureRenderComponent.class).scaleEntity();
    // New Part
    PhysicsUtils.setColliderShape(lamp, 0.5f, 0.5f);
    //
    lamp.setScale(0.5f,1f);
    return lamp;
  }

  public static Entity createChair(String[] assets) {
    Entity chair = createBaseObstacle(assets);
    chair.setScale(1.25f, 1.25f);
    PhysicsUtils.setScaledCollider(chair,1.5f, 1.5f);
    PhysicsUtils.setColliderShape(chair, 0.5f, 0.5f);
    return chair;
  }

  public static Entity createSideTable(String[] assets) {
    Entity side = createBaseObstacle(assets);
    side.setScale(0.5f, 0.5f);
    PhysicsUtils.setScaledCollider(side, 0.5f, 0.5f);
    return side;
  }

  public static Entity createFridge(String[] assets) {
    Entity fridge = createBaseObstacle(assets);
    fridge.setScale(2f,2f);
    PhysicsUtils.realignScaledCollider(fridge,0.5f,0.5f, PhysicsComponent.AlignX.LEFT, PhysicsComponent.AlignY.CENTER);
    return fridge;
  }

  public static Entity createCabinet(String[] assets) {
    Entity cab = createBaseObstacle(assets);
    cab.setScale(2.5f, 2f);
    PhysicsUtils.setScaledCollider(cab, 1f,1f);
    return cab;
  }

  public static Entity createBin(String[] assets) {
    Entity bin = createBaseObstacle(assets);
    bin.setScale(0.5f, 0.5f);
    PhysicsUtils.setScaledCollider(bin, 0.5f, 0.5f);
    return bin;
  }

  /**
   * Creates the object as a chore, and registers it as a chore to the ChoreController
   * @param assets the image and atlas assets of this object
   * @return The new entity of this obstacle
   */
  public static Entity createBaseChore(String[] assets) {
    Entity entity = createBaseInteractable(assets);
    ServiceLocator.getChoreController().addChore(entity, getChoreType(assets[3]));
    return entity;
  }

  public static Entity createBaseInteractable(String[] assets) {
    // Set interactable to have a base hitbox component
    Entity obstacle = createBaseObstacle(assets)
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE));
    addInteraction(assets[2], obstacle);
    PhysicsUtils.setScaledHitbox(obstacle, 1f, 1f);
    return obstacle;
  }

  public static Entity createBaseObstacle(String[] assets) {
    // Set obstacle to have base physics components
    Entity obstacle = new Entity()
            .addComponent(new PhysicsComponent().setBodyType(selectBodyType(assets[1])))
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    PhysicsUtils.setScaledCollider(obstacle, 0.5f, 0.5f);
    //obstacle.scaleHeight(1f);
    if (assets[0] == "") {
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

  private static BodyType selectBodyType(String ID) {
    switch (ID) {
      case "0":
        return BodyType.StaticBody;
      case "1":
        return BodyType.DynamicBody;
      case "2":
        return BodyType.KinematicBody;
      default:
        logger.error("No valid body type was specified");
        return BodyType.StaticBody;
    }
  }

  private static void addInteraction(String interactionID, Entity obstacle) {
    switch (interactionID) {
      case "0":
        logger.error("Interaction ID for non-interaction entity called");
      case "1":
        obstacle.addComponent(new TvActions());
        break;
      case "2":
        obstacle.addComponent(new DrinkActions())
                .addComponent(new SingleUse());
        break;
      case "3":
        obstacle.addComponent(new PlaceableBoxActions());
        break;
      case "4":
        obstacle.addComponent(new BedActions());
        break;
      case "5":
        obstacle.addComponent(new BananaPeelActions());
        break;
    }
  }

  private static ChoreList getChoreType(String choreID) {
    switch (choreID) {
      case "1":
        return ChoreList.TV;
      case "2":
        return ChoreList.DRINK;
      case "3":
        return ChoreList.PUDDLE;
      default:
        logger.debug("Invalid choreID provided");
        return null;
    }
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}

