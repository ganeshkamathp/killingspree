package com.sillygames.killingSpree;

import java.io.IOException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sillygames.killingSpree.networking.MyClient;
import com.sillygames.killingSpree.networking.MyServer;
import com.sillygames.killingSpree.screens.GameScreen;
import com.sillygames.killingSpree.screens.SplashScreen;

public class KillingSpree extends ApplicationAdapter {
    
    Screen nextScreen;
    Screen currentScreen;
    private int width;
    private int height;
    
	@Override
	public void create () {
	    GameScreen gameScreen = new GameScreen(this);
        gameScreen.loadLevel("maps/retro.tmx", true);
        MyServer.instance.start();
        try {
            MyClient.instance.client.connect(5000, "localhost", 2000, 3000);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        currentScreen = gameScreen;
	    currentScreen.show();
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
        MyServer.instance.dispose();
        MyClient.instance.dispose();
    }
    
    public void setScreen(Screen screen){
        nextScreen = screen;
        currentScreen.dispose();
        currentScreen = nextScreen;
        currentScreen.resize(width, height);
    }
}
