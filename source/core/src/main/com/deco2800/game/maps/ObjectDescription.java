package com.deco2800.game.maps;

public class ObjectDescription {
    private final ObjectData data;
    private int numRotations;

    public ObjectDescription(ObjectData data, int numRotations) {
        this.data = data;
        this.numRotations = numRotations;
    }

    public ObjectData getData() {
        return data;
    }

    public int getNumRotations() {
        return numRotations;
    }

    public void setNumRotations(int numRotations) {
        this.numRotations = (this.numRotations + numRotations) % data.getAssets().length;
    }
}
