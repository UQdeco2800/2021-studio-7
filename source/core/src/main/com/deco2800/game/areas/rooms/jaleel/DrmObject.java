package com.deco2800.game.areas.rooms.jaleel;

import com.badlogic.gdx.math.GridPoint2;

import java.lang.reflect.Method;

public class DrmObject {
    private String symbol;
    private Method method;
    private String texture;

    public DrmObject(String symbol, String methodName)  {
        this.symbol = symbol;
        this.texture = null;

        try {
            Class[] parameterTypes = {GridPoint2.class};
            this.method = HouseGameArea.class.getDeclaredMethod(methodName, parameterTypes);
        } catch (Exception e) {
            this.method = null;
        }
        assert this.method != null;
    }

    public DrmObject(String symbol, String methodName, String texture)  {
        this.symbol = symbol;
        this.texture = texture;
        
        try {
            Class[] parameterTypes = {GridPoint2.class, String.class};
            this.method = HouseGameArea.class.getDeclaredMethod(methodName, parameterTypes);
        } catch (Exception e) {
            this.method = null;
        }
        assert this.method != null;
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