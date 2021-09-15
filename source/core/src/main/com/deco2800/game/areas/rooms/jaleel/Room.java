package com.deco2800.game.areas.rooms.jaleel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.files.FileLoader;
import net.dermetfan.gdx.physics.box2d.PositionController;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Room {

    private final Vector2 roomScale;
    private final Array<DrmObject> tileDefinitions;
    private final Array<DrmObject> entityDefinitions;
    private final Array<Array<String>> tileGrid;
    private final Array<Array<String>> entityGrid;

    public Room(Vector2 roomScale, Array<DrmObject> tileDefinitions, Array<DrmObject> entityDefinitions,
                Array<Array<String>> tileGrid, Array<Array<String>> entityGrid) {
        this.roomScale = roomScale;
        this.tileDefinitions = tileDefinitions;
        this.entityDefinitions = entityDefinitions;
        this.tileGrid = tileGrid;
        this.entityGrid = entityGrid;

        System.out.println("tileGrid width is " + tileGrid.get(0).size);
        System.out.println("tileGrid height is " + tileGrid.size);
    }

    public Vector2 getRoomScale() {
        return roomScale;
    }

    public Array<DrmObject> getTileDefinitions() {
        return tileDefinitions;
    }

    public Array<DrmObject> getEntityDefinitions() {
        return entityDefinitions;
    }

    public Array<Array<String>> getTileGrid() {
        return tileGrid;
    }

    public Array<Array<String>> getEntityGrid() {
        return entityGrid;
    }

    public String[] getTileTextures() {
        return getTextures(tileDefinitions);
    }

    public String[] getEntityTextures() {
        return getTextures(entityDefinitions);
    }

    private String[] getTextures(Array<DrmObject> objectDefinitions) {
        Array<String> temp = new Array<>();
        for (int i = 0; i < objectDefinitions.size; i++) {
            if (objectDefinitions.get(i).getTexture() != null) {
                temp.add(objectDefinitions.get(i).getTexture());
            }
        }
        String[] temp1 = new String[temp.size];
        for (int i = 0; i < temp.size; i++) {
            temp1[i] = temp.get(i);
        }
        return temp1;
    }
}