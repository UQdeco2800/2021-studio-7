package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.entities.components.CombatStatsComponent;
import com.deco2800.game.entities.components.ScoreComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.entities.components.player.PlayerStatsDisplay;
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
import com.deco2800.game.rendering.components.AnimationRenderComponent;
import com.deco2800.game.generic.ServiceLocator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
@SuppressWarnings({"unused", "UnnecessaryLocalVariable"})
public class PlayerFactory {
  private static final PlayerConfig stats =
      FileLoader.readClass(PlayerConfig.class, "configs/player.json");

  public static Entity spawnPlayer(GridPoint2 position, String[] assets) {
    Entity player = createBasePlayer(assets)
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack, stats.stamina))
            .addComponent(new PlayerStatsDisplay())
            .addComponent(new PlayerActions())
            .addComponent(new SurveyorComponent())
            .addComponent(new ScoreComponent(1000));
    return player;
  }

  public static Entity createBasePlayer(String[] assets) {
    // Set player to have base physics components
    Entity player = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setDensity(1.5f))
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    PhysicsUtils.setScaledHitbox(player, 1.1f, 1.1f);

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
      for (TextureAtlas.AtlasRegion region : textureAtlas.getRegions()) {
        animator.addAnimation(region.name, 0.1f, Animation.PlayMode.LOOP);
      }
      player.addComponent(animator);
    }

    return player;
  }

  public static String getAtlas() {
    try {
      File input = new File("configs/currentCharacterAtlas.txt");
      BufferedReader br = new BufferedReader(new FileReader(input));
      String line = br.readLine();
      return line;

    } catch (Exception e) {
      throw new IllegalStateException("Could not read currentCharacterAtlas.txt");
    }
  }
}
