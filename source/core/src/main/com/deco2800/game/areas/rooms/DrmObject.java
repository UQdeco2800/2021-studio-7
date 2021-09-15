package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.GridPoint2;

import java.lang.reflect.Method;

public class DrmObject {
    private String symbol;
    private Method method;
    private String texture;

    public DrmObject(String symbol, String parameter)  {
        this.symbol = symbol;
        if (parameter.contains("/")) {
            // Must be a file reference -> parameter is a texture path
            this.texture = parameter;
            this.method = null;
        } else {
            // Not a file reference -> must be a method name
            this.texture = "";
            try {
                Class[] parameterTypes = {GridPoint2.class};
                this.method = (HouseGameArea.class).getDeclaredMethod(parameter, parameterTypes);
            } catch (Exception e) {
                this.method = null;
                System.out.println("Null method on symbol ".concat(symbol));
            }
        }
    }

    public DrmObject(String symbol, String methodName, String texture)  {
        this.symbol = symbol;
        this.texture = texture;

        try {
            Class[] parameterTypes = {GridPoint2.class, String.class};
            this.method = (HouseGameArea.class).getDeclaredMethod(methodName, parameterTypes);
        } catch (Exception e) {
            this.method = null;
            System.out.println("Null method on symbol ".concat(symbol));
        }
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