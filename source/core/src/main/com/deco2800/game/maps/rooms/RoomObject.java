package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.lang.reflect.Method;

public class RoomObject implements Json.Serializable {

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
        try {
            jsonData = jsonData.child();
            Class<?> clazz = Class.forName(jsonData.asString());

            jsonData = jsonData.next();
            method = clazz.getMethod(jsonData.asString());

            jsonData = jsonData.next();
            assets = jsonData.asStringArray();

            jsonData = jsonData.next();
            assert jsonData.isNull();
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Error reading room object at value " + jsonData.name());
        }
    }
}