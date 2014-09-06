package com.sillygames.killingSpree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.sillygames.killingSpree.KillingSpree;
import com.sillygames.killingSpree.managers.WorldManager;
import com.sillygames.killingSpree.screens.managers.WorldRenderer;

public class GameScreen extends AbstractScreen {
    public static final String TAG = "GameScreen";
    private WorldManager world;
    private WorldRenderer renderer;
    

    public GameScreen(KillingSpree game) {
        super(game);
        show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.update(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void show() {
        world = new WorldManager();
        renderer = new WorldRenderer(world);
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
    }

}
