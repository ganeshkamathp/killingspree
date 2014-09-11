package com.sillygames.killingSpree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sillygames.killingSpree.KillingSpree;

public class SplashScreen extends AbstractScreen {

    private BitmapFont font;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private float totalTime = 0;

    public SplashScreen(KillingSpree game) {
        super(game);
        show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.drawMultiLine(batch, "Silly\ngames", Gdx.graphics.getWidth()/2-200,
                Gdx.graphics.getHeight() - 120);
        batch.end();
        if (totalTime < 0f){
            totalTime += delta;
        } else {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        float aspectRatio = (float)width / height;
        camera.setToOrtho(false, 720 * aspectRatio, 720);
        camera.update();
    }

    @Override
    public void show() {
        font = game.getFont(200);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
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
        font.dispose();
        batch.dispose();
    }

}
