package com.sillygames.killingSpree.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sillygames.killingSpree.KillingSpree;
import com.sillygames.killingSpree.controls.InputController;
import com.sillygames.killingSpree.helpers.MyButton;

public class OptionsScreen extends AbstractScreen {

    private BitmapFont font;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private ArrayList<MyButton> buttons;
    private MyButton currentButton;
    private MyButton backButton;
    private MyButton changeNameButton;
    private MyButton setControlsScaleButton;
    private boolean buttonPressed;
    
    public OptionsScreen(KillingSpree game) {
        super(game);
        show();
    }

    @Override
    public void render(float delta) {
        renderButtons(delta);
        processInput();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
    }

    @Override
    public void show() {
        font = game.getFont(170);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);
        camera.setToOrtho(false, 1280, 720);
        buttons = new ArrayList<MyButton>();
        addAllButtons();
        buttonPressed = false;
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
        currentButton = currentButton.process();
        if (InputController.instance.buttonA()){
            processButton();
            return;
        }
        if(Gdx.input.isTouched()) {
            processTouched();
        }
    }
    
    private void processButton() {
        if (buttonPressed)
            return;
        if (currentButton == changeNameButton) {
            final Preferences prefs = Gdx.app.getPreferences("profile");
            buttonPressed = true;
            Gdx.input.getTextInput(new TextInputListener() {
                
                @Override
                public void input(String text) {
                    prefs.putString("name", text);
                    prefs.flush();
                    buttonPressed = false;
                }
                
                @Override
                public void canceled() {
                    buttonPressed = false;
                }
            }, "Enter your name", prefs.getString("name"));
        } else if (currentButton == setControlsScaleButton) {
            final Preferences prefs = Gdx.app.getPreferences("settings");
            buttonPressed = true;
            Gdx.input.getTextInput(new TextInputListener() {
                
                @Override
                public void input(String text) {
                    try {
                        int scaling = Integer.parseInt(text);
                        prefs.putInteger("scaling", scaling);
                        prefs.flush();
                    } catch (Exception e) {
                        game.platformServices.toast("Please enter a valid integer");
                    }
                    buttonPressed = false;
                }
                
                @Override
                public void canceled() {
                    buttonPressed = false;
                }
            }, "Enter scaling percentage for onscreen controls",
            Integer.toString(prefs.getInteger("scaling")));
        } else if (currentButton == backButton) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void processTouched() {
        Vector2 touchVector = viewport.unproject(new Vector2(Gdx.input.getX(),
                Gdx.input.getY()));
        for (MyButton button: buttons) {
            if(button.isPressed(touchVector, font)) {
                if (currentButton == button) {
                    processButton();
                    return;
                }
                currentButton.setActive(false);
                currentButton = button;
                currentButton.setActive(true);
                renderButtons(0.01f);
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (MyButton button: buttons) {
            button.render(batch, font, delta);
        }
        batch.end();
    }
    
    private void addAllButtons() {
        changeNameButton = addButton("change name", 100, 650);
        setControlsScaleButton  = addButton("set buttons scale", 100, 450);
        backButton = addButton("back", 100, 250);
        changeNameButton.setActive(true);
        currentButton = changeNameButton;
        setControlsScaleButton.setNorth(changeNameButton);
        backButton.setNorth(setControlsScaleButton);
    }
    
}
