package com.deco2800.game.entities.components.npc;

import com.deco2800.game.generic.Component;
import com.deco2800.game.rendering.components.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class GhostAnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("wander_start", this::animateWander);
    entity.getEvents().addListener("chase_start", this::animateChase);
  }

  void animateWander() {
    animator.startAnimation("float");
  }

  void animateChase() {
    animator.startAnimation("angry_float");
  }
}
