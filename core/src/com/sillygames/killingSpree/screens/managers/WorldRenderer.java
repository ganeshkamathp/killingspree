package com.sillygames.killingSpree.screens.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sillygames.killingSpree.controls.ControlsMessage;
import com.sillygames.killingSpree.controls.InputController;
import com.sillygames.killingSpree.managers.WorldManager;
import com.sillygames.killingSpree.networking.MyClient;
import com.sillygames.killingSpree.pooler.ObjectPool;

public class WorldRenderer {
    
    private WorldManager worldManager;
    private World world;
    private OrthographicCamera camera;
    private OrthographicCamera box2dCamera;
    private OrthogonalTiledMapRenderer renderer;
    private FitViewport viewport;
    private FitViewport box2dViewport;
    private Box2DDebugRenderer box2dRenderer;
    private TiledMap map;
    private boolean server;
    public final static float SCALE = 10;
    
    public WorldRenderer(WorldManager worldManager) {
        this.worldManager = worldManager;
        if (worldManager != null) {
            world = worldManager.getWorld();
            box2dRenderer = new Box2DDebugRenderer();
        }
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 450);
        box2dCamera = new OrthographicCamera();
        box2dCamera.setToOrtho(false, 600/SCALE, 450/SCALE);
        viewport = new FitViewport(600, 450, camera);
        box2dViewport = new FitViewport(600/SCALE, 450/SCALE, box2dCamera);
    }

    public void loadLevel(String level, boolean server) {
        this.server = server;
        map = new TmxMapLoader().load(level);
        TiledMapTileLayer collision = (TiledMapTileLayer) map.
                getLayers().get("collision");
        collision.setVisible(false);
        
        if (server) {
            for (int x = 0; x < collision.getWidth(); x++) {
                for (int y = 0; y < collision.getHeight(); y++) {
                    if(collision.getCell(x, y) != null) {
                        worldManager.addBox(7.5f/SCALE, 
                                7.5f/SCALE,
                                (x * 15f + 7.5f)/SCALE,
                                (y * 15f + 7.5f)/SCALE,
                                BodyType.StaticBody);
                    }
                }
            }
        }
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    public void render() {
        renderer.setView(camera);
        renderer.render();
        if (server) {
            box2dRenderer.render(world, box2dCamera.combined);
        }
        processControls();
    }

    private void processControls() {
        ControlsMessage message = ObjectPool.instance.
                controlsMessagePool.obtain();
        message.direction = 0;
        if (InputController.instance.axisRight()) {
            message.direction = 3;
        } else if (InputController.instance.axisLeft()) {
            message.direction = 7;
        }
        if (InputController.instance.buttonA()) {
            message.action = 2;
        }
        if (InputController.instance.buttonX()) {
            message.action += 1;
        }
        
        MyClient.instance.client.sendTCP(message);
        ObjectPool.instance.
                controlsMessagePool.free(message);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        box2dViewport.update(width, height);
        camera.update();
        box2dCamera.update();
    }
    
}
