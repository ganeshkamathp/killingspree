package com.sillygames.killingSpree.screens.helpers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyButton {
    public MyButton north;
    public MyButton south;
    public MyButton east;
    public MyButton west;
    private String text;
    private boolean active = true;
    private float x;
    private float y;
    
    public MyButton(String text, float x, float y){
        this.text = text;
        this.x = x;
        this.y = y;
        active = false;
    }
    
    public void setActive(boolean active){
        this.active = active;
    }
    
    public void render(SpriteBatch batch, BitmapFont font){
        if (active) {
            font.setColor(1, 1, 1, 1);
        } else {
            font.setColor(0.5f, 0.5f, 0.5f, 1);
        }
        font.draw(batch, text, x, y);
    }

    public void setNorth(MyButton north) {
        this.north = north;
        north.south = this;
    }
    
    public void setSouth(MyButton south) {
        this.south = south;
        south.north = this;
    }

    public void process() {
        
    }
}
