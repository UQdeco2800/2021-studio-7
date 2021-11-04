package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.ai.components.AITaskComponent;
import com.deco2800.game.ai.tasks.ChaseTask;
import com.deco2800.game.ai.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.ObjectData;
import com.deco2800.game.maps.ObjectDescription;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.components.AnimationRenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

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
    private static final Logger logger = LoggerFactory.getLogger(NPCFactory.class);

    public static Entity createMumSpawn(ObjectDescription desc, GridPoint2 worldPos) {
        ServiceLocator.getHome().getFloor().stashMumSpawnPosition(worldPos);
        return null;
    }

    public static Entity createMumTarget(ObjectDescription desc, GridPoint2 worldPos) {
        ServiceLocator.getHome().getFloor().stashMumTargetPosition(worldPos);
        return null;
    }

    public static Entity createMum(ObjectDescription desc, GridPoint2 worldPos) {
        return createNPC(desc, worldPos);
    }

    public static Entity createCat(ObjectDescription desc, GridPoint2 worldPos) {
        ObjectData data = desc.getData();
        Entity cat = ObjectFactory.createInteractive(desc, worldPos)
            .addComponent(new AITaskComponent()
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                .addTask(new ChaseTask(ServiceLocator.getHome().getScreen().getPlayer(), 10, 0.5f, 1f)));
        cat.getComponent(PhysicsMovementComponent.class).setTwoDCharacter();
        return cat;
    }

    public static Entity createNPC(ObjectDescription desc, GridPoint2 worldPos) {
        ObjectData data = desc.getData();

        // Set player to have base physics components
        Entity npc = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC));
        PhysicsUtils.setColliderShape(npc, data.getColliderScale().x, data.getColliderScale().y);
        PhysicsUtils.setColliderOffset(npc, data.getColliderOffset().x, data.getColliderOffset().y);
        PhysicsUtils.setHitboxShape(npc, data.getHitboxScale().x, data.getHitboxScale().y);
        PhysicsUtils.setHitboxOffset(npc, data.getHitboxOffset().x, data.getHitboxOffset().y);

        // Set player to have a base render component
        ResourceService resourceService = ServiceLocator.getResourceService();
        String asset = data.getAssets()[desc.getNumRotations() % data.getAssets().length];
        if (asset.endsWith(".atlas")) {
            // Asset is an atlas, add an AnimationRenderComponent
            TextureAtlas textureAtlas = resourceService.getAsset(asset, TextureAtlas.class);
            AnimationRenderComponent animator = new AnimationRenderComponent(textureAtlas);
            // Add all atlas regions as animations to the component
            for (TextureAtlas.AtlasRegion region : new Array.ArrayIterable<>(textureAtlas.getRegions())) {
                if (!animator.hasAnimation(region.name)) {
                    animator.addAnimation(region.name, 0.1f, Animation.PlayMode.LOOP);
                }
            }
            npc.addComponent(animator);
        }

        try {
            for (Class<? extends Component> component : data.getMiscComponents()) {
                npc.addComponent(component.getDeclaredConstructor().newInstance());
            }
        } catch (Exception e) {
            logger.error("Couldn't add component from {}", Arrays.toString(data.getMiscComponents()));
        }

        return npc;
    }

    private NPCFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
