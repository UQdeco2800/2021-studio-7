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
        ServiceLocator.registerHome(home);
        home.loadProperties();

        assertNotNull(ServiceLocator.getHome().getRoomProperties().getResources());
    }

    @Test
    void shouldReturnRoomInstances() {
        RoomProperties roomProperties = ServiceLocator.getHome().getRoomProperties();
        assertTrue(roomProperties.getResources().containsKey(Bathroom.class));
        assertTrue(roomProperties.getResources().containsKey(Bedroom.class));
        assertTrue(roomProperties.getResources().containsKey(Dining.class));
        assertTrue(roomProperties.getResources().containsKey(FrontFoyer.class));
        assertTrue(roomProperties.getResources().containsKey(Garage.class));
        assertTrue(roomProperties.getResources().containsKey(Kitchen.class));
        assertTrue(roomProperties.getResources().containsKey(Laundry.class));
        assertTrue(roomProperties.getResources().containsKey(Living.class));
    }
}
