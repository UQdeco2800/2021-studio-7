package com.deco2800.game.entities.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.entities.components.CombatStatsComponent;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  //private Label healthLabel;
  private Label staminaLabel; //TODO

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    //entity.getEvents().addListener("update_health", this::updatePlayerHealthUI);
    entity.getEvents().addListener("update_stamina", this::updatePlayerStaminaUI);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f).padLeft(5f);

    // Health text
    /*int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = String.format("Health: %d", health);
    healthLabel = new Label(healthText, skin, "large");*/

    // stamina text
    double stamina = entity.getComponent(CombatStatsComponent.class).getStamina();
    CharSequence staminaText = String.format("Stamina: %.0f", stamina);
    staminaLabel = new Label(staminaText, skin, "large");

    //table.add(healthLabel).left();
    //table.row();
    table.add(staminaLabel).left(); //todo
    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's health on the ui.
   * @param health player health
   */
  /*public void updatePlayerHealthUI(int health) {
    CharSequence text = String.format("Health: %d", health);
    healthLabel.setText(text);
  }*/

  /**
   * Updates the player's stamina on the ui.
   * @param stamina player stamina
   */
  public void updatePlayerStaminaUI (int stamina) {
    CharSequence text = String.format("Stamina: %d", stamina);
    staminaLabel.setText(text);
  }

  @Override
  public void dispose() {
    super.dispose();
    //healthLabel.remove();
    staminaLabel.remove(); //todo
  }
}
