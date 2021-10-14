package com.deco2800.game.entities.components;

import static org.mockito.Mockito.mock;

class SingleUseTest {
/*
    private static final String[] forestTextureAtlases = {
            "images/objects/energy_drink/energy.atlas"
    };

    @BeforeEach
    void beforeEach() {
        // Set up services and time
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(0.02f);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerResourceService(new ResourceService());
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextureAtlases(forestTextureAtlases);
    }

    @Test
    void remove() {
        Entity player = new Entity().addComponent(new PlayerActions());
        Entity energyDrink = new Entity();

        AnimationRenderComponent drinkAnimations = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/objects/energy_drink/energy.atlas",
                        TextureAtlas.class));

        energyDrink.addComponent(drinkAnimations)
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.DynamicBody))
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new DrinkActions())
                .addComponent(new SingleUse());



        energyDrink.getComponent(DrinkActions.class).onInteraction(player);
        assertNull(energyDrink.getComponent(PhysicsComponent.class));
        assertNull(energyDrink.getComponent(AnimationRenderComponent.class));
        assertNull(energyDrink.getComponent(HitboxComponent.class));
        assertNull(energyDrink.getComponent(ColliderComponent.class));

    }*/
}