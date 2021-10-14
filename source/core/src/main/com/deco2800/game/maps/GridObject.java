package com.deco2800.game.maps;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Represents an object on a single point on a grid.
 * Can define either a tile or an entity.
 */
public class GridObject implements Json.Serializable {

    private static final Logger logger = LoggerFactory.getLogger(GridObject.class);
    // Defined on deserialization
    private Method method;
    private String[] assets;

    public Method getMethod() {
        return method;
    }

    public String[] getAssets() {
        return assets;
    }

    public Array<String> getAssets(String extension) {
        Array<String> assetsWithExtension = new Array<>();
        for (String asset : assets) {
            if (asset.endsWith(extension)) {
                assetsWithExtension.add(asset);
            }
        }
        return assetsWithExtension;
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