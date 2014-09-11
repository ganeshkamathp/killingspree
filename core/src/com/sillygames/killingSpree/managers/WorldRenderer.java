package com.sillygames.killingSpree.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.kryonet.Client;
import com.sillygames.killingSpree.controls.ControlsMessage;
import com.sillygames.killingSpree.controls.InputController;
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
    private boolean isServer;
    public final static float SCALE = 10;
    SpriteBatch batch;
    Client client;
    
    public WorldRenderer(WorldManager worldManager, Client client) {
        this.worldManager = worldManager;
        if (worldManager != null) {
            world = worldManager.getWorld();
            box2dRenderer = new Box2DDebugRenderer();
        }
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 615, 450);
        box2dCamera = new OrthographicCamera();
        box2dCamera.setToOrtho(false, 615/SCALE, 450/SCALE);
        viewport = new FitViewport(615, 450, camera);
        box2dViewport = new FitViewport(615/SCALE, 450/SCALE, box2dCamera);
        batch = new SpriteBatch();
        this.client = client;
    }

    public void loadLevel(String level, boolean isServer) {
        this.isServer = isServer;
        map = new TmxMapLoader().load(level);
        renderer = new OrthogonalTiledMapRenderer(map);

        if (isServer) {
            MapLayer collision =  map.
                    getLayers().get("collision");
            for(MapObject object: collision.getObjects()) {
                worldManager.createWorldObject(object);
            }
        }
    }

    public void render(float delta) {
        renderer.setView(camera);
        renderer.render();
        if (isServer) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            worldManager.player.updateAndRender(delta, batch);
            batch.end();
            box2dRenderer.render(world, box2dCamera.combined);
        }
        if(Gdx.app.getType() == ApplicationType.Android)
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
        
        client.sendTCP(message);
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
