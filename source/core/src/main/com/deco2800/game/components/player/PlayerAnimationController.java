package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class PlayerAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("runLeft", this::animateRunLeft);
        entity.getEvents().addListener("runRight", this::animateRunRight);
        entity.getEvents().addListener("runUp", this::animateRunUp);
        entity.getEvents().addListener("runDown", this::animateRunDown);
        entity.getEvents().addListener("standLeft", this::animateStandLeft);
        entity.getEvents().addListener("standRight", this::animateStandRight);
        entity.getEvents().addListener("standUp", this::animateStandUp);
        entity.getEvents().addListener("standDown", this::animateStandDown);

    }

    void animateRunLeft() {
        animator.startAnimation("runLeft");
    }

    void animateRunRight(){animator.startAnimation("runRight");}

    void animateRunUp(){animator.startAnimation("runUp");}

    void animateRunDown(){animator.startAnimation("runDown");}

    void animateStandLeft(){animator.startAnimation("standLeft");}

    void animateStandRight(){animator.startAnimation("standRight");}

    void animateStandUp(){animator.startAnimation("standUp");}

    void animateStandDown(){animator.startAnimation("standDown");}

}

