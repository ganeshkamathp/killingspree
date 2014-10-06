package com.sillygames.killingSpree.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.sillygames.killingSpree.helpers.Event;
import com.sillygames.killingSpree.helpers.MyConnection;
import com.sillygames.killingSpree.helpers.Event.State;
import com.sillygames.killingSpree.managers.physics.Body;
import com.sillygames.killingSpree.managers.physics.World;
import com.sillygames.killingSpree.networking.messages.AudioMessage;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;
import com.sillygames.killingSpree.networking.messages.EntityState;
import com.sillygames.killingSpree.networking.messages.GameStateMessage;
import com.sillygames.killingSpree.networking.messages.ClientDetailsMessage;
import com.sillygames.killingSpree.networking.messages.PlayerNamesMessage;
import com.sillygames.killingSpree.networking.messages.ServerStatusMessage;
import com.sillygames.killingSpree.networking.messages.ServerStatusMessage.Status;
import com.sillygames.killingSpree.pool.MessageObjectPool;
import com.sillygames.killingSpree.screens.settings.Constants;
import com.sillygames.killingSpree.serverEntities.ServerBlob;
import com.sillygames.killingSpree.serverEntities.ServerEntity;
import com.sillygames.killingSpree.serverEntities.ServerFly;
import com.sillygames.killingSpree.serverEntities.ServerFrog;
import com.sillygames.killingSpree.serverEntities.ServerPlayer;

public class WorldManager{
    
    private World world;
    private Server server;
    public ConcurrentHashMap<Integer, ServerPlayer> playerList;
    public ArrayList<ServerEntity> entities;
    private WorldManager worldManager = this;
    private ArrayList<Event> incomingEventQueue;
    private ArrayList<Event> outgoingEventQueue;
    private Listener serverListener;
    private Listener outgoingEventListener;
    private WorldBodyUtils worldBodyUtils;
    public MyConnection dummyConnection;
    public short id;
    public AudioMessage audio;
//    public ServerFrog frog;
//    public ServerFly fly;
//    public ServerBlob blob;
    public LevelLoader loader;
    ArrayList<Vector2> playerPositions;
    
    public int i;
    
    public WorldManager(final Server server){
        i = 0;
        
        playerPositions = new ArrayList<Vector2>();
        playerPositions.add(new Vector2(50,85));
        playerPositions.add(new Vector2(395,85));
        playerPositions.add(new Vector2(50,230));
        playerPositions.add(new Vector2(395,230));
        
        playerList = new ConcurrentHashMap<Integer, ServerPlayer>();
        
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
        audio = new AudioMessage();

        worldBodyUtils = new WorldBodyUtils(worldManager);

        id = 0;
//        frog = new ServerFrog(id++, 20, 100, worldBodyUtils);
//        entities.add(frog);
//        fly = new ServerFly(id++, 20, 100, worldBodyUtils);
//        entities.add(fly);
//        blob = new ServerBlob(id++, 20, 100, worldBodyUtils);
//        entities.add(blob);
        if (server == null)
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
        audio.reset();
        if (server == null)
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
            server.sendToAllTCP(gameStateMessage);
            if (audio.audio != 0) {
                server.sendToAllUDP(audio);
            }
        }
        addOutgoingEvent(MessageObjectPool.instance.
                eventPool.obtain().set(State.RECEIVED, gameStateMessage));
        addOutgoingEvent(MessageObjectPool.instance.
                eventPool.obtain().set(State.RECEIVED, audio));
        
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
//            if (server != null)
//                server.sendToTCP(connection.getID(), "start");
        }
        
        @Override
        public void received(Connection connection, Object object) {
            try {
                if (object instanceof ControlsMessage) {
                    playerList.get(connection.getID()).
                    setCurrentControls((ControlsMessage) object);
                }
                if (object instanceof ClientDetailsMessage) {
                    updateClientDetails(connection, object);
                }
            }
            catch(Exception e) {
//                if (server != null) {
//                    server.sendToTCP(connection.getID(), "start");
//                }
                ServerPlayer player = new ServerPlayer(id++, playerPositions.get(i).x,
                        playerPositions.get(i).y, worldBodyUtils);
                i++;
                i %= playerPositions.size();
                playerList.put(connection.getID(), player);
                entities.add(player);
                if (object instanceof ClientDetailsMessage) {
                    updateClientDetails(connection, object);
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

    private void updateClientDetails(Connection connection, Object object) {
        int version = ((ClientDetailsMessage)object).protocolVersion;
        if (version != Constants.PROTOCOL_VERSION) {
            ServerStatusMessage message = new ServerStatusMessage();
            message.status = Status.DISCONNECT;
            if (version > Constants.PROTOCOL_VERSION) {
                message.toastText = "Please update server";
            } else if (version < Constants.PROTOCOL_VERSION) {
                message.toastText = "Please update client";
            }
            server.sendToTCP(connection.getID(), message);
            return;
        }
        
        playerList.get(connection.getID()).
        setName(((ClientDetailsMessage) object).name);
        PlayerNamesMessage players = new PlayerNamesMessage();
        for (ServerPlayer tempPlayer: playerList.values()) {
            players.players.put(tempPlayer.id, tempPlayer.getName());
        }
        if (server != null)
            server.sendToAllTCP(players);
        addOutgoingEvent(MessageObjectPool.instance.eventPool.obtain().
                set(State.RECEIVED, players));
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
        if (server != null)
            server.sendToAllTCP(body.getUserData().id);
        addOutgoingEvent(MessageObjectPool.instance.
                eventPool.obtain().set(State.RECEIVED, body.getUserData().id));
    }

    public void dispose() {
        if (server != null) {
            server.stop();
        }
    }
    
}

