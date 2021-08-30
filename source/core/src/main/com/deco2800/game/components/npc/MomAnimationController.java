package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class MomAnimationController extends Component{
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("walkLeft", this::animateRunLeft);
        entity.getEvents().addListener("walkRight", this::animateRunRight);
        entity.getEvents().addListener("walkUp", this::animateRunUp);
        entity.getEvents().addListener("walkDown", this::animateRunDown);
        entity.getEvents().addListener("standLeft", this::animateStandLeft);
        entity.getEvents().addListener("standRight", this::animateStandRight);
        entity.getEvents().addListener("standUp", this::animateStandUp);
        entity.getEvents().addListener("standDown", this::animateStandDown);

        entity.getEvents().trigger("standUp");
    }

    void animateRunLeft() {
        animator.startAnimation("walkLeft");
    }

    void animateRunRight(){animator.startAnimation("walkRight");}

    void animateRunUp(){animator.startAnimation("walkUp");}

    void animateRunDown(){animator.startAnimation("walkDown");}

    void animateStandLeft(){animator.startAnimation("standLeft");}

    void animateStandRight(){animator.startAnimation("standRight");}

    void animateStandUp(){animator.startAnimation("standUp");}

    void animateStandDown(){animator.startAnimation("standDown");}



}
