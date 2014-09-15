package com.sillygames.killingSpree.managers;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.kryonet.Client;
import com.sillygames.killingSpree.clientEntities.ClientPlayer;
import com.sillygames.killingSpree.helpers.Event.State;
import com.sillygames.killingSpree.networking.ControlsSender;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;
import com.sillygames.killingSpree.networking.messages.GameStateMessage;
import com.sillygames.killingSpree.networking.messages.StateProcessor;
import com.sillygames.killingSpree.pool.MessageObjectPool;

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
    private ClientPlayer player;
    
    public WorldRenderer(WorldManager worldManager, Client client) {
        this.worldManager = worldManager;
        stateProcessor = new StateProcessor(client);
        if (worldManager != null) {
            world = worldManager.getWorld();
            box2dRenderer = new Box2DDebugRenderer();
            worldManager.setOutgoingEventListener(stateProcessor);
        } else {
            this.client = client;
        }
        camera = new OrthographicCamera();
        box2dCamera = new OrthographicCamera();
        batch = new SpriteBatch();
        controlsSender = new ControlsSender();
        player = new ClientPlayer(0, 0);
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
        if(count % 60 == 0) {
//            System.gc();
        }
        renderer.setView(camera);
        renderer.render();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderObjects(delta);
        batch.end();
        if (isServer) {
//            box2dRenderer.render(world, box2dCamera.combined);
        }
        processControls();
    }

    private void renderObjects(float delta) {
        long currentTime = TimeUtils.nanoTime();
        stateProcessor.processStateQueue(currentTime);
        GameStateMessage nextStateMessage = stateProcessor.getNextState();
        GameStateMessage previousStateMessage = stateProcessor.getPreviousState();
        long previousTime = previousStateMessage.time;
        long nextTime = nextStateMessage.time;
//        Gdx.app.log("previousTime", Long.toString(previousTime));
//        Gdx.app.log("currentTime ", Long.toString(currentTime + stateProcessor.timeOffset));
//        Gdx.app.log("nextTime    ", Long.toString(nextTime));
        currentTime += stateProcessor.timeOffset;
        float alpha = 0;
        if (nextTime != previousTime)
            alpha = (float)(currentTime - previousTime) / (float)(nextTime - previousTime);
        if (currentTime > nextTime) {
            alpha = 1;
        }
        int counter = Math.min(previousStateMessage.states.size(), 
                nextStateMessage.states.size());
        for (;counter > 0; counter--) {
            player.processState(previousStateMessage.states.get(counter-1), 
                    nextStateMessage.states.get(counter-1), alpha);
            player.render(delta, batch);
        }
//        Gdx.app.log("alpha    ", Float.toString(alpha));
    }

    private void processControls() {

        ControlsMessage message = controlsSender.sendControls();
        
        if(isServer) {
            worldManager.addIncomingEvent(MessageObjectPool.instance.
                    eventPool.obtain().set(State.RECEIVED, message));
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
