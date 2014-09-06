package com.sillygames.killingSpree.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sillygames.killingSpree.InputController;
import com.sillygames.killingSpree.KillingSpree;
import com.sillygames.killingSpree.screens.helpers.MyButton;

public class MainMenuScreen extends AbstractScreen {

    private BitmapFont font;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private ArrayList<MyButton> buttons;
    private float slackTime;
    private MyButton currentButton;
    private MyButton newGameButton;
    private MyButton optionsButton;
    private MyButton exitButton;
    
    public MainMenuScreen(KillingSpree game) {
        super(game);
        show();
    }

    @Override
    public void render(float delta) {
        slackTime += delta;
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        processInput();
        renderButtons();
        batch.end();
        
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
        parameter.size = 200;
        font = generator.generateFont(parameter);
        generator.dispose();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);
        camera.setToOrtho(false, 1280, 720);
        buttons = new ArrayList<MyButton>();
        addAllButtons();
        slackTime = 0;
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
        batch.dispose();
        font.dispose();
    }
    
    public void processInput() {
        MyButton pressedButton = null;
        if (slackTime > 0.2f) {
            if (InputController.instance.axisDown()) {
                pressedButton = currentButton.south;
            } else if (InputController.instance.axisUp()) {
                pressedButton = currentButton.north;
            }
            if (pressedButton != null) {
                currentButton.setActive(false);
                currentButton = pressedButton;
                currentButton.setActive(true);
                slackTime = 0;
            }
        }
        if (InputController.instance.buttonA()){
            processButton();
        }
    }
    
    private void processButton() {
        buttons.clear();
        if (currentButton == newGameButton) {
            game.setScreen(new GameScreen(game));
        } else if (currentButton == optionsButton) {
            
        } else if (currentButton == exitButton) {
            Gdx.app.exit();
        }
    }

    public MyButton addButton(String text, float x, float y) {
        MyButton button = new MyButton(text, x, y);
        buttons.add(button);
        return button;
    }
    
    private void renderButtons() {
        for (MyButton button: buttons) {
            button.render(batch, font);
        }
    }
    
    private void addAllButtons() {
        newGameButton = addButton("New game", 100, 700);
        optionsButton = addButton("Options", 100, 500);
        exitButton = addButton("Exit", 100, 300);
        newGameButton.setActive(true);
        currentButton = newGameButton;
        optionsButton.setNorth(newGameButton);
        optionsButton.setSouth(exitButton);
        newGameButton.setNorth(exitButton);
    }

}
