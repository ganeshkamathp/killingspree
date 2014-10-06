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
import com.sillygames.killingSpree.networking.messages.ClientDetailsMessage;
import com.sillygames.killingSpree.screens.settings.Constants;

public class GameScreen extends AbstractScreen {
    private WorldManager world;
    private WorldRenderer renderer;
    private boolean isServer;
    private Server server;
    private Client client;
    private float currentTime;
    private final float frameTime = 1 / 60.0f;
    
    public GameScreen(KillingSpree game) {
        super(game);
        show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (isServer) {
//            currentTime += delta;
//            while (currentTime >= frameTime) {
//                currentTime -= frameTime;
//                world.update(frameTime);
                world.update(delta);
//            }
        }
        renderer.render(delta);
        if (renderer.stateProcessor.disconnected) {
            game.platformServices.toast("server disconnected");
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void show() {
        currentTime = 0;
    }
    
    public void startServer(boolean lonely) {
        isServer = true;
        if (!lonely) {
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
    }
    
    public boolean loadLevel(String level, String host, String name) {
        if (isServer) {
            world = new WorldManager(server);
            if (server == null)
                world.loader.platformServices = game.platformServices;
        } else {
            client = new Client();
            NetworkRegisterer.register(client);
            client.start();
            try {
                client.connect(Constants.TIMEOUT, host,
                        Constants.GAME_TCP_PORT,
                        Constants.GAME_UDP_PORT);
            } catch (IOException e) {
                game.platformServices.toast("Server not found");
                e.printStackTrace();
                game.setScreen(new ClientDiscoveryScreen(game));
                return false;
            }
        }
        
        renderer = new WorldRenderer(world, client, game);
        renderer.loadLevel(level, isServer, name);
        return true;
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
        if (isServer) {
            world.dispose();
        }
        if (renderer != null)
            renderer.dispose();
        
    }

}
