package com.sillygames.killingSpree.helpers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.controls.InputController;

public class MyButton {
    public MyButton north;
    public MyButton south;
    public MyButton east;
    public MyButton west;
    private String text;
    private boolean active = true;
    private float x;
    private float y;
    private float slackTime;

    
    public MyButton(String text, float x, float y){
        this.text = text;
        this.x = x;
        this.y = y;
        active = false;
        slackTime = 0;
    }
    
    public void setActive(boolean active){
        slackTime = 0;
        this.active = active;
    }
    
    public void render(SpriteBatch batch, BitmapFont font, float delta){
        if (active) {
            font.setColor(1, 1, 1, 1);
            font.draw(batch, text, x + MathUtils.random(0, 1), y + MathUtils.random(0, 1));
        } else {
            font.setColor(0.5f, 0.5f, 0.5f, 1);
            font.draw(batch, text, x, y);
        }
        slackTime += delta;
    }

    public void setNorth(MyButton north) {
        this.north = north;
        north.south = this;
    }
    
    public void setSouth(MyButton south) {
        this.south = south;
        south.north = this;
    }
    
    public void setEast(MyButton east) {
        this.east = east;
        east.west = this;
    }
    
    public void setWest(MyButton west) {
        this.west = west;
        west.east = this;
    }

    public MyButton process() {
        MyButton pressedButton = null;
        if (slackTime > 0.2f) {
            if (InputController.instance.axisDown()) {
                pressedButton = south;
            } else if (InputController.instance.axisUp()) {
                pressedButton = north;
            } else if (InputController.instance.axisLeft()) {
                pressedButton = east;
            } else if (InputController.instance.axisRight()) {
                pressedButton = west;
            }
            if (pressedButton != null) {
                active = false;
                pressedButton.setActive(true);
                return pressedButton;
            }
        }
        return this;
    }

    public boolean isPressed(Vector2 touchVector, BitmapFont font) {
        TextBounds bounds = font.getBounds(text);
        if(touchVector.x > x && touchVector.x < x + bounds.width &&
                touchVector.y < y && touchVector.y > y - bounds.height) {
            return true;
        }
        return false;
    }

    public String getText() {
        return text;
    }
}
