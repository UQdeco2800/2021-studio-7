package com.deco2800.game.entities.components.npc;


import com.deco2800.game.generic.Component;
import com.deco2800.game.rendering.components.AnimationRenderComponent;

public class InteractableComponentController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create(){
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("interaction_start", this::animateInteractionStart);
        entity.getEvents().addListener("interaction_end", this::animateInteractionEnd);
    }

    void animateInteractionStart() {
        animator.startAnimation("bed_highlight");
    }

    void animateInteractionEnd() {
        animator.startAnimation("bed");
    }
}
