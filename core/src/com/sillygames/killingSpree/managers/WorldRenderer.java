package com.sillygames.killingSpree.managers;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.kryonet.Client;
import com.sillygames.killingSpree.entities.Entity;
import com.sillygames.killingSpree.entities.Player;
import com.sillygames.killingSpree.networking.ControlsSender;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;
import com.sillygames.killingSpree.networking.messages.EntityState;
import com.sillygames.killingSpree.networking.messages.GameStateMessage;
import com.sillygames.killingSpree.networking.messages.StateProcessor;

public class WorldRenderer {
    
    public final static float SCALE = 10;
    public static float VIEWPORT_WIDTH = 525;
    public static float VIEWPORT_HEIGHT = 375;
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
    private SpriteBatch batch;
    private Client client;
    private int count;
    private ControlsSender controlsSender;
    private StateProcessor stateProcessor;
    private Player player;
    private ArrayList<Entity> entities;
    
    public WorldRenderer(WorldManager worldManager, Client client) {
        this.worldManager = worldManager;
        if (worldManager != null) {
            world = worldManager.getWorld();
            box2dRenderer = new Box2DDebugRenderer();
            entities = worldManager.getEntities();
        } else {
            entities = new ArrayList<Entity>();
            this.client = client;
            stateProcessor = new StateProcessor(client);
        }
        camera = new OrthographicCamera();
        box2dCamera = new OrthographicCamera();
        batch = new SpriteBatch();
        controlsSender = new ControlsSender();
        player = new Player(0, 0);
        player.loadAssets();
    }

    public void loadLevel(String level, boolean isServer) {
        this.isServer = isServer;
        map = new TmxMapLoader().load(level);
        TiledMapTileLayer layer = (TiledMapTileLayer) map.
                                    getLayers().get("terrain");
        VIEWPORT_WIDTH = layer.getTileWidth() * layer.getWidth();
        VIEWPORT_HEIGHT = layer.getTileHeight() * layer.getHeight();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        box2dCamera.setToOrtho(false, VIEWPORT_WIDTH / SCALE,
                VIEWPORT_HEIGHT/  SCALE);
        viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        box2dViewport = new FitViewport(VIEWPORT_WIDTH / SCALE,
                VIEWPORT_HEIGHT / SCALE, box2dCamera);
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
        // Temp experiment to check GC
        count++;
        if(count % 100 == 0) {
            System.gc();
        }
        renderer.setView(camera);
        renderer.render();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (isServer) {
            renderObjects(delta, worldManager.currentGameState);
        } else {
            renderObjects(delta, stateProcessor.getCurrentStates());
        }
        batch.end();
        if (isServer) {
//            box2dRenderer.render(world, box2dCamera.combined);
        }
        processControls();
    }

    private void renderObjects(float delta, GameStateMessage currentGameState) {
        for (EntityState state : currentGameState.states) {
            player.setPosition(state.x, state.y);
            player.render(delta, batch);
        }
    }

    private void processControls() {

        ControlsMessage message = controlsSender.sendControls();
        
        if(isServer) {
            worldManager.player.setCurrentControls(message);
        } else {
            client.sendUDP(message);
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        box2dViewport.update(width, height);
        camera.update();
        box2dCamera.update();
    }
    
}
