package com.deco2800.game.entities.components.interactions;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;

public interface Interactable {
    Entity preCollisionCheck(Fixture me, Fixture other);
    void onPreCollisionStart(Fixture me, Fixture other);
    void onPreCollisionEnd(Fixture me, Fixture other);
    void onCollisionStart(Entity target);
    void onCollisionEnd(Entity target);
    void onInteraction(Entity target);
    void toggleHighlight(boolean shouldHighlight);
}
