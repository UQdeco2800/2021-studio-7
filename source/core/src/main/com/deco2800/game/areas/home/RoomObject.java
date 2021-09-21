package com.deco2800.game.areas.home;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.HomeGameArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class RoomObject {
    private static final Logger logger = LoggerFactory.getLogger(RoomObject.class);

    private final Method method;
    private final String[] assets;

    public RoomObject(String methodName, String[] assetNames) {
        this.method = getDeclaredMethod(methodName, assetNames);
        this.assets = assetNames;
    }

    public Method getMethod() {
        return method;
    }

    public String[] getAssets() {
        return assets;
    }

    /**
     * Attempts to find the method name in the codebase. If found, a method
     * will be invoked later for object generation.
     *
     * @param methodName method to be found
     * @param assetName  optional asset to be loaded
     * @return found method signature
     */
    private Method getDeclaredMethod(String methodName, String[] assetNames) {
        Method method = null;
        Class[] paramTypes;
        if (assetNames == null) {
            paramTypes = new Class[]{GridPoint2.class};
        } else {
            paramTypes = new Class[]{GridPoint2.class, String[].class};
        }

        try {
            method = (HomeGameArea.class).getDeclaredMethod(methodName, paramTypes);
        } catch (Exception e) {
            logger.error("Method {} could not be found", methodName);
        }

        return method;
    }
}