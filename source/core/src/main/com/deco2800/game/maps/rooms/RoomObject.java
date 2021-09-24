package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.maps.floors.FloorPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class RoomObject implements Json.Serializable {

    private static final Logger logger = LoggerFactory.getLogger(RoomObject.class);
    private Method method;
    private String[] assets;

    public Method getMethod() {
        return method;
    }

    public String[] getAssets() {
        return assets;
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
            assert iterator.name().equals("class");
            Class<?> clazz = Class.forName(iterator.asString());

            iterator = iterator.next();
            assert iterator.name().equals("method");
            method = clazz.getMethod(iterator.asString(), String[].class);

            iterator = iterator.next();
            assert iterator.name().equals("assets");
            assets = iterator.asStringArray();

            assert iterator.next().isNull();
        } catch (Exception e) {
            logger.error("Error reading room object at {}: {}", iterator.name(), iterator.asString());
        }
    }
}