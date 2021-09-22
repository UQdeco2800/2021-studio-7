package com.deco2800.game.maps.floor.rooms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class RoomObject {
    private static final Logger logger = LoggerFactory.getLogger(RoomObject.class);

    private final Class<?> clazz;
    private final Method method;
    private final String[] assets;

    public RoomObject(String className, String methodName, String[] assetNames) {
        this.clazz = findClass(className);
        this.method = findMethod(methodName);
        this.assets = assetNames;
    }

    public Method getMethod() {
        return method;
    }

    public String[] getAssets() {
        return assets;
    }

    public Class<?> findClass(String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error("Class {} could not be found", className);
        }
        return clazz;
    }

    /**
     * Attempts to find the method name in the codebase. If found, a method
     * will be invoked later for object generation.
     *
     * @param methodName method to be found
     * @param assetName  optional asset to be loaded
     * @return found method signature
     */
    private Method findMethod(String methodName) {
        Method method = null;
        try {
            method = clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            logger.error("Method {} could not be found", methodName);
        }
        return method;
    }
}