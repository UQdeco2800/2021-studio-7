package com.deco2800.game.entities.components.player;

import com.deco2800.game.generic.Component;
import com.deco2800.game.rendering.components.AnimationRenderComponent;

/**
 * This class listens to events relevant to a players entity's state and plays the animation when one
 *  of the events is triggered.
 */
public class PlayerAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("standing_north", this::animateStandingNorth);
        entity.getEvents().addListener("standing_east", this::animateStandingEast);
        entity.getEvents().addListener("standing_south", this::animateStandingSouth);
        entity.getEvents().addListener("standing_west", this::animateStandingWest);
        entity.getEvents().addListener("walking_north", this::animateWalkingNorth);
        entity.getEvents().addListener("walking_east", this::animateWalkingEast);
        entity.getEvents().addListener("walking_south", this::animateWalkingSouth);
        entity.getEvents().addListener("walking_west", this::animateWalkingWest);

        entity.getEvents().trigger("standing_north");
    }

    void animateStandingNorth() {
        animator.startAnimation("standing_north");
    }

    void animateStandingEast() {
        animator.startAnimation("standing_east");
    }

    void animateStandingSouth() {
        animator.startAnimation("standing_south");
    }

    void animateStandingWest() {
        animator.startAnimation("standing_west");
    }

    void animateWalkingNorth() {
        animator.startAnimation("walking_north");
    }

    void animateWalkingEast() {
        animator.startAnimation("walking_east");
    }

    void animateWalkingSouth() {
        animator.startAnimation("walking_south");
    }

    void animateWalkingWest() {
        animator.startAnimation("walking_west");
    }
}

