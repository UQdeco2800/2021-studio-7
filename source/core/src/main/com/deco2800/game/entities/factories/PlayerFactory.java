package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.entities.components.CombatStatsComponent;
import com.deco2800.game.entities.components.ScoreComponent;
import com.deco2800.game.entities.components.player.InventoryComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.entities.components.player.PlayerStatsDisplay;
import com.deco2800.game.entities.components.player.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.components.InputComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.components.AnimationRenderComponent;
import com.deco2800.game.generic.ServiceLocator;
import java.io.*;

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public class PlayerFactory {
  private static final PlayerConfig stats =
      FileLoader.readClass(PlayerConfig.class, "configs/player.json");

  /**
   * Create a player entity.
   * @return entity
   */
  public static Entity createPlayer() {
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForPlayer();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset(getAtlas(), TextureAtlas.class));
    animator.addAnimation("standing_south", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("standing_north", 0.25f, Animation.PlayMode.LOOP);
    animator.addAnimation("standing_west", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("standing_east", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("standing_northeast", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("standing_northwest", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("standing_southeast", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("standing_southwest", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walking_south", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walking_north", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walking_west", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walking_east", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walking_northeast", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walking_northwest", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walking_southeast", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walking_southwest", 0.2f, Animation.PlayMode.LOOP);

    Entity player =
        new Entity()
            .addComponent(animator)
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack, stats.stamina))
            .addComponent(inputComponent)
            .addComponent(new PlayerStatsDisplay())
            .addComponent(new PlayerActions())
            .addComponent(new SurveyorComponent())
            .addComponent(new ScoreComponent(1000));

    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    PhysicsUtils.setScaledHitbox(player, 1.1f, 1.1f);
    player.scaleHeight(0.8f);
    return player;
  }


  public static String getAtlas() {
    BufferedReader br = null;
    try {
      File input = new File("configs/currentCharacterAtlas.txt");
      br = new BufferedReader(new FileReader(input));
      String line = br.readLine();
      return line;

    } catch (Exception e) {
      throw new IllegalStateException("Could not read currentCharacterAtlas.txt");
    }finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }



  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
