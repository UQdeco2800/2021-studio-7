package com.deco2800.game.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for reading Java objects from JSON files.
 *
 * <p>A generic method is provided already, but methods for reading specific classes can be added
 * for more control.
 */
public class FileLoader {
  private static final Logger logger = LoggerFactory.getLogger(FileLoader.class);
  static final Json json = new Json();

  /**
   * Read generic Java classes from a JSON file. Properties in the JSON file will override class
   * defaults.
   *
   * @param type class type
   * @param filename file to read from
   * @param <T> Class type to read JSON into
   * @return instance of class, may be null
   */
  public static <T> T readClass(Class<T> type, String filename) {
    return readClass(type, filename, Location.INTERNAL);
  }

  /**
   * Read generic Java classes from a JSON file. Properties in the JSON file will override class
   * defaults.
   *
   * @param type class type
   * @param filename file to read from
   * @param location File storage type. See
   *     https://github.com/libgdx/libgdx/wiki/File-handling#file-storage-types
   * @param <T> Class type to read JSON into
   * @return instance of class, may be null
   */
  public static <T> T readClass(Class<T> type, String filename, Location location) {
    logger.debug("Reading class {} from {}", type.getSimpleName(), filename);
    FileHandle file = getFileHandle(filename, location);
    if (file == null) {
      logger.error("Failed to create file handle for {}", filename);
      return null;
    }

    T object;
    try {
      object = json.fromJson(type, file);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return null;
    }
    if (object == null) {
      String path = file.path();
      logger.error("Error creating {} class instance from {}", type.getSimpleName(), path);
    }
    return object;
  }

  /**
   * Write generic Java classes to a JSON file.
   *
   * @param object Java object to write.
   * @param filename File to write to.
   */
  public static void writeClass(Object object, String filename) {
    writeClass(object, filename, Location.EXTERNAL);
  }

  /**
   * Write generic Java classes to a JSON file.
   *
   * @param object Java object to write.
   * @param filename File to write to.
   * @param location File storage type. See
   *     https://github.com/libgdx/libgdx/wiki/File-handling#file-storage-types
   */
  public static void writeClass(Object object, String filename, Location location) {
    logger.debug("Reading class {} from {}", object.getClass().getSimpleName(), filename);
    FileHandle file = getFileHandle(filename, location);
    assert file != null;
    file.writeString(json.prettyPrint(object), false);
  }

  public static List<FileHandle> getJsonFiles(String directory) {
    FileHandle[] files = (new FileHandle(directory)).list(".json");
    List<FileHandle> jsons = new ArrayList<>();
    for (FileHandle file : files) {
      if (!file.isDirectory()) {
        jsons.add(file);
      }
    }
    return jsons;
  }

  public static void assertJsonValueName(JsonValue jsonData, String name) {
    if (!jsonData.name().equals(name)) {
      throw new IllegalArgumentException("JsonValue name " + jsonData.name() + " does not equal " + name);
    }
  }

  public static void assertJsonValueNull(JsonValue jsonData) {
    if (jsonData != null) {
      throw new IllegalArgumentException("Too much information in file");
    }
  }

  public static void readCharacterGrid(String name, Character[][] grid, JsonValue jsonData) {
    FileLoader.assertJsonValueName(jsonData, name);
    JsonValue iterator = jsonData.child();
    for (int y = 0; y < grid.length; y++) {
      JsonValue cellIterator = iterator.child();
      for (int x = 0; x < grid[y].length; x++) {
        grid[y][x] = cellIterator.asChar();
        cellIterator = cellIterator.next();
      }
      iterator = iterator.next();
    }
  }

  public static <V> void readCharacterObjectMap(String name, ObjectMap<Character, V> map, Class<V> valueClass,
                                        Json json, JsonValue jsonData)
          throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    if (valueClass.isAssignableFrom(Json.Serializable.class)) {
      throw new IllegalAccessException("Class does not implement Json.Serializable");
    }
    FileLoader.assertJsonValueName(jsonData, name);
    JsonValue iterator = jsonData.child();
    while (iterator != null) {
      V object = valueClass.getConstructor().newInstance();
      ((Json.Serializable) object).read(json, iterator);
      map.put(iterator.name().charAt(0), object);
      iterator = iterator.next();
    }
  }

  public static FileHandle getFileHandle(String filename, Location location) {
    switch (location) {
      case CLASSPATH:
        return Gdx.files.classpath(filename);
      case INTERNAL:
        return Gdx.files.internal(filename);
      case LOCAL:
        return Gdx.files.local(filename);
      case EXTERNAL:
        return Gdx.files.external(filename);
      case ABSOLUTE:
        return Gdx.files.absolute(filename);
      default:
        return null;
    }
  }

  public enum Location {
    CLASSPATH,
    INTERNAL,
    LOCAL,
    EXTERNAL,
    ABSOLUTE
  }
}
