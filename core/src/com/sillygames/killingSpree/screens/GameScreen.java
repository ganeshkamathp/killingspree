package com.sillygames.killingSpree.screens;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.sillygames.killingSpree.KillingSpree;
import com.sillygames.killingSpree.managers.WorldManager;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.networking.NetworkRegisterer;
import com.sillygames.killingSpree.screens.settings.Constants;

public class GameScreen extends AbstractScreen {
    public static final String TAG = "GameScreen";
    private WorldManager world;
    private WorldRenderer renderer;
    private boolean isServer;
    private Server server;
    private Client client;

    public GameScreen(KillingSpree game) {
        super(game);
        show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (isServer) {
            world.update(delta);
        }
        renderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void show() {
    }
    
    public void startServer() {
        isServer = true;
        server = new Server();
        NetworkRegisterer.register(server);
        server.start();
        try {
            server.bind(Constants.GAME_TCP_PORT,
                    Constants.GAME_UDP_PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadLevel(String level, String host) {
        if (isServer) {
            world = new WorldManager(server);
        }
        
        client = new Client();
        NetworkRegisterer.register(client);
        client.start();
        try {
            client.connect(Constants.TIMEOUT, host,
                    Constants.GAME_TCP_PORT,
                    Constants.GAME_UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        renderer = new WorldRenderer(world, client);
        renderer.loadLevel(level, isServer);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}
