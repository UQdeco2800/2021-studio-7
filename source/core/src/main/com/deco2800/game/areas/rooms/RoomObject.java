package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.HomeGameArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class RoomObject {
    private static final Logger logger = LoggerFactory.getLogger(RoomObject.class);
    private final String symbol;
    private final Method method;
    private final String texture;

    public RoomObject(String symbol, String ref)  {
        this.symbol = symbol;
        if (ref.contains("/")) {
            // Must be a file reference, parameter is a texture path
            this.texture = ref;
            this.method = null;
        } else {
            // Deterministically, must be a method name
            this.texture = "";
            this.method = getDeclaredMethod(ref, null);
        }
    }

    public RoomObject(String symbol, String methodName, String texture)  {
        this.symbol = symbol;
        this.texture = texture;
        this.method = getDeclaredMethod(methodName, texture);
    }

    public String getSymbol() {
        return symbol;
    }
    
    public Method getMethod() {
        return method;
    }
    
    public String getTexture() {
        return texture;
    }

}