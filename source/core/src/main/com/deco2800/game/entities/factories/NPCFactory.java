package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.ai.components.AITaskComponent;
import com.deco2800.game.entities.components.CombatStatsComponent;
import com.deco2800.game.entities.components.interactions.Actions.MumActions;
import com.deco2800.game.ai.tasks.ChaseTask;
import com.deco2800.game.ai.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.MumConfig;
import com.deco2800.game.entities.configs.NPCConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.components.AnimationRenderComponent;
import com.deco2800.game.generic.ServiceLocator;

/**
 * Factory to create non-playable character (NPC) entities with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
@SuppressWarnings("unused")
public class NPCFactory {

  private static final NPCConfigs configs =
      FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

  /**
   * Creates a mum entity.
   *
   * @return entity
   */
  public static Entity createMum(String[] assets) {
    MumConfig config = configs.mum;
    return createBaseNPC(ServiceLocator.getHome().getActiveFloor().getPlayer(), assets)
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.stamina))
            .addComponent(new MumActions());
  }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createBaseNPC(Entity target, String[] assets) {
    // Set npc to have base physics components
    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC));
    PhysicsUtils.setScaledCollider(npc, 0.5f, 0.5f);
    PhysicsUtils.setScaledHitbox(npc, 1.1f, 1.1f);
    // Set npc to have base AI component
    npc.addComponent(new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new ChaseTask(target, 10, 3f, 4f)));
    // Set npc to have a base render component
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
      npc.addComponent(animator);
    }

    return npc;
  }

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
