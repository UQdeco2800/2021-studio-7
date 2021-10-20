package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.ai.components.AITaskComponent;
import com.deco2800.game.ai.tasks.SlipTask;
import com.deco2800.game.entities.components.CombatStatsComponent;
import com.deco2800.game.entities.components.ScoreComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.entities.components.player.PlayerStatDisplay;
import com.deco2800.game.entities.components.player.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.components.AnimationRenderComponent;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
@SuppressWarnings({"unused", "UnnecessaryLocalVariable"})
public class PlayerFactory {
  private static final Logger logger = LoggerFactory.getLogger(PlayerFactory.class);
  private static final PlayerConfig stats =
      FileLoader.readClass(PlayerConfig.class, "configs/player.json");

  public static Entity createPlayer(String[] assets) {
    Entity player = createBasePlayer(assets)
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack, stats.stamina))
            .addComponent(new PlayerStatDisplay())
            .addComponent(new InteractionControllerComponent())
            .addComponent(new PlayerActions())
            .addComponent(new ScoreComponent(2500))
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new AITaskComponent().addTask(new SlipTask()));

    player.setScale(0.8f,0.8f);
    return player;
  }

  public static Entity createBasePlayer(String[] assets) {
    // Set player to have base physics components
    Entity player = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setDensity(1.5f))
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
    PhysicsUtils.setScaledCollider(player, 0.5f, 0.5f);
    PhysicsUtils.setColliderShape(player, 0.5f, 0.5f);
    PhysicsUtils.setScaledHitbox(player, 1f, 1f);

    // Set player to have a base input component
    InputService inputService = ServiceLocator.getInputService();
    player.addComponent(inputService.getInputFactory().createForPlayer());

    // Set player to have a base render component
    ResourceService resourceService = ServiceLocator.getResourceService();
    if (assets[0].endsWith(".atlas")) {
      // Asset is an atlas, add an AnimationRenderComponent
      TextureAtlas textureAtlas = resourceService.getAsset(assets[0], TextureAtlas.class);
      AnimationRenderComponent animator = new AnimationRenderComponent(textureAtlas);
      // Add all atlas regions as animations to the component
      for (TextureAtlas.AtlasRegion region : new Array.ArrayIterable<>(textureAtlas.getRegions())) {
        if (!animator.hasAnimation(region.name)) {
          animator.addAnimation(region.name, 0.1f, Animation.PlayMode.LOOP);
        }
      }
      player.addComponent(animator);
    }

    return player;
  }

  public static String getAtlas() {
    File input = new File("configs/currentCharacterAtlas.txt");
    String line = null;
    try (BufferedReader br = new BufferedReader(new FileReader(input))) {
      line = br.readLine();
    } catch (IOException e) {
      logger.error("Could not read currentCharacterAtlas.txt");
    }
    return line;
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
