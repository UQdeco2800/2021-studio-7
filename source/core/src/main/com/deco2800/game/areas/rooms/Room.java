package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Room {

    private final Vector2 roomScale;
    private final Array<DrmObject> tileDefinitions;
    private final Array<DrmObject> entityDefinitions;
    private final String[][] tileGrid;
    private final String[][] entityGrid;

    public Room(Vector2 roomScale, Array<DrmObject> tileDefinitions, Array<DrmObject> entityDefinitions,
                String[][] tileGrid, String[][] entityGrid) {
        this.roomScale = roomScale;
        this.tileDefinitions = tileDefinitions;
        this.entityDefinitions = entityDefinitions;
        this.tileGrid = tileGrid;
        this.entityGrid = entityGrid;
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

    public String[][] getTileGrid() {
        return tileGrid;
    }

    public String[][] getEntityGrid() {
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

    public Map<String, TerrainTile> getSymbolTerrainTileMap() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        Map<String, TerrainTile> stringTerrainTileMap = new Map<>() {
            private StringToTileEntry head = null;
            private int size = 0;

            @Override
            public int size() {
                return size;
            }

            @Override
            public boolean isEmpty() {
                return head == null;
            }

            @Override
            public boolean containsKey(Object key) {
                return false;
            }

            @Override
            public boolean containsValue(Object value) {
                return false;
            }

            @Override
            public TerrainTile get(Object key) {
                if (head == null) {
                    return null;
                }
                StringToTileEntry current = head;
                do {
                    if (current.getKey().equals(key)) {
                        return current.getValue();
                    }
                    current = current.getNext();
                } while (current != null);
                return null;
            }

            @Override
            public TerrainTile put(String key, TerrainTile value) {
                if (head == null) {
                    head = new StringToTileEntry(key, value);
                    size++;
                    return value;
                }
                StringToTileEntry current = head;
                StringToTileEntry previous;
                do {
                    if (current.getKey().equals(key)) {
                        TerrainTile temp = current.getValue();
                        current.setValue(value);
                        return temp;
                    }
                    previous = current;
                    current = current.getNext();
                } while (current != null);
                previous.setNext(new StringToTileEntry(key, value));
                size++;
                return null;
            }

            @Override
            public TerrainTile remove(Object key) {
                return null;
            }

            @Override
            public void putAll(Map<? extends String, ? extends TerrainTile> m) {

            }

            @Override
            public void clear() {
                head = null;
                size = 0;
            }

            @Override
            public Set<String> keySet() {
                return null;
            }

            @Override
            public Collection<TerrainTile> values() {
                return null;
            }

            @Override
            public Set<Entry<String, TerrainTile>> entrySet() {
                return null;
            }

            class StringToTileEntry {
                private String symbol;
                private TerrainTile tile;
                private StringToTileEntry next;

                StringToTileEntry(String symbol, TerrainTile tile) {
                    this.symbol = symbol;
                    this.tile = tile;
                    this.next = null;
                }

                String getKey() {
                    return symbol;
                }

                TerrainTile getValue() {
                    return tile;
                }

                void setValue(TerrainTile tile) {
                    this.tile = tile;
                }

                StringToTileEntry getNext() {
                    return next;
                }

                void setNext(StringToTileEntry next) {
                    this.next = next;
                }
            }
        };
        for (int i = 0; i < getTileDefinitions().size; i++) {
            DrmObject current = getTileDefinitions().get(i);
            stringTerrainTileMap.put(current.getSymbol(), new TerrainTile(new TextureRegion(
                    resourceService.getAsset(current.getTexture(), Texture.class))));
        }
        return stringTerrainTileMap;
    }

    public Map<String, DrmObject> getSymbolObjectMap() {
        Map<String, DrmObject> stringDrmObjectMap = new Map<String, DrmObject>() {
            private StringDrmObjectEntry head = null;
            private int size = 0;

            @Override
            public int size() {
                return size;
            }

            @Override
            public boolean isEmpty() {
                return head == null;
            }

            @Override
            public boolean containsKey(Object key) {
                return false;
            }

            @Override
            public boolean containsValue(Object value) {
                return false;
            }

            @Override
            public DrmObject get(Object key) {
                if (head == null) {
                    return null;
                }
                StringDrmObjectEntry current = head;
                do {
                    if (current.getKey().equals(key)) {
                        return current.getValue();
                    }
                    current = current.getNext();
                } while (current != null);
                return null;
            }

            @Override
            public DrmObject put(String key, DrmObject value) {
                if (head == null) {
                    head = new StringDrmObjectEntry(key, value);
                    size++;
                    return value;
                }
                StringDrmObjectEntry current = head;
                StringDrmObjectEntry previous;
                do {
                    if (current.getKey().equals(key)) {
                        DrmObject temp = current.getValue();
                        current.setValue(value);
                        return temp;
                    }
                    previous = current;
                    current = current.getNext();
                } while (current != null);
                previous.setNext(new StringDrmObjectEntry(key, value));
                size++;
                return null;
            }

            @Override
            public DrmObject remove(Object key) {
                return null;
            }

            @Override
            public void putAll(Map<? extends String, ? extends DrmObject> m) {

            }

            @Override
            public void clear() {
                head = null;
                size = 0;
            }

            @Override
            public Set<String> keySet() {
                return null;
            }

            @Override
            public Collection<DrmObject> values() {
                return null;
            }

            @Override
            public Set<Entry<String, DrmObject>> entrySet() {
                return null;
            }
            class StringDrmObjectEntry {
                private String symbol;
                private DrmObject drmObject;
                private StringDrmObjectEntry next;

                StringDrmObjectEntry(String symbol, DrmObject drmObject) {
                    this.symbol = symbol;
                    this.drmObject = drmObject;
                    this.next = null;
                }

                String getKey() {
                    return symbol;
                }

                DrmObject getValue() {
                    return drmObject;
                }

                void setValue(DrmObject drmObject) {
                    this.drmObject = drmObject;
                }

                StringDrmObjectEntry getNext() {
                    return next;
                }

                void setNext(StringDrmObjectEntry next) {
                    this.next = next;
                }
            }
        };
        for (int i = 0; i < getEntityDefinitions().size; i++) {
            DrmObject current = getEntityDefinitions().get(i);
            stringDrmObjectMap.put(current.getSymbol(), current);
        }
        return stringDrmObjectMap;
    }

    public int getMaxScale() {
        int max;
        if (roomScale.x < roomScale.y) {
            max = (int) roomScale.y;
        } else {
            max = (int) roomScale.x;
        }
        return max;
    }
}