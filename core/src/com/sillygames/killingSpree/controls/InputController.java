package com.sillygames.killingSpree.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controllers;

public class InputController {
    
    public static final InputController instance = new InputController();
    
    public boolean controllerEnabled() {
        return (Controllers.getControllers().size > 0);
    }

    public boolean axisLeft() {
        return (controllerEnabled() && Controllers
                .getControllers().get(0).getAxis(8) == -1
                || Gdx.input.isKeyPressed(Keys.LEFT));
    }

    public boolean axisRight() {
        return (controllerEnabled() && Controllers
                .getControllers().get(0).getAxis(8) == 1
                || Gdx.input.isKeyPressed(Keys.RIGHT));
    }

    public boolean axisUp() {
        return (controllerEnabled() && Controllers
                .getControllers().get(0).getAxis(9) == -1)
                || Gdx.input.isKeyPressed(Keys.UP);
    }

    public boolean axisDown() {
        return (controllerEnabled() && Controllers
                .getControllers().get(0).getAxis(9) == 1)
                || Gdx.input.isKeyPressed(Keys.DOWN);
    }

    public boolean buttonA() {
        return (controllerEnabled() && Controllers
                .getControllers().get(0).getButton(96) ||
                Gdx.input.isKeyPressed(Keys.SPACE));
    }

    public boolean buttonB() {
        return (controllerEnabled() && Controllers
                .getControllers().get(0).getButton(97));
    }

    public boolean buttonX() {
        return (controllerEnabled() && Controllers
                .getControllers().get(0).getButton(99) ||
                Gdx.input.isKeyPressed(Keys.CONTROL_LEFT));
    }

    public boolean buttonY() {
        return (controllerEnabled() && Controllers
                .getControllers().get(0).getButton(100));
    }

    public boolean rightTrigger() {
        return (controllerEnabled() && Controllers
                .getControllers().get(0).getAxis(7) == 1);
    }
    
}
