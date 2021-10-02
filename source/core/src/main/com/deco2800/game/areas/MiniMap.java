package com.deco2800.game.areas;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MiniMap {

    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera = new OrthographicCamera();

    public MiniMap(TiledMap map) {
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);

        camera.setToOrtho(false, 30, 20);

        camera.zoom = 10;
    }

    public void update(){

    }

    public void update(float x, float y){

        //Pixventure.instance.gameScreen.getCamera()

        camera.position.x = x;
        camera.position.y = y;
        camera.update();

        renderer.setView(camera.combined, x-15, y-15, 30, 30);
        //renderer.setView(camera);
    }

    public void render(){
        renderer.render();
    }

}

