package com.deco2800.game.entities.components.player;

import com.deco2800.game.entities.components.InteractableComponent;
import com.deco2800.game.entities.Entity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerObjectInteractionsTest {

    @Test
    void addObject() {
        Entity player =
                new Entity().addComponent(new PlayerObjectInteractions());
        Entity entity1 =
                new Entity().addComponent(new InteractableComponent(player));
        player.getComponent(PlayerObjectInteractions.class)
                .addObject(entity1);
        assertEquals(1, player.getComponent(PlayerObjectInteractions.class).getInteractableObjects().size());
        Object[] arrayListContents =
                player.getComponent(PlayerObjectInteractions.class).getInteractableObjects().toArray();
        assertArrayEquals(new Object[]{entity1}, arrayListContents);
        player.getComponent(PlayerObjectInteractions.class)
                .addObject(entity1);
        assertEquals(1, player.getComponent(PlayerObjectInteractions.class).getInteractableObjects().size());
        assertArrayEquals(new Object[]{entity1}, arrayListContents);
        Entity entity2 = new Entity();
        player.getComponent(PlayerObjectInteractions.class)
                .addObject(entity2);
        assertEquals(1, player.getComponent(PlayerObjectInteractions.class).getInteractableObjects().size());
        arrayListContents =
                player.getComponent(PlayerObjectInteractions.class).getInteractableObjects().toArray();
        assertArrayEquals(new Object[]{entity1}, arrayListContents);
        Entity entity3 =
                new Entity().addComponent(new InteractableComponent(player));
        player.getComponent(PlayerObjectInteractions.class)
                .addObject(entity3);
        assertEquals(2,
                player.getComponent(PlayerObjectInteractions.class).getInteractableObjects().size());
    }


    @Test
    void removeObject() {
        Entity player =
                new Entity().addComponent(new PlayerObjectInteractions());
        Entity entity1 =
                new Entity().addComponent(new InteractableComponent(player));
        Entity entity2 =
                new Entity().addComponent(new InteractableComponent(player));
        player.getComponent(PlayerObjectInteractions.class)
                .addObject(entity1);
        player.getComponent(PlayerObjectInteractions.class)
                .addObject(entity2);
        assertEquals(2,
                player.getComponent(PlayerObjectInteractions.class).getInteractableObjects().size());
        Object[] arrayListContents =
                player.getComponent(PlayerObjectInteractions.class).getInteractableObjects().toArray();
        assertArrayEquals(new Object[]{entity1, entity2}, arrayListContents);
        player.getComponent(PlayerObjectInteractions.class).removeObject(entity1);
        assertEquals(1,
                player.getComponent(PlayerObjectInteractions.class).getInteractableObjects().size());
        arrayListContents =
                player.getComponent(PlayerObjectInteractions.class).getInteractableObjects().toArray();
        assertArrayEquals(new Object[]{entity2}, arrayListContents);
        player.getComponent(PlayerObjectInteractions.class).removeObject(entity1);
        assertEquals(1,
                player.getComponent(PlayerObjectInteractions.class).getInteractableObjects().size());
        arrayListContents =
                player.getComponent(PlayerObjectInteractions.class).getInteractableObjects().toArray();
        assertArrayEquals(new Object[]{entity2}, arrayListContents);
        player.getComponent(PlayerObjectInteractions.class).removeObject(entity2);
        assertEquals(0,
                player.getComponent(PlayerObjectInteractions.class).getInteractableObjects().size());
    }

    @Test
    void isObjectInteractive(){
        Entity player =
                new Entity().addComponent(new PlayerObjectInteractions());
        Entity entity1 =
                new Entity().addComponent(new InteractableComponent(player));
        Entity entity2 = new Entity();
        assertEquals(true,
                player.getComponent(PlayerObjectInteractions.class).isObjectInteractive(entity1));
        assertEquals(false,
                player.getComponent(PlayerObjectInteractions.class).isObjectInteractive(entity2));
    }
}