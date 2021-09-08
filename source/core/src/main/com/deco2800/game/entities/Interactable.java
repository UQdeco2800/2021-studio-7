package com.deco2800.game.entities;

import com.badlogic.gdx.physics.box2d.Fixture;

public interface Interactable {
    public void onCollisionStart(Fixture selfFix, Fixture otherFix);
    public void onCollisionEnd(Fixture selfFix, Fixture otherFix);
}
