package com.sillygames.killingSpree.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.sillygames.killingSpree.KillingSpree;
import com.sillygames.killingSpree.controls.InputController;
import com.sillygames.killingSpree.helpers.MyButton;
import com.sillygames.killingSpree.networking.NetworkRegisterer;
import com.sillygames.killingSpree.networking.messages.ConnectMessage;
import com.sillygames.killingSpree.pool.MessageObjectPool;
import com.sillygames.killingSpree.screens.settings.Constants;

public class LobbyScreen extends AbstractScreen {

    private BitmapFont font;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private ArrayList<MyButton> buttons;
    private MyButton currentButton;
    private MyButton startGameButton;
    private MyButton backButton;
    private boolean isServer;
    private ArrayList<String> ipAddresses;
    public LobbyClientListener clientListener;
    public LobbyServerListener serverListener;
    private boolean markForDispose;
    private boolean startGame;
    private String host; 
    private Client client;
    private Server server;
    
    public LobbyScreen(KillingSpree game) {
        super(game);
        show();
    }
    
    @Override
    public void render(float delta) {
//        Gdx.app.log("rendering", "lobby");
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderButtons(delta);
        batch.end();
        processInput();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
    }

    @Override
    public void show() {
        font = game.getFont(120);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);
        camera.setToOrtho(false, 1280, 720);
        buttons = new ArrayList<MyButton>();
        isServer = false;
        ipAddresses = new ArrayList<String>();
        markForDispose = false;
        startGame = false;
        host = "127.0.0.1";
    }

    public void setHost(String host) {
        this.host = host;
    }
    
    public void setServer(boolean isServer) {
        this.isServer = isServer;
        try {
            if (isServer) {
                server = new Server();
                NetworkRegisterer.register(server);
                serverListener = new LobbyServerListener();
                server.addListener(serverListener);
                server.start();
                server.bind(Constants.DISCOVERY_TCP_PORT,
                            Constants.DISCOVERY_UDP_PORT);
//                Thread.currentThread().sleep(200);
            }
            
            client = new Client();
            NetworkRegisterer.register(client);
            client.start();
            clientListener = new LobbyClientListener();
            client.addListener(clientListener);
            client.connect(5000, host, 
                    Constants.DISCOVERY_TCP_PORT,
                    Constants.DISCOVERY_UDP_PORT);
        } catch (Exception e) {
            currentButton = backButton;
            markForDispose = true;
            e.printStackTrace();
        }
        addAllButtons();
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
        ipAddresses.clear();
        client.removeListener(clientListener);
        if (isServer) {
            server.removeListener(serverListener);
            server.stop();
        }
        client.stop();
        buttons.clear();
        batch.dispose();
        font.dispose();
        ipAddresses = null;
        buttons = null;
        font = null;
    }
    
    public void processInput() {
        currentButton = currentButton.process();
        if (startGame) {
            GameScreen gameScreen = new GameScreen(game);
            if(isServer) {
                gameScreen.startServer(ipAddresses.size() == 1);
            }
            gameScreen.loadLevel("maps/retro-small.tmx", host);
            game.setScreen(gameScreen);
            return;
        }
        if (InputController.instance.buttonA() || markForDispose) {
            processButton();
            return;
        }
        if(Gdx.input.isTouched()) {
            processTouched();
        }
    }
    
    private void processButton() {
        if (currentButton == backButton) {
            game.setScreen(new MainMenuScreen(game));
            if(isServer){
                server.stop();
            }
            client.stop();
        } else if (isServer && currentButton == startGameButton) {
            server.sendToAllTCP("start");
        }
    }
    
    private void processTouched() {
        Vector2 touchVector = viewport.unproject(new Vector2(Gdx.input.getX(),
                Gdx.input.getY()));
        for (MyButton button: buttons) {
            if(button.isPressed(touchVector, font)) {
                currentButton = button;
                processButton();
                return;
            }
        }
    }

    public MyButton addButton(String text, float x, float y) {
        MyButton button = new MyButton(text, x, y);
        buttons.add(button);
        return button;
    }
    
    private void renderButtons(float delta) {
        for (MyButton button : buttons) {
            button.render(batch, font, delta);
        }
        float y = 580;
        font.setColor(0.5f, 0.5f, 1f, 1f);
        for (String name : ipAddresses) {
            font.draw(batch, name, 300, y);
            y -= 150;
        }
    }
    
    private void addAllButtons() {
        backButton = addButton("Back", 700, 720);
        currentButton = backButton;
        currentButton.setActive(true);
        if (isServer) {
            startGameButton = addButton("Start", 300, 720);
            startGameButton.setWest(backButton);
            startGameButton.setEast(backButton);
        }
    }
    
    public class LobbyClientListener extends Listener{
    
        @Override
        public void disconnected(Connection connection) {
            currentButton = backButton;
            markForDispose = true;
        }

        @Override
        public void received(Connection connection, Object object) {
            if (object instanceof ConnectMessage) {
                ipAddresses = ((ConnectMessage) object).hosts;
            } else if (object instanceof String) {
                if(((String)object).matches("start")) {
                    startGame = true;
                }
            }
        }
        
    }
    
    public class LobbyServerListener extends Listener {
        
        @Override
        public void connected(Connection connection) {
            sendHosts();
        };
        
        @Override
        public void disconnected(Connection connection) {
            sendHosts();
        }
        
        public void sendHosts() {
            ConnectMessage message = new ConnectMessage();
            for(Connection connection : server.getConnections()) {
                message.insertNewHost(connection
                        .getRemoteAddressTCP().getHostName());
            }
            server.sendToAllTCP(message);
        }
    }

}
