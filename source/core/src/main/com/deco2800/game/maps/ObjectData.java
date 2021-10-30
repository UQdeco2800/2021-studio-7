package com.deco2800.game.maps;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.Loadable;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents an object on a single point on a grid.
 * Can define either a tile or an entity.
 */
public class ObjectData implements Json.Serializable, Loadable {
    private static final Logger logger = LoggerFactory.getLogger(ObjectData.class);
    // Defined from deserialization or constructor injection
    private Method method;
    private String[] assets;
    private BodyType bodyType;
    private Vector2 renderScale;
    private Vector2 colliderScale;
    private Vector2 colliderOffset;
    private Vector2 hitboxScale;
    private Vector2 hitboxOffset;
    private Class<? extends Component>[] miscComponents;
    private ChoreList choreType;

    public ObjectData() {
    }

    public ObjectData(Method method, String[] assets) {
        this.method = method;
        this.assets = assets;
    }

    public Method getMethod() {
        if (method == null) {
            method = Home.getMethod(Home.getObjectName(this));
        }
        return method;
    }

    public String[] getAssets() {
        return assets;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public Vector2 getRenderScale() {
        return renderScale;
    }

    public Vector2 getColliderScale() {
        return colliderScale;
    }

    public Vector2 getColliderOffset() {
        return colliderOffset;
    }

    public Vector2 getHitboxScale() {
        return hitboxScale;
    }

    public Vector2 getHitboxOffset() {
        return hitboxOffset;
    }

    public Class<? extends Component>[] getMiscComponents() {
        return miscComponents;
    }

    public ChoreList getChoreType() {
        return choreType;
    }

    public void setNullMethod() {
        method = null;
    }

    @Override
    public void loadAssets() {
        for (String filepath : assets) {
            Class<?> type = ResourceService.getDefaultClass(filepath);
            if (type != null) {
                ServiceLocator.getResourceService().loadAsset(filepath, type);
            }
        }
    }

    @Override
    public void unloadAssets() {
        for (String filepath : assets) {
            ServiceLocator.getResourceService().unloadAsset(filepath);
        }
    }

    @Override
    public void write(Json json) {
        // No purpose yet
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(Json json, JsonValue jsonData) {
        JsonValue iterator = jsonData.child();
        try {
            if (iterator != null && iterator.name().equals("method")) {
                String classMethodName = iterator.asString();
                int delim = classMethodName.lastIndexOf('.');
                String className = classMethodName.substring(0, delim);
                String methodName = classMethodName.substring(delim + 1);
                method = Class.forName(className).getMethod(methodName, ObjectData.class, int.class, GridPoint2.class);
                iterator = iterator.next();
            } else {
                method = null;
            }

            if (iterator != null && iterator.name().equals("assets")) {
                assets = iterator.asStringArray();
                iterator = iterator.next();
            } else {
                assets = new String[]{""};
            }

            if (iterator != null && iterator.name().equals("scale")) {
                renderScale = Vector2Utils.read(iterator);
                iterator = iterator.next();
            } else {
                renderScale = new Vector2(1f, 1f);
            }

            if (iterator != null && iterator.name().equals("physics")) {
                switch (iterator.asString()) {
                    case "DYNAMIC":
                        bodyType = BodyType.DynamicBody;
                        break;
                    case "KINEMATIC":
                        bodyType = BodyType.KinematicBody;
                        break;
                    default:
                        bodyType = BodyType.StaticBody;
                }
                iterator = iterator.next();
            } else {
                bodyType = BodyType.StaticBody;
            }

            if (iterator != null && iterator.name().equals("collider")) {
                float[] colliderArgs = iterator.asFloatArray();
                colliderScale = new Vector2(colliderArgs[0], colliderArgs[1]);
                colliderOffset = new Vector2(colliderArgs[2], colliderArgs[3]);
                iterator = iterator.next();
            } else {
                colliderScale = new Vector2(1f, 1f);
                colliderOffset = new Vector2(0f, 0f);
            }

            if (iterator != null && iterator.name().equals("hitbox")) {
                float[] hitboxArgs = iterator.asFloatArray();
                hitboxScale = new Vector2(hitboxArgs[0], hitboxArgs[1]);
                hitboxOffset = new Vector2(hitboxArgs[2], hitboxArgs[3]);
                iterator = iterator.next();
            } else {
                hitboxScale = new Vector2(1f, 1f);
                hitboxOffset = new Vector2(0f, 0f);
            }

            if (iterator != null && iterator.name().equals("misc")) {
                String[] classNames = iterator.asStringArray();
                miscComponents = new Class[classNames.length];
                for (int i = 0; i < classNames.length; i++) {
                    miscComponents[i] = (Class<? extends Component>) Class.forName(classNames[i]);
                }
                iterator = iterator.next();
            } else {
                miscComponents = new Class[0];
            }

            if (iterator != null && iterator.name().equals("chore")) {
                choreType = ChoreList.valueOf(iterator.asString());
                iterator = iterator.next();
            } else {
                choreType = null;
            }

            FileLoader.assertJsonValueNull(iterator);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectData that = (ObjectData) o;
        return Objects.equals(method, that.method) &&
            Arrays.equals(assets, that.assets) &&
            Objects.equals(renderScale, that.renderScale) &&
            Objects.equals(colliderScale, that.colliderScale) &&
            Objects.equals(colliderOffset, that.colliderOffset) &&
            Objects.equals(hitboxScale, that.hitboxScale) &&
            Objects.equals(hitboxOffset, that.hitboxOffset) &&
            Arrays.equals(miscComponents, that.miscComponents) &&
            bodyType == that.bodyType &&
            choreType == that.choreType;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(method, renderScale, colliderScale, colliderOffset,
            hitboxScale, hitboxOffset, bodyType, choreType);
        result = 31 * result + Arrays.hashCode(assets);
        result = 31 * result + Arrays.hashCode(miscComponents);
        return result;
    }
}