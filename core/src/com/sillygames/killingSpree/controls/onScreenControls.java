package com.sillygames.killingSpree.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class onScreenControls extends InputController {
    private Stage stage;
    private Touchpad touchpad;
    private TouchpadStyle touchpadStyle;
    private Button jumpButton;
    private Button shootButton;
    private Button throwGrenadeButton;
    private Skin skin;
    private boolean createObjectsWasPressed;
    private static float BUTTON_SIZE = 150f;

    public onScreenControls() {
        stage = new Stage();

        skin = new Skin();
        skin.add("knob", new Texture("controls/knob.png"));
        skin.add("buttonA", new Texture("controls/buttonA.png"));
        skin.add("buttonB", new Texture("controls/buttonB.png"));

        Drawable button;
        button = skin.getDrawable("knob");

        touchpadStyle = new TouchpadStyle();
        touchpadStyle.knob = button;
        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(0, 0, 250, 250);
        touchpad.setColor(touchpad.getColor().r, touchpad.getColor().g,
                touchpad.getColor().b, touchpad.getColor().a / 5);

        button = skin.getDrawable("buttonA");
        jumpButton = new Button(button);
        jumpButton.setColor(jumpButton.getColor().r, jumpButton.getColor().g,
                jumpButton.getColor().b, jumpButton.getColor().a / 5);

        button = skin.getDrawable("buttonB");
        shootButton = new Button(button);
        shootButton.setColor(shootButton.getColor().r,
                shootButton.getColor().g, shootButton.getColor().b,
                shootButton.getColor().a / 5);

//        throwGrenadeButton = new Button(button);
//        throwGrenadeButton.setColor(throwGrenadeButton.getColor().r,
//                throwGrenadeButton.getColor().g,
//                throwGrenadeButton.getColor().b,
//                throwGrenadeButton.getColor().a / 5);

        this.stage.addActor(touchpad);
        this.stage.addActor(jumpButton);
        this.stage.addActor(shootButton);
//        this.stage.addActor(throwGrenadeButton);
        Gdx.input.setInputProcessor(stage);
        resize();
    }

    @Override
    public boolean axisLeft() {
        return ((touchpad.getKnobPercentX() <= -0.2) || (InputController.instance
                    .axisLeft()));
    }

    @Override
    public boolean axisRight() {
        return ((touchpad.getKnobPercentX() >= 0.2) || InputController.instance
                    .axisRight());
    }

    public boolean axisUp() {
        return ((touchpad.getKnobPercentY() >= 0.4) || InputController.instance
                    .axisUp());
    }

    @Override
    public boolean axisDown() {
        return ((touchpad.getKnobPercentY() <= -0.6) || InputController.instance
                    .axisDown());
    }

    @Override
    public boolean buttonA() {
        return (jumpButton.isPressed() || InputController.instance
                    .buttonA());
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean createObjects() {
        boolean createObjectsStatus = (createObjectsWasPressed || InputController.instance
                .buttonB());
        createObjectsWasPressed = false;
        return createObjectsStatus;
    }

    @Override
    public boolean buttonX() {
        return (shootButton.isPressed() ||
                InputController.instance.buttonX());
    }

    public void render() {
        stage.draw();
    }
    
    public void dispose() {
        skin.dispose();
    }

    public void resize() {
        shootButton.setBounds(Gdx.graphics.getWidth() - BUTTON_SIZE,
                BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE);
        jumpButton.setBounds(Gdx.graphics.getWidth() - 2 * BUTTON_SIZE, 0,
                BUTTON_SIZE, BUTTON_SIZE);
        // testButton.setBounds(Gdx.graphics.getWidth() / 2 - BUTTON_SIZE / 2,
        // Gdx.graphics.getHeight() - BUTTON_SIZE, BUTTON_SIZE,
        // BUTTON_SIZE);
//        throwGrenadeButton.setBounds(Gdx.graphics.getWidth() - BUTTON_SIZE,
//                2.1f * BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE);
    }
}