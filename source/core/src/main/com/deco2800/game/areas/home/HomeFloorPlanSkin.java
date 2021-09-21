package com.deco2800.game.areas.home;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.roomtypes.RoomType;
import com.deco2800.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeFloorPlanSkin {
    protected static final Logger logger = LoggerFactory.getLogger(RoomType.class);
    HomeFloorPlanWrapper resources;

    public HomeFloorPlanSkin() {
    }

    public HomeFloorPlanSkin(FileHandle skinFile) {
        load(skinFile);
    }

    public void load(FileHandle skinFile) {
        resources = FileLoader.readClass(HomeFloorPlanWrapper.class, skinFile.path());
    }

    public HomeFloorPlan get() {
        return get("default");
    }

    public HomeFloorPlan get(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null.");
        }
        return resources.stuff.get(name);
    }

    public static class HomeFloorPlanWrapper {
        public ObjectMap<String, HomeFloorPlan> stuff = new ObjectMap<>();
    }
}