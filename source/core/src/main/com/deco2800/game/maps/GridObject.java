package com.deco2800.game.maps;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.Loadable;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents an object on a single point on a grid.
 * Can define either a tile or an entity.
 */
public class GridObject implements Json.Serializable, Loadable {
    private static final Logger logger = LoggerFactory.getLogger(GridObject.class);
    // Defined from deserialization or constructor injection
    private Method method;
    private String[] assets;
    private Vector2 renderScale;
    private Vector2 colliderScale;
    private Vector2 colliderOffset;
    private Vector2 hitboxScale;
    private Vector2 hitboxOffset;
    private Class<?>[] uniqueComponents;

    public GridObject() {
    }

    public GridObject(Method method, String[] assets) {
        this.method = method;
        this.assets = assets;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public void setAssets(String[] assets) {
        this.assets = assets;
    }

    public String[] getAssets() {
        return assets;
    }

    public List<Integer> getAssetIndexes() {
        List<Integer> assetIndexes = new ArrayList<>();
        for (int i = 0; i < assets.length; i++) {
            if (assets[i].endsWith(".png") || assets[i].endsWith(".atlas")) {
                assetIndexes.add(i);
            }
        }
        return assetIndexes;
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
        json.writeObjectStart();
        json.writeValue("class", (Object) method.getDeclaringClass());
        json.writeValue("method", method.getName());
        json.writeValue("assets", assets);
        json.writeObjectEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        JsonValue iterator = jsonData.child();
        try {
            FileLoader.assertJsonValueName(iterator, "method");
            int delim = iterator.asString().lastIndexOf('.');
            String className = iterator.asString().substring(delim);
            String methodName = iterator.asString().substring(0, delim);
            method = Class.forName(className).getMethod(methodName, GridObject.class, GridPoint2.class);

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "assets");
            assets = iterator.asStringArray();

            iterator = iterator.next();
            if (iterator != null) {
                FileLoader.assertJsonValueName(iterator, "scale");
                renderScale = Vector2Utils.read(iterator);
            } else {
                return;
            }

            iterator = iterator.next();
            if (iterator != null) {
                FileLoader.assertJsonValueName(iterator, "collider");
                float[] colliderArgs = iterator.asFloatArray();
                colliderScale = new Vector2(colliderArgs[0], colliderArgs[1]);
                colliderOffset = new Vector2(colliderArgs[2], colliderArgs[3]);
            } else {
                return;
            }

            iterator = iterator.next();
            if (iterator != null) {
                FileLoader.assertJsonValueName(iterator, "hitbox");
                float[] hitboxArgs = iterator.asFloatArray();
                hitboxScale = new Vector2(hitboxArgs[0], hitboxArgs[1]);
                hitboxOffset = new Vector2(hitboxArgs[2], hitboxArgs[3]);
            } else {
                return;
            }

            FileLoader.assertJsonValueNull(iterator.next());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GridObject that = (GridObject) o;
        return Objects.equals(method, that.method) && Arrays.equals(assets, that.assets);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(method);
        result = 31 * result + Arrays.hashCode(assets);
        return result;
    }
}