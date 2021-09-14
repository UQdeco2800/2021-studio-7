package com.deco2800.game.areas.rooms.jaleel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.files.FileLoader;
import net.dermetfan.gdx.physics.box2d.PositionController;

public class Room {

    private final Vector2 roomScale;
    private final Array<DrmObject> tileDefinitions;
    private final Array<DrmObject> entityDefinitions;
    private final Array<Array<String>> tileGrid;
    private final Array<Array<String>> entityGrid;
    private final String[] tileTextures;
    private final String[] entityTextures;

    public Room(Vector2 roomScale, Array<DrmObject> tileDefinitions, Array<DrmObject> entityDefinitions,
                Array<Array<String>> tileGrid, Array<Array<String>> entityGrid) {
        this.roomScale = roomScale;
        this.tileDefinitions = tileDefinitions;
        this.entityDefinitions = entityDefinitions;
        this.tileGrid = tileGrid;
        this.entityGrid = entityGrid;

        Array<String> temp = new Array<>();
        for (int i = 0; i < tileDefinitions.size; i++) {
            if (tileDefinitions.get(i).getTexture() != null) {
                temp.add(tileDefinitions.get(i).getTexture());
            }
        }
        this.tileTextures = temp.toArray();

        temp = new Array<>();
        for (int i = 0; i < entityDefinitions.size; i++) {
            if (entityDefinitions.get(i).getTexture() != null) {
                temp.add(entityDefinitions.get(i).getTexture());
            }
        }
        this.entityTextures = temp.toArray();
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
        return tileTextures;
    }

    public String[] getEntityTextures() {
        return entityTextures;
    }
}
