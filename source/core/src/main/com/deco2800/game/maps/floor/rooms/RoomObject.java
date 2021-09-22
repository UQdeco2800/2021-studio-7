package com.deco2800.game.maps.floor.rooms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class RoomObject {
    private static final Logger logger = LoggerFactory.getLogger(RoomObject.class);

    private String className;
    private String methodName;
    private String[] assets;

    public Method getMethod() {
        return findMethod(methodName);
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
     * @return found method signature
     */
    private Method findMethod(String methodName) {
        Method method = null;
        try {
            method = findClass(className).getMethod(methodName);
        } catch (NoSuchMethodException e) {
            logger.error("Method {} could not be found", methodName);
        }
        return method;
    }
}