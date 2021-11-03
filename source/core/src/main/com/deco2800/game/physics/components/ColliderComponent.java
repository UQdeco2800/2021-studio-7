package com.deco2800.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ComponentPriority;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsComponent.AlignX;
import com.deco2800.game.physics.components.PhysicsComponent.AlignY;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Attaches a physics collider to an entity. By default, this is a rectangle the same size as the
 * entity's scale. This allows an entity to collide with other physics objects, or detect collisions
 * without interaction (if sensor = true)
 */
public class ColliderComponent extends Component {
    // Constants
    public static final int LEFT_SIDE = 0;
    public static final int RIGHT_SIDE = 1;
    public static final int X_OFFSET = 0;
    public static final int Y_OFFSET = 1;
    public static final int ALIGN_SCALE = 2;

    private static final float X_SCALE = 1f;
    private static final float Y_SCALE = 0.5f;
    private static final int NO_OFFSET = 0;

    // Privates
    private static final Logger logger = LoggerFactory.getLogger(ColliderComponent.class);

    private final FixtureDef fixtureDef;
    private Fixture fixture;

    private float left;
    private float right;
    private float offX;
    private float offY;
    private float posX;
    private float posY;

    public ColliderComponent() {
        createPriority = ComponentPriority.COLLIDER.ordinal();
        fixtureDef = new FixtureDef();
    }

    @Override
    public void create() {
        if (fixtureDef.shape == null) {
            logger.trace("{} Setting default bounding box", this);
            fixtureDef.shape = makeBoundingBox();
        }

        Body physBody = entity.getComponent(PhysicsComponent.class).getBody();
        fixture = physBody.createFixture(fixtureDef);
    }

    /**
     * @return Size of the collider component, in tile units, broken down by side length.
     *          The side length is a scalar multiple of the stored value.
     */
    public float[] getSides() {
        return new float[]{this.left, this.right};
    }

    /**
     * @return Offset position of the collider component.
     */
    public float[] getOffset() {
        return new float[]{this.offX, this.offY};
    }

    /**
     * @return Offset position of the collider component.
     */
    public float[] getPosition() {
        return new float[]{this.posX, this.posY};
    }

    /**
     * Calculate the x and y position of the collider alignment.
     *
     * @param left Number of side units on bottom left.
     * @param right Number of side units on bottom right.
     * @param alignX The X coordinate alignment value.
     * @param alignY The Y coordinate alignment value.
     *
     * @return The position of the alignment.
     */
    private Vector2 getAlignmentPosition(
            float left, float right,
            AlignX alignX, AlignY alignY
    ) {
        // Divide values by the alignment scaling factor for middle aligning
        left /= ALIGN_SCALE;
        right /= ALIGN_SCALE;

        float mid = Math.abs(left / 2 - right / 2);

        Vector2 position = new Vector2();
        switch (alignX) {
            case LEFT:
                position.x = entity.getScale().x;
                break;
            case CENTER:
                position.x = entity.getCenterPosition().x;
                break;
            case RIGHT:
                position.x = entity.getScale().x - right;
                break;
        }

        switch (alignY) {
            case BOTTOM:
                position.y = 0;
                break;
            case CENTER:
                position.y = entity.getCenterPosition().y;
                break;
            case TOP:
                position.y = entity.getScale().y - mid;
                break;
        }

        return position;
    }

    /**
     * Create an iso shape for the collider component.
     *
     * @param left Number of side units on bottom left.
     * @param right Number of side units on bottom right.
     * @param alignX The X coordinate alignment value.
     * @param alignY The Y coordinate alignment value.
     * @param offX X position offset
     * @param offY Y position offset
     *
     * @return The created collider component
     */
    private ColliderComponent createIsoShape(
            float left, float right,
            AlignX alignX, AlignY alignY,
            float offX, float offY
    ) {
        Vector2 position = getAlignmentPosition(left, right, alignX, alignY);
        position.x += offX;
        position.y += offY;

        this.left = left;
        this.right = right;
        this.offX = offX;
        this.offY = offY;
        this.posX = position.x;
        this.posY = position.y;

        return changeIsoShape(left, right, position);
    }

    /**
     * Changes the shape of the component to be an isometric, 4-sided polygon.
     *
     * @param left length of the shape on the bottom left (and top right) sides, in tile units.
     * @param right length of the shape on the bottom right (and top left) sides, in tile units.
     * @param position offset for the shape.
     * @return self
     */
    private ColliderComponent changeIsoShape (
            float left, float right,
            Vector2 position)
    {
        left /= ALIGN_SCALE;
        right /= ALIGN_SCALE;

        PolygonShape bound = new PolygonShape();

        // Each point is offset by the given alignment
        Vector2 south = isoVector2(position.x, position.y);
        Vector2 east = isoVector2(position.x + right, position.y + right);
        Vector2 north = isoVector2(position.x + right - left, position.y + right + left);
        Vector2 west = isoVector2(position.x - left, position.y + left);

        // Collect each of the isometric parallelogram's corners
        Vector2[] points = new Vector2[]{west, north, east, south};

        bound.set(points);
        setShape(bound);
        return this;
    }

    /**
     * Helper method to convert points into a vector on an isometric scale. The
     * conversion works by scaling the x and y values by the corresponding iso
     * scalar value.
     *
     * @param x length to scale in the x-direction, in tile units.
     * @param y length to scale in the y-direction, in tile units.
     * @return Vector2 representing the resultant scaled points.
     */
    private Vector2 isoVector2(float x, float y) {
        return new Vector2(x * X_SCALE, y * Y_SCALE);
    }

    // set with left right
    // set with left right offx offy
    // set with all
    public ColliderComponent setIsoShape(float left, float right) {
        return createIsoShape(left, right,
                AlignX.CENTER, AlignY.BOTTOM,
                NO_OFFSET, NO_OFFSET
        );
    }

    public ColliderComponent setIsoShape(float left, float right,
                                         float offX, float offY)
    {
        return createIsoShape(left, right,
                AlignX.CENTER, AlignY.BOTTOM,
                offX, offY
        );
    }

    public ColliderComponent setIsoShape(float left, float right,
                                         AlignX alignX, AlignY alignY,
                                         float offX, float offY)
    {
        return createIsoShape(left, right,
                alignX, alignY,
                offX, offY
        );
    }


    /**
     * Set friction. This affects the object when touching other objects, but does not affect friction
     * with the ground.
     *
     * @param friction friction, default = 0
     * @return self
     */
    public ColliderComponent setFriction(float friction) {
        if (fixture == null) {
            fixtureDef.friction = friction;
        } else {
            fixture.setFriction(friction);
        }
        return this;
    }

    /**
     * Set whether this physics component is a sensor. Sensors don't collide with other objects but
     * still trigger collision events. See: https://www.iforce2d.net/b2dtut/sensors
     *
     * @param isSensor true if sensor, false if not. default = false.
     * @return self
     */
    public ColliderComponent setSensor(boolean isSensor) {
        if (fixture == null) {
            fixtureDef.isSensor = isSensor;
        } else {
            fixture.setSensor(isSensor);
        }
        return this;
    }

    /**
     * Set density
     *
     * @param density Density and size of the physics component determine the object's mass. default =
     *     0
     * @return self
     */
    public ColliderComponent setDensity(float density) {
        if (fixture == null) {
            fixtureDef.density = density;
        } else {
            fixture.setDensity(density);
        }
        return this;
    }

    /**
     * Set restitution
     *
     * @param restitution restitution is the 'bounciness' of an object, default = 0
     * @return self
     */
    public ColliderComponent setRestitution(float restitution) {
        if (fixture == null) {
            fixtureDef.restitution = restitution;
        } else {
            fixture.setRestitution(restitution);
        }
        return this;
    }

    /**
     * Set shape
     *
     * @param shape shape, default = bounding box the same size as the entity
     * @return self
     */
    public ColliderComponent setShape(Shape shape) {
        if (fixture == null) {
            fixtureDef.shape = shape;
        } else {
            logger.error("{} Cannot set Collider shape after create(), ignoring.", this);
        }
        return this;
    }

    /** @return Physics fixture of this collider. Null before created() */
    public Fixture getFixture() {
        return fixture;
    }

    /**
     * Set the collider layer, used in collision logic
     * @param layerMask Bitmask of {@link PhysicsLayer} this collider belongs to
     * @return self
     */
    public ColliderComponent setLayer(short layerMask) {
        if (fixture == null) {
            fixtureDef.filter.categoryBits = layerMask;
        } else {
            Filter filter = fixture.getFilterData();
            filter.categoryBits = layerMask;
            fixture.setFilterData(filter);
        }
        return this;
    }

    /**
     * @return The {@link PhysicsLayer} this collider belongs to
     */
    public short getLayer() {
        if (fixture == null) {
            return fixtureDef.filter.categoryBits;
        }
        return fixture.getFilterData().categoryBits;
    }

    private Shape makeBoundingBox() {
        PolygonShape bbox = new PolygonShape();
        Vector2 center = entity.getScale().scl(0.5f);

		/*
		   height would normally be tan(30 deg) * size.y for iso, but we estimate
		   it with a 1:2 ratio. Therefore, height is half of the y size.
		   */
        float height = 0.5f * center.y;

        Vector2 west = new Vector2(0 - (center.x / 2), (height / 2));
        Vector2 north = new Vector2(0, height);
        Vector2 east = new Vector2((center.x / 2), (height / 2));
        Vector2 south = new Vector2(0, 0);
        // Collect each of the isometric parallelogram's corners
        Vector2[] points = new Vector2[]{west, north, east, south};

        bbox.set(points);
        setShape(bbox);
        return bbox;
    }
}
