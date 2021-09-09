package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.entities.components.CombatStatsComponent;
import com.deco2800.game.entities.components.SurveyorActions;
import com.deco2800.game.entities.components.player.InventoryComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.entities.components.player.PlayerObjectInteractions;
import com.deco2800.game.entities.components.player.PlayerAnimationController;
import com.deco2800.game.entities.components.player.PlayerStatsDisplay;
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
import com.deco2800.game.rendering.components.TextureRenderComponent;

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
                    ServiceLocator.getResourceService().getAsset("images/characters/boy_01/boy_01.atlas", TextureAtlas.class));
    animator.addAnimation("standing_south", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("standing_north", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("standing_west", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("standing_east", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("walking_south", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("walking_north", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("walking_west", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("walking_east", 0.1f, Animation.PlayMode.LOOP);

    Entity player =
        new Entity()
            .addComponent(animator).addComponent(new PlayerAnimationController())
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new PlayerActions())
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack, stats.stamina))
            .addComponent(new InventoryComponent(stats.gold))
            .addComponent(inputComponent)
            .addComponent(new PlayerStatsDisplay())
            .addComponent(new PlayerObjectInteractions());


    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    return player;
  }

  public static Entity createSurveyor(Entity player) {
    Entity surveyor = new Entity();

    surveyor.addComponent(new TextureRenderComponent("images/objects/tree/tree.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new SurveyorActions());

    surveyor.getComponent(TextureRenderComponent.class).scaleEntity();
    surveyor.setScale(player.getScale());
    surveyor.getComponent(HitboxComponent.class).setAsBoxAligned(
                    surveyor.getScale().cpy().scl(player.getScale().x, player.getScale().y),
                    PhysicsComponent.AlignX.CENTER, PhysicsComponent.AlignY.BOTTOM);
    return surveyor;
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
