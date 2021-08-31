package com.deco2800.game.components;

import com.badlogic.gdx.Gdx;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.components.PhysicsComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class CombatStatsComponentTest {

  private PhysicsComponent physicsComponent;
  private CombatStatsComponent combatStatsComponent;

  @Test
  void shouldSetGetHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 100);
    assertEquals(100, combat.getHealth());

    combat.setHealth(150);
    assertEquals(150, combat.getHealth());

    combat.setHealth(-50);
    assertEquals(0, combat.getHealth());
  }

  @Test
  void shouldSetGetStamina() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 100);
    assertEquals(100, combat.getStamina());

    combat.setStamina(150);
    assertEquals(100, combat.getStamina());

    combat.setStamina(-50);
    assertEquals(0, combat.getStamina());
  }

  @Test
  void shouldAddStamina() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 0);
    combat.changeStamina(1);
    assertEquals(1, combat.getStamina());

    combat.changeStamina(-1);
    assertEquals(0, combat.getStamina());

    combat.changeStamina(101);
    assertEquals(100, combat.getStamina());
  }

  @Test
  void shouldCheckIsDead() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 100);
    assertFalse(combat.isDead());

    combat.setHealth(0);
    assertTrue(combat.isDead());
  }

  @Test
  void shouldAddHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 100);
    combat.addHealth(-500);
    assertEquals(0, combat.getHealth());

    combat.addHealth(100);
    combat.addHealth(-20);
    assertEquals(80, combat.getHealth());
  }

  @Test
  void shouldSetGetBaseAttack() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 100);
    assertEquals(20, combat.getBaseAttack());

    combat.setBaseAttack(150);
    assertEquals(150, combat.getBaseAttack());

    combat.setBaseAttack(-50);
    assertEquals(150, combat.getBaseAttack());
  }
}
