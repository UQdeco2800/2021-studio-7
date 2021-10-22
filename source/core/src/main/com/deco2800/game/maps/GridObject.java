package com.deco2800.game.maps;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Represents an object on a single point on a grid.
 * Can define either a tile or an entity.
 */
public class GridObject implements Json.Serializable {
    private static final Logger logger = LoggerFactory.getLogger(GridObject.class);
    // Defined from deserialization or constructor injection
    private Method method;
    private String[] assets;

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

    public List<String> getAssets(String extension) {
        List<String> assetsWithExtension = new ArrayList<>();
        for (String asset : assets) {
            if (asset.endsWith(extension)) {
                assetsWithExtension.add(asset);
            }
        }
        return assetsWithExtension;
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
            FileLoader.assertJsonValueName(iterator, "class");
            Class<?> clazz = Class.forName(iterator.asString());

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "method");
            method = clazz.getMethod(iterator.asString(), String[].class);

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "assets");
            assets = iterator.asStringArray();

            FileLoader.assertJsonValueNull(iterator.next());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}