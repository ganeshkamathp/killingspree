package com.sillygames.killingSpree;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.sillygames.killingSpree.pool.AssetLoader;
import com.sillygames.killingSpree.screens.GameScreen;
import com.sillygames.killingSpree.screens.SplashScreen;
import com.sillygames.killingSpree.sound.SFXPlayer;

public class KillingSpree extends ApplicationAdapter {

    Screen nextScreen;
    Screen currentScreen;
    private int width;
    private int height;
    private FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameter;
    public PlatformServices platformServices;
    
    public KillingSpree(PlatformServices platformServices) {
        super();
        this.platformServices = platformServices;
    }
    
	@Override
	public void create () {
	    generator = new FreeTypeFontGenerator
	            (Gdx.files.internal("fonts/splash.ttf"));
	    parameter = new FreeTypeFontParameter();
	    
	    AssetLoader.instance.loadAll();
	    currentScreen = new SplashScreen(this);
	    Gdx.audio.getClass();
//	    GameScreen gameScreen = new GameScreen(this);
//        gameScreen.startServer();
//        gameScreen.loadLevel("maps/retro-small.tmx", "localhost");
//        currentScreen = gameScreen;
	}

	@Override
	public void render () {
	    float delta = Gdx.graphics.getDeltaTime();
	    currentScreen.render(delta);
	}
	
	@Override
    public void resize(int width, int height) {
	    currentScreen.resize(width, height);
	    this.width = width;
	    this.height = height;
    }

    @Override
    public void pause() {
        currentScreen.pause();
    }

    @Override
    public void resume() {
        currentScreen.resume();
    }

    @Override
    public void dispose() {
        currentScreen.dispose();
        generator.dispose();
    }
    
    public void setScreen(Screen screen){
        nextScreen = screen;
        currentScreen.dispose();
        currentScreen = nextScreen;
        currentScreen.resize(width, height);
    }
    
    public BitmapFont getFont(int size) {
        parameter.size = size;
        return generator.generateFont(parameter);
    }
}
