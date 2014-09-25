package com.sillygames.killingSpree.managers;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.sillygames.killingSpree.helpers.Event;
import com.sillygames.killingSpree.helpers.MyConnection;
import com.sillygames.killingSpree.helpers.WorldBodyUtils;
import com.sillygames.killingSpree.helpers.Event.State;
import com.sillygames.killingSpree.managers.physics.Body;
import com.sillygames.killingSpree.managers.physics.World;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;
import com.sillygames.killingSpree.networking.messages.EntityState;
import com.sillygames.killingSpree.networking.messages.GameStateMessage;
import com.sillygames.killingSpree.pool.MessageObjectPool;
import com.sillygames.killingSpree.serverEntities.ServerBlob;
import com.sillygames.killingSpree.serverEntities.ServerEntity;
import com.sillygames.killingSpree.serverEntities.ServerFly;
import com.sillygames.killingSpree.serverEntities.ServerFrog;
import com.sillygames.killingSpree.serverEntities.ServerPlayer;

public class WorldManager{
    
    private World world;
    private Server server;
    public HashMap<Integer, ServerPlayer> playerList;
    public ArrayList<ServerEntity> entities;
    private WorldManager worldManager = this;
    private ArrayList<Event> incomingEventQueue;
    private ArrayList<Event> outgoingEventQueue;
    private Listener serverListener;
    private Listener outgoingEventListener;
    private WorldBodyUtils worldBodyUtils;
    public MyConnection dummyConnection;
    public short id;
//    public ServerFrog frog;
//    public ServerFly fly;
//    public ServerBlob blob;
    public LevelLoader loader;
    
    public WorldManager(final Server server){
        
        playerList = new HashMap<Integer, ServerPlayer>();
        
        world = new World(new Vector2(0, -500f));
        
        serverListener = new WorldManagerServerListener();

        if  (server != null) {
            server.addListener(serverListener);
        }
        
        this.server = server;
        
        entities = new ArrayList<ServerEntity>();
        
        incomingEventQueue = new ArrayList<Event>();
        outgoingEventQueue = new ArrayList<Event>();
        dummyConnection = new MyConnection();
        incomingEventQueue.add(MessageObjectPool.instance.
                eventPool.obtain().set(State.CONNECTED, null));
        outgoingEventQueue.add(MessageObjectPool.instance.
                eventPool.obtain().set(State.CONNECTED, null));
        worldBodyUtils = new WorldBodyUtils(worldManager);

        id = 0;
//        frog = new ServerFrog(id++, 20, 100, worldBodyUtils);
//        entities.add(frog);
//        fly = new ServerFly(id++, 20, 100, worldBodyUtils);
//        entities.add(fly);
//        blob = new ServerBlob(id++, 20, 100, worldBodyUtils);
//        entities.add(blob);
        loader = new LevelLoader("maps/retro-small.txt", this);
    }
    
    public void setOutgoingEventListener(Listener listener) {
        outgoingEventListener = listener;
    }
    
    public ServerFrog addFrog(float x, float y) {
        ServerFrog frog = new ServerFrog(id++, x, y, worldBodyUtils);
        entities.add(frog);
        return frog;
    }
    
    public ServerFly addFly(float x, float y) {
        ServerFly fly = new ServerFly(id++, x, y, worldBodyUtils);
        entities.add(fly);
        return fly;
    }
    
    public ServerBlob addBlob(float x, float y) {
        ServerBlob blob = new ServerBlob(id++, x, y, worldBodyUtils);
        entities.add(blob);
        return blob;
    }

    public void update(float delta) {
        loader.loadNextLine(delta);
        for(ServerEntity entity: entities) {
            entity.update(delta);
        }
        world.step(delta, 1, this);
        
        GameStateMessage gameStateMessage = MessageObjectPool.instance.
                gameStateMessagePool.obtain();
        for (ServerEntity entity: entities) {
            EntityState state = MessageObjectPool.instance.
                                    entityStatePool.obtain();
            entity.updateState(state);
            gameStateMessage.addNewState(state);
        }
        
        entities.addAll(worldBodyUtils.entities);
        worldBodyUtils.entities.clear();

        gameStateMessage.time = TimeUtils.nanoTime();
        if (server != null) {
            server.sendToAllUDP(gameStateMessage);
        }
        addOutgoingEvent(MessageObjectPool.instance.
                eventPool.obtain().set(State.RECEIVED, gameStateMessage));
        
        processEvents(serverListener, incomingEventQueue);
        processEvents(outgoingEventListener, outgoingEventQueue);
//        if (frog.body.toDestroy ) {
//            frog = new ServerFrog(id++, 20, 100, worldBodyUtils);
//            entities.add(frog);
//        }
//        if (fly.body.toDestroy ) {
//            fly = new ServerFly(id++, 20, 100, worldBodyUtils);
//            entities.add(fly);
//        }
//        if (blob.body.toDestroy ) {
//            blob = new ServerBlob(id++, 20, 100, worldBodyUtils);
//            entities.add(blob);
//        }
    }


    public World getWorld() {
        return world;
    }
    
    
    public ArrayList<ServerEntity> getEntities() {
        return entities;
    }

    private class WorldManagerServerListener extends Listener {
        @Override
        public void connected(Connection connection) {
            server.sendToTCP(connection.getID(), "start");
        }
        
        @Override
        public void received(Connection connection, Object object) {
            if (object instanceof ControlsMessage) {
                try {
                    playerList.get(connection.getID()).
                    setCurrentControls((ControlsMessage) object);
                }
                catch(Exception e) {
                    server.sendToTCP(connection.getID(), "start");
                    ServerPlayer player = new ServerPlayer(id++, 50, 150, worldBodyUtils);
                    playerList.put(connection.getID(), player);
                    entities.add(player);
                }
            }
        }
        
        @Override
        public void disconnected(Connection connection) {
            ServerPlayer player = playerList.get(connection.getID());
            if (player != null) {
                player.dispose();
                playerList.remove(connection.getID());
                entities.remove(player);
                if (server!= null) {
                    server.sendToAllTCP(player.id);
                }
                addOutgoingEvent(MessageObjectPool.instance.
                        eventPool.obtain().set(State.RECEIVED, player.id));
            }
        }
    }

    private void processEvents(Listener listener, ArrayList<Event> queue) {
        for (Event event: queue) {
            if (event.state == State.CONNECTED) {
                listener.connected(dummyConnection);
            } else if (event.state == State.RECEIVED) {
                listener.received(dummyConnection, event.object);
            } else if (event.state == State.DISCONNECTED) {
                listener.disconnected(dummyConnection);
            }
        }
        queue.clear();
    }
    
    public void addIncomingEvent(Event event) {
        incomingEventQueue.add(event);
    }
    
    public void addOutgoingEvent(Event event) {
        outgoingEventQueue.add(event);
    }

    public void createWorldObject(MapObject object) {
        worldBodyUtils.createWorldObject(object);
    }
    
    public void destroyBody(Body body) {
        entities.remove(body.getUserData());
        server.sendToAllTCP(body.getUserData().id);
        addOutgoingEvent(MessageObjectPool.instance.
                eventPool.obtain().set(State.RECEIVED, body.getUserData().id));
    }
    
}

