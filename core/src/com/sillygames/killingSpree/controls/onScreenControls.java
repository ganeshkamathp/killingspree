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
    private Button leftButton;
    private Button rightButton;
    private Button upButton;
    private Button jumpButton;
    private Button shootButton;
    private Button throwBombButton;
    private Button closeButton;
    private Skin skin;
    private boolean createObjectsWasPressed;
    private static float BUTTON_SIZE = 125f;

    public onScreenControls() {
        stage = new Stage();

        skin = new Skin();
        skin.add("buttonA", new Texture("controls/buttonA.png"));
        skin.add("buttonB", new Texture("controls/buttonB.png"));
        skin.add("buttonX", new Texture("controls/buttonX.png"));
        skin.add("buttonLeft", new Texture("controls/buttonLeft.png"));
        skin.add("buttonRight", new Texture("controls/buttonRight.png"));
        skin.add("buttonUp", new Texture("controls/buttonUp.png"));
        skin.add("closeButton", new Texture("controls/closeButton.png"));
        

        Drawable button;


        button = skin.getDrawable("buttonLeft");
        leftButton = new Button(button);
        leftButton.setColor(leftButton.getColor().r, leftButton.getColor().g,
                leftButton.getColor().b, leftButton.getColor().a / 5);
        
        button = skin.getDrawable("buttonRight");
        rightButton = new Button(button);
        rightButton.setColor(rightButton.getColor().r, rightButton.getColor().g,
                rightButton.getColor().b, rightButton.getColor().a / 5);
        
        button = skin.getDrawable("buttonUp");
        upButton = new Button(button);
        upButton.setColor(upButton.getColor().r, upButton.getColor().g,
                upButton.getColor().b, upButton.getColor().a / 5);
        
        button = skin.getDrawable("buttonA");
        jumpButton = new Button(button);
        jumpButton.setColor(jumpButton.getColor().r, jumpButton.getColor().g,
                jumpButton.getColor().b, jumpButton.getColor().a / 5);

        button = skin.getDrawable("buttonX");
        shootButton = new Button(button);
        shootButton.setColor(shootButton.getColor().r,
                shootButton.getColor().g, shootButton.getColor().b,
                shootButton.getColor().a / 5);

        button = skin.getDrawable("buttonB");
        throwBombButton = new Button(button);
        throwBombButton.setColor(throwBombButton.getColor().r,
                throwBombButton.getColor().g,
                throwBombButton.getColor().b,
                throwBombButton.getColor().a / 5);
        
        button = skin.getDrawable("closeButton");
        closeButton = new Button(button);
        closeButton.setColor(closeButton.getColor().r,
                closeButton.getColor().g,
                closeButton.getColor().b,
                closeButton.getColor().a / 5);

        this.stage.addActor(jumpButton);
        this.stage.addActor(shootButton);
        this.stage.addActor(throwBombButton);
        this.stage.addActor(closeButton);
        this.stage.addActor(upButton);
        this.stage.addActor(rightButton);
        this.stage.addActor(leftButton);
        Gdx.input.setInputProcessor(stage);
        resize();
    }

    @Override
    public boolean axisLeft() {
        return (leftButton.isPressed() || (InputController.instance
                    .axisLeft()));
    }

    @Override
    public boolean axisRight() {
        return (rightButton.isPressed() || InputController.instance
                    .axisRight());
    }

    public boolean axisUp() {
        return (upButton.isPressed() || InputController.instance
                    .axisUp());
    }

    @Override
    public boolean axisDown() {
        return false;
    }

    @Override
    public boolean buttonA() {
        return (jumpButton.isPressed() || InputController.instance
                    .buttonA());
    }

    @Override
    public boolean buttonB() {
        return (throwBombButton.isPressed() || InputController.instance
                .buttonB());
    }
    
    @Override
    public boolean closeButton() {
        return (closeButton.isPressed() || InputController.instance
                .closeButton());
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
        float buttonSize = BUTTON_SIZE * (float) Gdx.graphics.getWidth() / 1280f;
        shootButton.setBounds(Gdx.graphics.getWidth() - buttonSize * 1.1f,
                buttonSize, buttonSize, buttonSize);
        jumpButton.setBounds(Gdx.graphics.getWidth() - 2 * buttonSize, 10,
                buttonSize, buttonSize);
        throwBombButton.setBounds(Gdx.graphics.getWidth() - buttonSize * 1.1f,
                2f * buttonSize  * 1.1f, buttonSize, buttonSize);
        closeButton.setBounds(Gdx.graphics.getWidth() - buttonSize * 1.1f,
                Gdx.graphics.getHeight() - buttonSize * 1.1f, buttonSize,
                buttonSize);
        buttonSize *= 0.8f;
        leftButton.setBounds(buttonSize * 0.1f,
                buttonSize * 0.1f, buttonSize, buttonSize);
        upButton.setBounds(buttonSize * 1.6f / 2,
                buttonSize, buttonSize, buttonSize);
        rightButton.setBounds(buttonSize * 1.6f,
                buttonSize * 0.1f, buttonSize, buttonSize);
    }
}