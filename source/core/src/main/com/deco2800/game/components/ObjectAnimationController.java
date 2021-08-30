package com.deco2800.game.components;

import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * Listens to events relevant to an object's entity state, and plays
 * an animation when one of the events is triggered.
 */
public class ObjectAnimationController extends Component {
    private String baseImage;
    private String highlightedImage;
    AnimationRenderComponent animator;

    /**
     * Listens to events relevant to an objects entity state, and plays an animation when one of
     * the events is triggered. Currently just for highlighting an object when in contact.
     * @param baseImage The initial image/ animation to display
     * @param highlightedImage The image/ animation to display when in contact with the player
     */
    public ObjectAnimationController(String baseImage, String highlightedImage) {
        this.baseImage = baseImage;
        this.highlightedImage = highlightedImage;
    }

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("interactionStart", this::interactionStart);
        entity.getEvents().addListener("interactionEnd", this::interactionEnd);
    }

    void interactionStart() {
        animator.startAnimation(highlightedImage);
    }

    void interactionEnd() {
        animator.startAnimation(baseImage);
    }
}
