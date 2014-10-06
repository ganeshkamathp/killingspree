package com.sillygames.killingSpree.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.sillygames.killingSpree.pool.AssetLoader;
import com.sillygames.killingSpree.serverEntities.ServerPlayer;

public class HUDRenderer {
    
    private Sprite bomb;
    private BitmapFont font;
    
    public HUDRenderer() {
        bomb = new Sprite(AssetLoader.instance.
                getTexture("sprites/bomb.png"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 7;
        font = new FreeTypeFontGenerator
                (Gdx.files.internal("fonts/game.ttf")).generateFont(parameter);
    }
    
    public void render(SpriteBatch batch, float x, float y, short extra, String name) {
        bomb.setSize(5, 5);
        int totalBombs = (extra >> 1) & 0x7;
        String score = name + ":" + Integer.toString((extra >> 4));
        float startX = x + ((ServerPlayer.WIDTH + 6f) / 2)
                - (font.getBounds(score).width/2);
        font.draw(batch, score, startX, y + 35);
        for (int i=0; i < totalBombs; i++) {
            bomb.setPosition(x + i * 6, y + 21);
            bomb.draw(batch);
        }
    }

    public void dispose() {
        font.dispose();
    }
    
}
