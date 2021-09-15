package com.deco2800.game.areas.rooms.jaleel;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.components.GameAreaDisplay;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class HouseGameArea extends GameArea {

    private static final Logger logger = LoggerFactory.getLogger(HouseGameArea.class);
    private final TerrainFactory terrainFactory;
    private EventHandler eventHandler;
    public Entity player;

    private final String[] drmLocations = {"maps/s2/r1_jaleel.drm"};
    private final String[] houseTextureAtlases = {
            "images/characters/boy_01/boy_01.atlas",
            "images/characters/mum_01/mum_01.atlas",
            "images/objects/bed/bed.atlas"
    };
    private Array<Room> rooms;

    public HouseGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
        this.eventHandler = new EventHandler();
        this.rooms = new Array<>();
    }

    @Override
    public void create() {
        extractRooms();
        loadAssets();
        displayUI();

        createRooms();
    }

    public void extractRooms() {
        for (String drmLocation : drmLocations) {
            RoomReader reader = new RoomReader(drmLocation, FileLoader.Location.INTERNAL);
            // Get room scale
            Vector2 roomScale = reader.extractRoomScale();
            // Get tile information in room
            Array<DrmObject> tileDefinitions = reader.extractDefinitions(reader.getTileKey());
            Array<Array<String>> tileGrid = reader.extractGrid(roomScale);
            // Get entity information in room
            Array<DrmObject> entityDefinitions = reader.extractDefinitions(reader.getEntityKey());
            Array<Array<String>> entityGrid = reader.extractGrid(roomScale);

            rooms.add(new Room(roomScale, tileDefinitions, entityDefinitions, tileGrid, entityGrid));
        }
    }

    public void createRooms() {
        for (Room room : rooms) {
            // Tile generation
            terrain = terrainFactory.createRoomTerrain(room);
            spawnEntity(new Entity().addComponent(terrain));

            // Entity generation
            // Go through grid and set tile cells
            Map<String, DrmObject> stringDrmObjectMap = createStringEntityMap(room);
            for (int i = 0; i < room.getTileGrid().size; i++) {
                for (int j = 0; j < room.getEntityGrid().get(i).size; j++) {
                    String current = room.getEntityGrid().get(i).get(j);
                    DrmObject drmObject = stringDrmObjectMap.get(current);
                    try {
                        if (drmObject == null) {
                            continue;
                        }
                        Object[] params;
                        if (drmObject.getTexture().equals("")) {
                            params = new Object[]{new GridPoint2(i, j)};
                        } else {
                            params = new Object[]{new GridPoint2(i, j), drmObject.getTexture()};
                        }
                        drmObject.getMethod().invoke(this, params);
                    } catch (InvocationTargetException e) {
                        logger.error("Couldn't invoke object spawn method");
                    } catch (IllegalAccessException e) {
                        logger.error("No access to invoked spawn method");
                    }
                }
            }
        }
    }

    public void spawnWall(GridPoint2 gridPosition, String texture) {
        Entity newWall = ObstacleFactory.createWall(1f, 1f, texture);
        spawnEntityAt(newWall, gridPosition, true, true);
    }

    public void spawnPlayer(GridPoint2 gridPosition) {
        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, gridPosition, true, true);
        player = newPlayer;
    }

    public void spawnBed(GridPoint2 gridPosition) {
        Entity bed = ObstacleFactory.createBed();
        spawnEntityAt(bed, gridPosition, true, true);
    }

    public void spawnMum(GridPoint2 gridPosition) {
        Entity mum = NPCFactory.createMum(player);
        spawnEntityAt(mum, gridPosition, true, true);
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Box Forest"));
        spawnEntity(ui);
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();

        for (Room room : rooms) {
            for (DrmObject current : room.getTileDefinitions()) {
                if (current.getTexture() != null) {
                    resourceService.loadTexture(current.getTexture());
                }
            }
            for (DrmObject current : room.getEntityDefinitions()) {
                if (current.getTexture() != null) {
                    resourceService.loadTexture(current.getTexture());
                }
            }
        }
        resourceService.loadTextureAtlases(houseTextureAtlases);

        while (!resourceService.loadForMillis(10)) {
        // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        for (Room room : rooms) {
            resourceService.unloadAssets(room.getTileTextures());
            resourceService.unloadAssets(room.getEntityTextures());
        }
        resourceService.unloadAssets(houseTextureAtlases);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.unloadAssets();
    }


    public Map<String, DrmObject> createStringEntityMap(Room room) {
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
        for (int i = 0; i < room.getEntityDefinitions().size; i++) {
            DrmObject current = room.getEntityDefinitions().get(i);
            stringDrmObjectMap.put(current.getSymbol(), current);
        }
        return stringDrmObjectMap;
    }
}
