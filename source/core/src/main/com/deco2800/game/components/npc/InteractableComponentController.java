package com.deco2800.game.components.npc;


import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class InteractableComponentController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create(){
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("interactionStart", this::animateInteractionStart);
        entity.getEvents().addListener("interactionEnd", this::animateInteractionEnd);
    }

    void animateInteractionStart() {
        animator.startAnimation("bed");
    }

    void animateInteractionEnd() {
        animator.startAnimation("bed_highlight");
    }
}
