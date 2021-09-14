package com.deco2800.game.areas.rooms.jaleel;

import com.badlogic.gdx.files.FileHandle;
import com.deco2800.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoomLoader {

    private static final Logger logger = LoggerFactory.getLogger(RoomLoader.class);

    public static FileHandle getRoomFileHandle(String filename, FileLoader.Location location) {
        FileHandle file = FileLoader.getFileHandle(filename, location);
        if (file == null) {
            logger.error("Failed to create file handle for {}", filename);
            return null;
        } else if (file.extension().equals("drm")) {
            logger.error("{} is not a .drm file", filename);
            return null;
        }
        return file;
    }
}
