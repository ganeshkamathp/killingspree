package com.sillygames.killingSpree.screens.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.sillygames.killingSpree.managers.WorldManager;

public class WorldRenderer {
    
    private WorldManager worldManager;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private float aspectRatio;
    
    public WorldRenderer(WorldManager worldManager) {
        this.worldManager = worldManager;
        world = worldManager.getWorld();
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
    }

    public void render() {
        debugRenderer.render(world, camera.combined);
    }

    public void resize(int width, int height) {
        aspectRatio = (float)width / height;
        camera.setToOrtho(false, 100 * aspectRatio, 100);
        worldManager.setRatio(height / 100.0f);
    }
    
    
    
}
