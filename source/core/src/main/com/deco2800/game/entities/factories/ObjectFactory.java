package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.object.DoorActions;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.Home;
import com.deco2800.game.maps.ObjectData;
import com.deco2800.game.maps.ObjectDescription;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.components.AnimationRenderComponent;
import com.deco2800.game.rendering.components.TextureRenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
@SuppressWarnings({"unused"})
public class ObjectFactory {
    private static final Logger logger = LoggerFactory.getLogger(ObjectFactory.class);

    public static Entity createBed(ObjectDescription desc, GridPoint2 worldPos) {
        ServiceLocator.getHome().getFloor().stashBedPosition(worldPos);
        return null;
    }

    public static Entity createDoor(ObjectDescription desc, GridPoint2 worldPos) {
        Entity door = createInteractive(desc, worldPos);
        String doorName = Home.getObjectName(desc.getData());
        if (doorName != null && doorName.endsWith("v")) {
            door.getComponent(DoorActions.class).setAnimations(
                    new String[]{"door_close_left_re", "left_highlight", "door_close_left_re"}
            );
        }
        else if (doorName != null && doorName.endsWith("h")) {
            door.getComponent(DoorActions.class).setAnimations(
                    new String[]{"door_close_right_re", "right_highlight", "door_close_right_re"}
            );
        }

        return door;
    }

    public static Entity createChore(ObjectDescription desc, GridPoint2 worldPos) {
        ObjectData data = desc.getData();
        Entity chore = createInteractive(desc, worldPos);
        ChoreList type = data.getChoreType();
        if (type != null) {
            ServiceLocator.getChoreController().addChore(chore, data.getChoreType());
        } else {
            logger.error("Couldn't add entity as chore because type is null");
        }
        return chore;
    }

    public static Entity createInteractive(ObjectDescription desc, GridPoint2 worldPos) {
        ObjectData data = desc.getData();
        // Set interactable to have a base hitbox component
        Entity interactive = createObject(desc, worldPos)
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE));
        PhysicsUtils.setHitboxShape(interactive, data.getHitboxScale().x, data.getHitboxScale().y);
        PhysicsUtils.setHitboxOffset(interactive, data.getHitboxOffset().x, data.getHitboxOffset().y);

        try {
            for (Class<? extends Component> component : data.getMiscComponents()) {
                interactive.addComponent(component.getDeclaredConstructor().newInstance());
            }
        } catch (Exception e) {
            logger.error("Couldn't add component from {}", Arrays.toString(data.getMiscComponents()));
        }

        return interactive;
    }

    public static Entity createObject(ObjectDescription desc, GridPoint2 worldPos) {
        ObjectData data = desc.getData();
        Entity object = new Entity();
        object.setScale(data.getRenderScale());

        // Set obstacle to have a base render component
        ResourceService resourceService = ServiceLocator.getResourceService();
        String asset = data.getAssets()[desc.getNumRotations() % data.getAssets().length];
        if (asset.endsWith(".png")) {
            // Asset is a texture, add a TextureRenderComponent
            Texture texture = resourceService.getAsset(asset, Texture.class);
            object.addComponent(new TextureRenderComponent(texture));
        } else if (asset.endsWith(".atlas")) {
            // Asset is an atlas, add an AnimationRenderComponent
            TextureAtlas atlas = resourceService.getAsset(asset, TextureAtlas.class);
            AnimationRenderComponent animator = new AnimationRenderComponent(atlas);
            object.addComponent(animator);
            // Add all atlas regions as animations to the component
            for (TextureAtlas.AtlasRegion region : new Array.ArrayIterator<>(atlas.getRegions())) {
                if (!animator.hasAnimation(region.name)) {
                    if (region.name.equals("TV_on1") || region.name.equals("TV_onh1")
                        || region.name.equals("dust1")) {
                        animator.addAnimation(region.name, 0.1f, Animation.PlayMode.LOOP);
                    } else {
                        animator.addAnimation(region.name, 1f);
                    }
                }
            }
        }

        // Set obstacle to have base physics components
        object.addComponent(new PhysicsComponent().setBodyType(data.getBodyType()))
            .addComponent(new ColliderComponent());
        PhysicsUtils.setColliderShape(object, data.getColliderScale().x, data.getColliderScale().y);
        PhysicsUtils.setColliderOffset(object, data.getColliderOffset().x, data.getColliderOffset().y);

        return object;
    }

    private ObjectFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}

