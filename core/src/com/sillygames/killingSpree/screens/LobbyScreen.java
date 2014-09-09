package com.sillygames.killingSpree.screens;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.sillygames.killingSpree.KillingSpree;
import com.sillygames.killingSpree.controls.InputController;
import com.sillygames.killingSpree.networking.MyClient;
import com.sillygames.killingSpree.networking.MyServer;
import com.sillygames.killingSpree.networking.messages.ConnectMessage;
import com.sillygames.killingSpree.pooler.ObjectPool;
import com.sillygames.killingSpree.screens.helpers.MyButton;

public class LobbyScreen extends AbstractScreen {

    private BitmapFont font;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private ArrayList<MyButton> buttons;
    private MyButton currentButton;
    private MyButton startGameButton;
    private MyButton backButton;
    private boolean server;
    private ArrayList<String> ipAddresses;
    public LobbyClientListener clientListener;
    public LobbyServerListener serverListener;
    private boolean markForDispose;
    private boolean startGame;
    private String host; 
    
    public LobbyScreen(KillingSpree game) {
        super(game);
        show();
    }
    
    @Override
    public void render(float delta) {
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
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator
                (Gdx.files.internal("fonts/splash.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 120;
        font = generator.generateFont(parameter);
        generator.dispose();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);
        camera.setToOrtho(false, 1280, 720);
        buttons = new ArrayList<MyButton>();
        server = false;
        ipAddresses = new ArrayList<String>();
        clientListener = new LobbyClientListener();
        MyClient.instance.client.addListener(clientListener);
        markForDispose = false;
        startGame = false;
        host = "127.0.0.1";
    }

    public void setHost(String host) {
        this.host = host;
    }
    
    public void setServer(boolean server) {
        this.server = server;
        if (server) {
            MyServer.instance.start();
            serverListener = new LobbyServerListener();
            MyServer.instance.server.addListener(serverListener);
        }
        try {
//            Thread.currentThread().sleep(500);
            MyClient.instance.client.connect(5000, host, 2000, 3000);
        } catch (Exception e) {
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
        MyClient.instance.client.removeListener(clientListener);
        if (server) {
            MyServer.instance.server.removeListener(serverListener);
        }
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
            gameScreen.loadLevel("maps/retro.tmx", server);
            game.setScreen(gameScreen);
            return;
        }
        if (InputController.instance.buttonA() || markForDispose){
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
            if(server){
                MyServer.instance.stop();
            }
            MyClient.instance.stop();
        } else if (server && currentButton == startGameButton) {
            MyServer.instance.startGame();
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
        if (server) {
            startGameButton = addButton("Start", 300, 720);
            startGameButton.setWest(backButton);
            startGameButton.setEast(backButton);
        }
    }
    
    public class LobbyClientListener extends Listener{
    
        @Override
        public void disconnected(Connection connection) {
            currentButton = backButton;
            startGame = true;
        }

        @Override
        public void received(Connection connection, Object object) {
            if (object instanceof ConnectMessage) {
                ipAddresses = ((ConnectMessage) object).hosts;
                ObjectPool.instance.connectMessagePool.
                free((ConnectMessage) object);
            } else if (object instanceof String) {
                if(((String)object).matches("start")) {
                    startGame = true;
                }
            }
        }
        
    }
    
    public class LobbyServerListener extends Listener{
        
        @Override
        public void connected(Connection connection) {
            MyServer.instance.sendHosts();
        };
        
        @Override
        public void disconnected(Connection connection) {
            MyServer.instance.sendHosts();
        }
    }

}
