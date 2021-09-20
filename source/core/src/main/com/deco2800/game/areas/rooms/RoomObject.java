package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.HouseGameArea;
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

    /**
     * Attempts to find the method name in the codebase. If found, a method
     * will be invoked later for object generation.
     * @param methodName method to be found
     * @param textureName optional texture to be loaded (for walls)
     * @return found method signature
     */
    private Method getDeclaredMethod(String methodName, String textureName) {
        Method method = null;
        Class[] paramTypes;
        if (textureName == null) {
            paramTypes = new Class[]{GridPoint2.class};
        } else {
            paramTypes = new Class[]{GridPoint2.class, String.class};
        }

        try {
            method = (HouseGameArea.class).getDeclaredMethod(methodName, paramTypes);
        } catch (Exception e) {
            logger.error("No method retrieved for symbol {}", symbol);
        }

        return method;
    }
}