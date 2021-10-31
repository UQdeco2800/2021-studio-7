package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.components.AITaskComponent;
import com.deco2800.game.ai.tasks.ChaseTask;
import com.deco2800.game.ai.tasks.MumWaitTask;
import com.deco2800.game.ai.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.ObjectData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;

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

    public static Entity createMum(ObjectData data, int numRotations, GridPoint2 worldPos) {
        Entity mum = createNPC(data, numRotations, worldPos)
            .addComponent(new AITaskComponent()
                .addTask(new MumWaitTask())
                .addTask(new ChaseTask(ServiceLocator.getHome().getScreen().getPlayer(), 10, 5f, 8f)));
        return mum;
    }

    public static Entity createCat(ObjectData data, int numRotations, GridPoint2 worldPos) {
        Entity cat = ObjectFactory.createInteractive(data, numRotations, worldPos)
            .addComponent(new AITaskComponent()
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                .addTask(new ChaseTask(ServiceLocator.getHome().getScreen().getPlayer(), 10, 0.5f, 1f)));
        cat.getComponent(PhysicsMovementComponent.class).setTwoDCharacter();
        return cat;
    }

    public static Entity createNPC(ObjectData data, int numRotations, GridPoint2 worldPos) {
        Entity npc = ObjectFactory.createInteractive(data, numRotations, worldPos);
        npc.getComponent(HitboxComponent.class).setLayer(PhysicsLayer.NPC);
        return npc;
    }

    private NPCFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
