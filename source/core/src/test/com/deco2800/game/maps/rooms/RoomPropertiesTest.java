package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.Home;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoomPropertiesTest {

    private static GdxGame game;
    private static HeadlessApplication app;

    @BeforeEach
    void beforeEach() {

        game = new GdxGame();
        final HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        app = new HeadlessApplication(game, config);

        Home home = new Home();
        home.loadProperties();

        assertNotNull(ServiceLocator.getRoomProperties().getResources());
        System.out.println(ServiceLocator.getRoomProperties().getResources());
        System.out.flush();
    }

    @Test
    void shouldReturnRoomInstances() {
        /*ObjectMap.Keys<Class<? extends Room>> keys = new ObjectMap.Keys<>(RoomProperties.getResources());
        while (keys.hasNext()) {
            Class<? extends Room> key = keys.next();
            System.out.println(key.toString());
        }
        System.out.flush();
        */


        assertTrue(ServiceLocator.getRoomProperties().getResources().containsKey(Bathroom.class));
    }
}
