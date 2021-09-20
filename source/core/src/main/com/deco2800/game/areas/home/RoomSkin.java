package com.deco2800.game.areas.home;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.deco2800.game.areas.home.rooms.*;

/** A skin stores resources for UI widgets to use (texture regions, ninepatches, fonts, colors, etc). Resources are named and can
 * be looked up by name and type. Resources can be described in JSON. Skin provides useful conversions, such as allowing access to
 * regions in the atlas as ninepatches, sprites, drawables, etc. The get* methods return an instance of the object in the skin.
 * The new* methods return a copy of an instance in the skin.
 * <p>
 * See the <a href="https://github.com/libgdx/libgdx/wiki/Skin">documentation</a> for more.
 * @author Nathan Sweet */
public class RoomSkin implements Disposable {
    ObjectMap<Class, ObjectMap<String, Object>> resources = new ObjectMap();

    private final ObjectMap<String, Class> jsonClassTags = new ObjectMap(defaultTagClasses.length);
    {
        for (Class c : defaultTagClasses)
            jsonClassTags.put(c.getSimpleName(), c);
    }

    /**
     * Creates an empty skin.
     */
    public RoomSkin() {
    }

    /**
     * Creates a skin containing the resources in the specified skin JSON file. If a file in the same directory with a ".atlas"
     * extension exists, it is loaded as a {@link TextureAtlas} and the texture regions added to the skin. The atlas is
     * automatically disposed when the skin is disposed.
     */
    public RoomSkin(FileHandle skinFile) {
        load(skinFile);
    }


    /** Adds all resources in the specified skin JSON file. */
    public void load (FileHandle skinFile) {
        try {
            getJsonLoader(skinFile).fromJson(Skin.class, skinFile);
        } catch (SerializationException ex) {
            throw new SerializationException("Error reading file: " + skinFile, ex);
        }
    }

    /** Returns a resource named "default" for the specified type.
     * @throws GdxRuntimeException if the resource was not found. */
    public <T> T get (Class<T> type) {
        return get("default", type);
    }

    /** Returns a named resource of the specified type.
     * @throws GdxRuntimeException if the resource was not found. */
    public <T> T get (String name, Class<T> type) {
        if (name == null) throw new IllegalArgumentException("name cannot be null.");
        if (type == null) throw new IllegalArgumentException("type cannot be null.");

        if (type == Drawable.class) return (T)getDrawable(name);
        if (type == TextureRegion.class) return (T)getRegion(name);
        if (type == NinePatch.class) return (T)getPatch(name);
        if (type == Sprite.class) return (T)getSprite(name);

        ObjectMap<String, Object> typeResources = resources.get(type);
        if (typeResources == null) throw new GdxRuntimeException("No " + type.getName() + " registered with name: " + name);
        Object resource = typeResources.get(name);
        if (resource == null) throw new GdxRuntimeException("No " + type.getName() + " registered with name: " + name);
        return (T)resource;
    }

    protected Json getJsonLoader (final FileHandle roomSkinFile) {
        final RoomSkin roomSkin = this;

        final Json json = new Json() {
            static private final String parentFieldName = "parent";

            public <T> T readValue (Class<T> type, Class elementType, JsonValue jsonData) {
                // If the JSON is a string but the type is not, look up the actual value by name.
                if (jsonData != null && jsonData.isString() && !ClassReflection.isAssignableFrom(CharSequence.class, type))
                    return get(jsonData.asString(), type);
                return super.readValue(type, elementType, jsonData);
            }

            protected boolean ignoreUnknownField (Class type, String fieldName) {
                return fieldName.equals(parentFieldName);
            }

            public void readFields (Object object, JsonValue jsonMap) {
                if (jsonMap.has(parentFieldName)) {
                    String parentName = readValue(parentFieldName, String.class, jsonMap);
                    Class parentType = object.getClass();
                    while (true) {
                        try {
                            copyFields(get(parentName, parentType), object);
                            break;
                        } catch (GdxRuntimeException ex) { // Parent resource doesn't exist.
                            parentType = parentType.getSuperclass(); // Try resource for super class.
                            if (parentType == Object.class) {
                                SerializationException se = new SerializationException(
                                        "Unable to find parent resource with name: " + parentName);
                                se.addTrace(jsonMap.child.trace());
                                throw se;
                            }
                        }
                    }
                }
                super.readFields(object, jsonMap);
            }
        };
        json.setTypeName(null);
        json.setUsePrototypes(false);

        json.setSerializer(Skin.class, new Json.ReadOnlySerializer<Skin>() {
            public Skin read (Json json, JsonValue typeToValueMap, Class ignored) {
                for (JsonValue valueMap = typeToValueMap.child; valueMap != null; valueMap = valueMap.next) {
                    try {
                        Class type = json.getClass(valueMap.name());
                        if (type == null) type = ClassReflection.forName(valueMap.name());
                        readNamedObjects(json, type, valueMap);
                    } catch (ReflectionException ex) {
                        throw new SerializationException(ex);
                    }
                }
                return skin;
            }

            private void readNamedObjects (Json json, Class type, JsonValue valueMap) {
                Class addType = type == Skin.TintedDrawable.class ? Drawable.class : type;
                for (JsonValue valueEntry = valueMap.child; valueEntry != null; valueEntry = valueEntry.next) {
                    Object object = json.readValue(type, valueEntry);
                    if (object == null) continue;
                    try {
                        add(valueEntry.name, object, addType);
                        if (addType != Drawable.class && ClassReflection.isAssignableFrom(Drawable.class, addType))
                            add(valueEntry.name, object, Drawable.class);
                    } catch (Exception ex) {
                        throw new SerializationException(
                                "Error reading " + ClassReflection.getSimpleName(type) + ": " + valueEntry.name, ex);
                    }
                }
            }
        });

        json.setSerializer(BitmapFont.class, new Json.ReadOnlySerializer<BitmapFont>() {
            public BitmapFont read (Json json, JsonValue jsonData, Class type) {
                String path = json.readValue("file", String.class, jsonData);
                int scaledSize = json.readValue("scaledSize", int.class, -1, jsonData);
                Boolean flip = json.readValue("flip", Boolean.class, false, jsonData);
                Boolean markupEnabled = json.readValue("markupEnabled", Boolean.class, false, jsonData);

                FileHandle fontFile = skinFile.parent().child(path);
                if (!fontFile.exists()) fontFile = Gdx.files.internal(path);
                if (!fontFile.exists()) throw new SerializationException("Font file not found: " + fontFile);

                // Use a region with the same name as the font, else use a PNG file in the same directory as the FNT file.
                String regionName = fontFile.nameWithoutExtension();
                try {
                    BitmapFont font;
                    Array<TextureRegion> regions = skin.getRegions(regionName);
                    if (regions != null)
                        font = new BitmapFont(new BitmapFont.BitmapFontData(fontFile, flip), regions, true);
                    else {
                        TextureRegion region = skin.optional(regionName, TextureRegion.class);
                        if (region != null)
                            font = new BitmapFont(fontFile, region, flip);
                        else {
                            FileHandle imageFile = fontFile.parent().child(regionName + ".png");
                            if (imageFile.exists())
                                font = new BitmapFont(fontFile, imageFile, flip);
                            else
                                font = new BitmapFont(fontFile, flip);
                        }
                    }
                    font.getData().markupEnabled = markupEnabled;
                    // Scaled size is the desired cap height to scale the font to.
                    if (scaledSize != -1) font.getData().setScale(scaledSize / font.getCapHeight());
                    return font;
                } catch (RuntimeException ex) {
                    throw new SerializationException("Error loading bitmap font: " + fontFile, ex);
                }
            }
        });

        json.setSerializer(Color.class, new Json.ReadOnlySerializer<Color>() {
            public Color read (Json json, JsonValue jsonData, Class type) {
                if (jsonData.isString()) return get(jsonData.asString(), Color.class);
                String hex = json.readValue("hex", String.class, (String)null, jsonData);
                if (hex != null) return Color.valueOf(hex);
                float r = json.readValue("r", float.class, 0f, jsonData);
                float g = json.readValue("g", float.class, 0f, jsonData);
                float b = json.readValue("b", float.class, 0f, jsonData);
                float a = json.readValue("a", float.class, 1f, jsonData);
                return new Color(r, g, b, a);
            }
        });

        json.setSerializer(Skin.TintedDrawable.class, new Json.ReadOnlySerializer() {
            public Object read (Json json, JsonValue jsonData, Class type) {
                String name = json.readValue("name", String.class, jsonData);
                Color color = json.readValue("color", Color.class, jsonData);
                if (color == null) throw new SerializationException("TintedDrawable missing color: " + jsonData);
                Drawable drawable = newDrawable(name, color);
                if (drawable instanceof BaseDrawable) {
                    BaseDrawable named = (BaseDrawable)drawable;
                    named.setName(jsonData.name + " (" + name + ", " + color + ")");
                }
                return drawable;
            }
        });

        for (ObjectMap.Entry<String, Class> entry : jsonClassTags)
            json.addClassTag(entry.key, entry.value);

        return json;
    }


    /** Returns a map of class tags that will be used when loading room skin JSON. The map can
     * be modified before calling load(FileHandle). By default the map is populated with the simple class names of libGDX
     * classes commonly used in skins. */
    public ObjectMap<String, Class> getJsonClassTags () {
        return jsonClassTags;
    }

    static private final Class[] defaultTagClasses = {Bathroom.BathroomStyle.class, Bedroom.BedroomStyle.class,
            Dining.DiningStyle.class, FrontFoyer.FrontFoyerStyle.class, Garage.GarageStyle.class,
            Kitchen.KitchenStyle.class, Laundry.LaundryStyle.class, Living.LivingStyle.class};

    @Null
    static private Method findMethod (Class type, String name) {
        Method[] methods = ClassReflection.getMethods(type);
        for (int i = 0, n = methods.length; i < n; i++) {
            Method method = methods[i];
            if (method.getName().equals(name)) return method;
        }
        return null;
    }
}