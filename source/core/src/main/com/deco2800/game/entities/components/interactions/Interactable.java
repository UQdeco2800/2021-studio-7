package com.deco2800.game.entities.components.interactions;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;

public interface Interactable {
    Entity preCollisionCheck(Fixture me, Fixture other);
    void onCollisionStart(Fixture me, Fixture other);
    void onCollisionEnd(Fixture me, Fixture other);
    void onInteraction(Entity target);
}
