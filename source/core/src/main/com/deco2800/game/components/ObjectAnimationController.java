package com.deco2800.game.components;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * The animation controller to be used by interactable objects
 */
public class ObjectAnimationController extends Component {
    AnimationRenderComponent animator;
    private final String inactiveAnimation;
    private final String contactAnimation;


    /**
     * Controls the animations of the entity depending on whether it is in contact with the player
     * or not. Requires the entity to have the InteractableComponent added.
     *
     * @param inactiveAnimation The animation to play WHEN NOT in contact with the player
     * @param contactAnimation The animation to play WHEN in contact with the player
     */
    public ObjectAnimationController(String inactiveAnimation, String contactAnimation) {
        this.inactiveAnimation = inactiveAnimation;
        this.contactAnimation = contactAnimation;
    }

    @Override
    public void create(){
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("interactionStart", this::animateInteractionStart);
        entity.getEvents().addListener("interactionEnd", this::animateInteractionEnd);
    }

    void animateInteractionStart() {
        animator.startAnimation(contactAnimation);
    }

    void animateInteractionEnd() {
        animator.startAnimation(inactiveAnimation);
    }
}