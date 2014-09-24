package com.sillygames.killingSpree.managers;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.kryonet.Client;
import com.sillygames.killingSpree.clientEntities.ClientArrow;
import com.sillygames.killingSpree.clientEntities.ClientBlob;
import com.sillygames.killingSpree.clientEntities.ClientBullet;
import com.sillygames.killingSpree.clientEntities.ClientEntity;
import com.sillygames.killingSpree.clientEntities.ClientFly;
import com.sillygames.killingSpree.clientEntities.ClientFrog;
import com.sillygames.killingSpree.clientEntities.ClientPlayer;
import com.sillygames.killingSpree.controls.onScreenControls;
import com.sillygames.killingSpree.helpers.EntityUtils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.helpers.Event.State;
import com.sillygames.killingSpree.managers.physics.World;
import com.sillygames.killingSpree.managers.physics.WorldDebugRenderer;
import com.sillygames.killingSpree.networking.ControlsSender;
import com.sillygames.killingSpree.networking.StateProcessor;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;
import com.sillygames.killingSpree.networking.messages.EntityState;
import com.sillygames.killingSpree.networking.messages.GameStateMessage;
import com.sillygames.killingSpree.pool.MessageObjectPool;

public class WorldRenderer {
    
    public static int VIEWPORT_WIDTH = 525;
    public static int VIEWPORT_HEIGHT = 375;
    private WorldManager worldManager;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private FitViewport viewport;
    private TiledMap map;
    private boolean isServer;
    private SpriteBatch batch;
    private Client client;
    private int count;
    private ControlsSender controlsSender;
    private StateProcessor stateProcessor;
    private ConcurrentHashMap<Short, ClientEntity> worldMap;
    long previousTime;
    private WorldDebugRenderer debugRenderer;
    private onScreenControls controls;
    private int screenWidth;
    private int screenHeight;
    private short recentId;
    
    public WorldRenderer(WorldManager worldManager, Client client) {
        worldMap = new ConcurrentHashMap<Short, ClientEntity>();
        this.worldManager = worldManager;
        stateProcessor = new StateProcessor(client, worldMap);
        if (worldManager != null) {
            debugRenderer = new WorldDebugRenderer(worldManager.getWorld());
            worldManager.setOutgoingEventListener(stateProcessor);
        } else {
            this.client = client;
        }
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        controlsSender = new ControlsSender();
        recentId = -2;
    }

    public void loadLevel(String level, boolean isServer) {
        this.isServer = isServer;
        map = new TmxMapLoader().load(level);
        TiledMapTileLayer layer = (TiledMapTileLayer) map.
                                    getLayers().get("terrain");
        VIEWPORT_WIDTH = (int) (layer.getTileWidth() * layer.getWidth());
        VIEWPORT_HEIGHT = (int) (layer.getTileHeight() * layer.getHeight());
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        renderer = new OrthogonalTiledMapRenderer(map);

        if (isServer) {
            MapLayer collision =  map.
                    getLayers().get("collision");
            for(MapObject object: collision.getObjects()) {
                worldManager.createWorldObject(object);
            }
        }
        controls = new onScreenControls();
    }

    public void render(float delta) {
        // Temp experiment to check GC
        count++;
        if(count % 60 == 0) {
//            System.gc();
        }
        viewport.apply();
        renderer.setView(camera);
        renderer.render();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderObjects(delta);
        batch.end();
        if (isServer) {
//            debugRenderer.render(camera.combined);
        }
        processControls();
        Gdx.gl.glViewport(0, 0, screenWidth, screenHeight);
        controls.render();
    }

    private void renderObjects(float delta) {
        long currentTime = TimeUtils.nanoTime();
        float alpha = 0;
        GameStateMessage nextStateMessage;
        if (isServer) {
            alpha = 1;
            nextStateMessage = stateProcessor.stateQueue.get(stateProcessor.stateQueue.size() - 1);
        } else {
            stateProcessor.processStateQueue(currentTime);
            nextStateMessage = stateProcessor.getNextState();
            long nextTime = nextStateMessage.time;
            currentTime += stateProcessor.timeOffset;
            if (nextTime != previousTime)
                alpha = (float)(currentTime - previousTime) / (float)(nextTime - previousTime);
            if (currentTime > nextTime) {
                alpha = 1;
            }
        }
        
        for (EntityState state: nextStateMessage.states) {
            short id = recentId;
            if (!worldMap.containsKey(state.id) && state.id > recentId) {
                ClientEntity entity = null;
                if (EntityUtils.ByteToActorType(state.type) == ActorType.PLAYER) {
                    entity = new ClientPlayer(state.id, state.x, state.y);
                } else if (EntityUtils.ByteToActorType(state.type) == ActorType.BLOB) {
                    entity = new ClientBlob(state.id, state.x, state.y);
                } else if (EntityUtils.ByteToActorType(state.type) == ActorType.ARROW) {
                    entity = new ClientArrow(state.id, state.x, state.y);
                } else if (EntityUtils.ByteToActorType(state.type) == ActorType.BULLET) {
                    entity = new ClientBullet(state.id, state.x, state.y);
                } else if (EntityUtils.ByteToActorType(state.type) == ActorType.FLY) {
                    entity = new ClientFly(state.id, state.x, state.y);
                } else if (EntityUtils.ByteToActorType(state.type) == ActorType.FROG) {
                    entity = new ClientFrog(state.id, state.x, state.y);
                } else {
                    Gdx.app.log("Error", "Couldnt decode actor type");
                    Gdx.app.exit();
                }
                worldMap.put(state.id, entity);
                id = (short) Math.max(id, state.id);
            }
            recentId = id;
        }

        for (EntityState state: nextStateMessage.states) {
            if (worldMap.get(state.id) != null) {
                worldMap.get(state.id).processState(state, alpha);
            }
        }
        for (ClientEntity entity: worldMap.values()) {
            if (entity.remove) {
                worldMap.remove(entity.id);
                continue;
            }
            entity.render(delta, batch);
        }
//        Gdx.app.log("alpha    ", Float.toString(alpha));
        previousTime = currentTime;
        
    }

    private void processControls() {

        ControlsMessage message = controlsSender.sendControls(controls);
        
        if(isServer) {
            worldManager.addIncomingEvent(MessageObjectPool.instance.
                    eventPool.obtain().set(State.RECEIVED, message));
        } else {
            client.sendUDP(message);
        }
    }

    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        viewport.update(width, height);
        camera.update();
    }
    
}
