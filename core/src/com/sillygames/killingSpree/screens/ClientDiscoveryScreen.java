package com.sillygames.killingSpree.screens;

import java.io.IOException;
import java.net.InetAddress;
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
import com.sillygames.killingSpree.InputController;
import com.sillygames.killingSpree.KillingSpree;
import com.sillygames.killingSpree.networking.MyClient;
import com.sillygames.killingSpree.networking.MyServer;
import com.sillygames.killingSpree.networking.messages.ConnectMessage;
import com.sillygames.killingSpree.screens.helpers.MyButton;

public class ClientDiscoveryScreen extends AbstractScreen {

    private BitmapFont font;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private ArrayList<MyButton> buttons;
    private MyButton currentButton;
    private MyButton backButton;
    private MyButton refreshButton;
    private ArrayList<MyButton> ipAddresses;
    private boolean markForDispose;
    
    public ClientDiscoveryScreen(KillingSpree game) {
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
        ipAddresses = new ArrayList<MyButton>();
        markForDispose = false;
        addAllButtons();
        addIpButtons();
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
        buttons.clear();
        batch.dispose();
        font.dispose();
        ipAddresses = null;
        buttons = null;
        font = null;
    }
    
    public void processInput() {
        currentButton = currentButton.process();
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
        } else if (currentButton == refreshButton) {
            addIpButtons();
        } else {
            String address = currentButton.getText();
            LobbyScreen lobbyScreen = new LobbyScreen(game);
            lobbyScreen.setHost(address);
            lobbyScreen.setServer(false);
            game.setScreen(lobbyScreen);
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
        for (MyButton button: ipAddresses) {
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
        for (MyButton button : ipAddresses) {
            button.render(batch, font, delta);
        }
    }
    
    private void addAllButtons() {
        refreshButton = addButton("Refresh", 300, 720);
        backButton = addButton("Back", 700, 720);
        currentButton = refreshButton;
        currentButton.setActive(true);
        refreshButton.setWest(backButton);
    }
    
    private void addIpButtons() {
        refreshButton.south = null;
        backButton.south = null;
        ipAddresses.clear();
        MyButton previousButton = refreshButton;
        float y = 580;
        for (InetAddress address : 
            MyClient.instance.client.discoverHosts(3000, 500)) {
            MyButton button = new MyButton(address.getHostName(), 300, y);
            ipAddresses.add(button);
            previousButton.setSouth(button);
            previousButton = button;
            y -= 150;
        }
        
    }
    

}
